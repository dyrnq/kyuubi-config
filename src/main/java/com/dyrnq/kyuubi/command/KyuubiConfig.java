package com.dyrnq.kyuubi.command;

import com.dyrnq.kyuubi.dto.KyuubiConfigSection;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Strings;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import picocli.CommandLine;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "kyuubi", aliases = {"k"}, description = "kyuubi")

@Slf4j
public class KyuubiConfig extends CommonOptions implements Callable<Integer> {
    @CommandLine.Option(names = {"-n", "--no-section"}, description = "")
    boolean noSection;

    @CommandLine.Option(names = {"-ver", "-kv", "--kyuubi-version"}, description = "kyuubi version e.g v1.10.2", defaultValue = "master")
    String kyuubiVersion;

    @CommandLine.Option(names = {"-f", "--format"}, description = "format", defaultValue = "json")
    String format;

    @Override
    public Integer call() throws Exception {
        String url = String.format("https://kyuubi.readthedocs.io/en/%s/configuration/settings.html", kyuubiVersion);
        List<KyuubiConfigSection> result = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(url).get();
            Element section = doc.selectFirst("section#kyuubi-configurations");
            if (section != null) {
                Elements subSections = section.children().select("section");
                for (Element subSection : subSections) {
                    String subSectionId = subSection.attr("id");
                    List<Map<String, String>> tableRows = new ArrayList<>();
                    Elements tables = subSection.select("table");
                    for (Element table : tables) {
                        Element thead = table.selectFirst("thead");
                        Elements ths = thead.select("th");
                        List<String> columnNames = new ArrayList<>();
                        for (Element th : ths) {
                            columnNames.add(th.text().trim());
                        }
                        Elements rows = table.select("tbody tr");
                        for (Element row : rows) {
                            Elements cols = row.select("td");
                            Map<String, String> rowMap = new LinkedHashMap<>();
                            for (int i = 0; i < cols.size(); i++) {
                                rowMap.put(columnNames.get(i), cols.get(i).text().trim());
                            }
                            tableRows.add(rowMap);
                        }
                    }
                    KyuubiConfigSection line = new KyuubiConfigSection(subSectionId, tableRows);
                    result.add(line);
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
                        System.out.println(c.get("Key") + "=" + c.get("Default"));
                    });
                }
            }


            return 0;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return 100;
        }
    }
}
