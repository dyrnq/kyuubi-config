package com.dyrnq.kyuubi.command;

import com.dyrnq.kyuubi.dto.KyuubiConfigSection;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Strings;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import picocli.CommandLine;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "kafka", aliases = {"ka"}, description = "kafka")

@Slf4j
public class KafkaConfig extends CommonOptions implements Callable<Integer> {
    @CommandLine.Option(names = {"-n", "--no-section"}, description = "")
    boolean noSection;

    @CommandLine.Option(names = {"-ver", "-fv", "--kafka-version"}, description = "kafka version e.g. 39", defaultValue = "master")
    String kafkaVersion;

    @CommandLine.Option(names = {"-f", "--format"}, description = "format", defaultValue = "json")
    String format;


    /**
     * Walk forward through siblings of {@code from} (inclusive of from itself)
     * and return the first element whose tag name matches {@code tag}, or null.
     * Used for the alt-structure (system-properties) parser where the
     * description <p> and metadata <table> are siblings of the wrapping <ul>,
     * not children of the property's <li>.
     */
    private static Element nextSiblingOfTag(Element from, String tag) {
        Element n = from;
        while (n != null) {
            if (tag.equalsIgnoreCase(n.tagName())) return n;
            n = n.nextElementSibling();
        }
        return null;
    }


    /**
     * Kafka 3.6+ documentation site was rebuilt on Hugo + Docsy. Configurations
     * are no longer embedded in a single page under script#configuration-template;
     * instead each config type lives at its own URL like
     * /{version}/configuration/broker-configs/. The exact set of sub-pages and
     * even their URL slugs differ between versions (e.g. 36-39 use
     * "topic-level-configs" while master uses "topic-configs"), so we discover
     * the live list by scraping the configuration/ index page instead of
     * hard-coding it.
     * <p>
     * Most sub-pages use the standard structure:
     *   ul.config-list > li > h4 + p + table/tbody/tr/th/td
     * but the system-properties page uses an alt structure:
     *   ul > li > h4[id]  followed by sibling <p> + <table>/tr/th/td
     * The parser below handles both.
     */

    @Override
    public Integer call() throws Exception {
        String versionPath = Strings.CI.equals("master", kafkaVersion) ? "43" : kafkaVersion;
        String baseUrl = "https://kafka.apache.org/" + versionPath + "/configuration/";

        List<KyuubiConfigSection> result = new ArrayList<>();
        boolean anySuccess = false;

        // Step 1: discover the live sub-page list from the configuration/ index.
        List<String> paths;
        try {
            Connection indexConn = applyProxy(Jsoup.connect(baseUrl));
            indexConn.followRedirects(true);
            Document indexDoc = indexConn.timeout(2 * 60 * 1000).get();
            paths = new ArrayList<>();
            for (Element a : indexDoc.select("a[href]")) {
                String href = a.attr("href");
                // internal links like "/36/configuration/broker-configs" or
                // "/36/configuration/broker-configs/"
                String prefix = "/" + versionPath + "/configuration/";
                if (!href.startsWith(prefix)) continue;
                String tail = href.substring(prefix.length());
                // skip empty / page itself / anchor-only
                if (tail.isEmpty() || tail.startsWith("#")) continue;
                // strip trailing slash and any anchor
                int slash = tail.indexOf('/');
                if (slash >= 0) tail = tail.substring(0, slash);
                int hash = tail.indexOf('#');
                if (hash >= 0) tail = tail.substring(0, hash);
                if (tail.isEmpty()) continue;
                // Hugo generates a print view at /<page>/_print/ which is just
                // a stripped copy of the page itself - skip it.
                if (tail.equals("_print")) continue;
                if (!paths.contains(tail)) paths.add(tail);
            }
            log.info("kafka {} : discovered {} config pages: {}", kafkaVersion, paths.size(), paths);
        } catch (IOException e) {
            log.error("kafka {} : failed to fetch index {} ({})", kafkaVersion, baseUrl, e.getMessage());
            return 100;
        }

        // Step 2: fetch each discovered sub-page and parse its config list.
        for (String path : paths) {
            String url = baseUrl + path + "/";
            try {
                Connection connection = applyProxy(Jsoup.connect(url));
                connection.followRedirects(true);
                connection.timeout(2 * 60 * 1000);
                int status = connection.execute().statusCode();
                if (status == 404) {
                    log.info("kafka {} : {} returned 404, skipping", kafkaVersion, url);
                    continue;
                }
                Document doc = connection.get();

                Elements uls = doc.select("ul.config-list");
                boolean altStructure = uls.isEmpty();
                if (altStructure) {
                    // system-properties page has no ul.config-list; check for the
                    // alt-structure marker (an h4[id] inside a ul) before giving up.
                    Elements altMarkers = doc.select("ul > li > h4[id]");
                    if (altMarkers.isEmpty()) {
                        log.info("kafka {} : no ul.config-list at {} (empty page, skipping)", kafkaVersion, url);
                        continue;
                    }
                    log.info("kafka {} : {} has no ul.config-list but found {} alt-structure property heads", kafkaVersion, url, altMarkers.size());
                }
                anySuccess = true;

                String sectionName = path;
                Element h1 = doc.selectFirst("h1");
                if (h1 != null && !h1.text().trim().isEmpty()) {
                    sectionName = h1.text().trim();
                }
                log.info("kafka {} : section '{}' from {}", kafkaVersion, sectionName, url);

                List<Map<String, String>> subSectionList = new LinkedList<>();
                if (!altStructure) {
                    // Standard structure: ul.config-list > li > h4 + p + table/tbody/tr/th/td
                    for (Element ul : uls) {
                        for (Element li : ul.select("li")) {
                            Map<String, String> rowMap = new LinkedHashMap<>();
                            Element h4 = li.selectFirst("h4");
                            if (h4 == null) continue;
                            String key = h4.text().trim();
                            Element p = li.selectFirst("p");
                            rowMap.put("Name", key);
                            rowMap.put("Desc", p != null ? p.text().trim() : "");
                            for (Element row : li.select("tbody tr")) {
                                Element th = row.selectFirst("th");
                                Element td = row.selectFirst("td");
                                if (th == null || td == null) continue;
                                rowMap.put(th.text().trim().replace(":", ""), td.text().trim());
                            }
                            subSectionList.add(rowMap);
                        }
                    }
                } else {
                    // Alt structure (system-properties): <ul><li><h4 id=...>key</h4></li></ul>
                    // followed by sibling <p> description, then sibling <table> with
                    // metadata rows (no <tbody> wrapper).
                    Elements propHeads = doc.select("ul > li > h4[id]");
                    log.info("kafka {} : alt structure, found {} property headers", kafkaVersion, propHeads.size());
                    for (Element h4 : propHeads) {
                        // skip nav/toctoc h4s (none of those have ids in practice)
                        if (h4.id().isEmpty()) continue;
                        Element ul = h4.parent().parent(); // h4 > li > ul
                        Map<String, String> rowMap = new LinkedHashMap<>();
                        rowMap.put("Name", h4.text().trim());
                        // first <p> sibling of the wrapping <ul>
                        Element descEl = nextSiblingOfTag(ul, "p");
                        rowMap.put("Desc", descEl != null ? descEl.text().trim() : "");
                        Element table = nextSiblingOfTag(ul, "table");
                        if (table != null) {
                            for (Element row : table.select("tr")) {
                                Element th = row.selectFirst("th");
                                Element td = row.selectFirst("td");
                                if (th == null || td == null) continue;
                                rowMap.put(th.text().trim().replace(":", ""), td.text().trim());
                            }
                        }
                        subSectionList.add(rowMap);
                    }
                }

                if (!subSectionList.isEmpty()) {
                    result.add(new KyuubiConfigSection(sectionName, subSectionList));
                }
            } catch (IOException e) {
                log.warn("kafka {} : failed to fetch {} ({})", kafkaVersion, url, e.getMessage());
            }
        }

        if (!anySuccess) {
            log.error("kafka {} : no configuration pages could be fetched", kafkaVersion);
            return 100;
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        if (Strings.CI.equals("json", format)) {
            if (noSection) {
                List<Map<String, String>> noSectionList = new LinkedList<>();
                for (KyuubiConfigSection kyuubiConfigSection : result) {
                    noSectionList.addAll(kyuubiConfigSection.getList());
                }
                System.out.println(gson.toJson(noSectionList));
            } else {
                System.out.println(gson.toJson(result));
            }
        }
        if (Strings.CI.equals("conf", format)) {
            for (KyuubiConfigSection kyuubiConfigSection : result) {
                if (!noSection) {
                    System.out.println("#" + kyuubiConfigSection.getName());
                }
                kyuubiConfigSection.getList().forEach(c -> {
                    System.out.println(c.get("Name") + "=" + c.get("Default"));
                });
            }

        }

        return 0;
    }
}