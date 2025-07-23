package com.dyrnq.kyuubi.command;

import com.dyrnq.kyuubi.dto.KyuubiConfigSection;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
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


    @Override
    public Integer call() throws Exception {
//        List<Map<String, String>> noSectionList = new LinkedList<>();
        List<KyuubiConfigSection> result = new ArrayList<>();

        for (String url :
                new String[]{
                        "https://kafka.apache.org/documentation/",

                }
        ) {
            try {
                if (!Strings.CI.equals("master", kafkaVersion)) {
                    url = Strings.CS.replace(url, "documentation/", kafkaVersion + "/documentation.html");
                }
                url = url + "#configuration";


                Connection connection = Jsoup.connect(String.format(url, kafkaVersion));
                Document doc = connection.timeout(2 * 60 * 1000).get();
                Element script = doc.selectFirst("script#configuration-template");
                String scriptHtml = RegExUtils.replaceAll(script.html(), "\n|\r", "");
                scriptHtml = RegExUtils.replaceAll(scriptHtml, ">\\s+<", "><");


                Document docScript = Jsoup.parse(scriptHtml, Parser.htmlParser());
                Elements title = docScript.children().select("h3");


                //3.1 Broker Configs 3.2 Topic-Level Configs 3.3 Producer Configs 3.4 Consumer Configs 3.5 Kafka Connect Configs 3.6 Kafka Streams Configs 3.7 Admin Configs 3.8 MirrorMaker Configs 3.9 System Properties 3.10 Tiered Storage Configs 3.11 Configuration Providers
                //IOUtils.write(scriptHtml, new FileOutputStream("kafka/" + kafkaVersion + "/full.html"));

                Map<String, Document> subDocs = new LinkedHashMap<>();
                log.info("title.size()={}", title.size());
                for (int i = 0; i < title.size(); i++) {
                    Element currentElement = title.get(i);
                    String current = currentElement.outerHtml();
                    String next = "LAST";
                    String subDocHtml = null;
                    if (i == title.size() - 1) {
                        log.info("LAST={}", current);
                        subDocHtml =
                                StringUtils.substringAfter(scriptHtml, current);
                    } else {
                        Element nextElement = title.get(i + 1);
                        next = nextElement.outerHtml();
                        subDocHtml = StringUtils.substringBetween(scriptHtml, current, next);
                    }
                    if (subDocHtml == null) {
                        log.warn("Cannot find subDocHtml for {}--{}", current, next);
                        continue;
                    }
                    log.info("SUB---{}----{}----{}", i, current, next);
//                    log.info(subDocHtml);
                    //IOUtils.write(subDocHtml, new FileOutputStream("kafka/" + kafkaVersion + "/" + currentElement.text() + ".html"));
                    try {
                        Document subDoc = Jsoup.parse(subDocHtml);
                        subDocs.put(currentElement.text(), subDoc);
                    } catch (Exception e) {
                        log.error("Error parsing subDocHtml", e);
                    }
                }


                log.info("subDocs.size()={}", subDocs.size());
                subDocs.forEach((k, var) -> {
                    KyuubiConfigSection kyuubiConfigSection = new KyuubiConfigSection();
                    List<Map<String, String>> subSectionList = new LinkedList<>();

                    Elements uls = var.select("ul.config-list");
                    log.info("uls.size()={}", uls.size());
                    for (Element table : uls) {

                        Elements lis = table.select("li");

                        for (Element li : lis) {

                            Map<String, String> rowMap = new LinkedHashMap<>();
                            Element h4 = li.selectFirst("h4");
                            Element p = li.selectFirst("p");
                            if (h4 == null) continue;
                            String key = h4 != null ? h4.text().trim() : "NoneKey";
                            String desc = p != null ? p.text().trim() : "";
                            rowMap.put("Name", key);
                            rowMap.put("Desc", desc);
                            Elements rows = li.select("tbody tr");
                            for (Element row : rows) {
                                Element th = row.selectFirst("th");
                                Element td = row.selectFirst("td");
                                rowMap.put(th.text().trim().replace(":", ""), td.text().trim());
                            }
                            subSectionList.add(rowMap);
//                            noSectionList.add(rowMap);

                        }
                    }

                    kyuubiConfigSection.setName(k);
                    kyuubiConfigSection.setList(subSectionList);
                    result.add(kyuubiConfigSection);

                });

            } catch (IOException e) {
                log.error(e.getMessage(), e);
                return 100;
            }
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
