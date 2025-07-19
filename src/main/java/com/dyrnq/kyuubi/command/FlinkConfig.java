package com.dyrnq.kyuubi.command;

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

@CommandLine.Command(name = "flink", aliases = {"fl"}, description = "flink")

@Slf4j
public class FlinkConfig extends CommonOptions implements Callable<Integer> {
    @CommandLine.Option(names = {"-n", "--no-section"}, description = "")
    boolean noSection;

    @CommandLine.Option(names = {"-ver", "-fv", "--flink-version"}, description = "flink version e.g. 1.20", defaultValue = "master")
    String flinkVersion;

    @CommandLine.Option(names = {"-f", "--format"}, description = "format", defaultValue = "json")
    String format;

    @Override
    public Integer call() throws Exception {
        List<Map<String, String>> noSectionList = new LinkedList<>();
        Map<String, String> replaceKey = new LinkedHashMap<>();
        replaceKey.put("System property", "Property Name");
        replaceKey.put("Default Value", "Default");

        for (String url :
                new String[]{
                        "https://nightlies.apache.org/flink/flink-docs-%s/docs/deployment/config/",

                }
        ) {
            try {
                if (!Strings.CI.equals("master", flinkVersion)) {
                    url = Strings.CS.replace(url, "flink-docs-", "flink-docs-release-");
                }
                Connection connection = Jsoup.connect(String.format(url, flinkVersion));
                Document doc = connection.timeout(10 * 1000).get();
                Elements tables = doc.select("table");


                for (Element table : tables) {

                    if (!(table.html().contains("Key") && table.html().contains("Default")
                          && table.html().contains("Description")
                    )) {
                        continue;
                    }

                    Element thead = table.selectFirst("thead");
                    List<String> columnNames = new ArrayList<>();
                    Elements ths = null;
                    if (thead != null) {
                        ths = thead.select("th");
                    } else {
                        ths = table.selectFirst("tr").select("th");
                    }
                    boolean isConfig = true;
                    for (Element th : ths) {
                        String name = th.text().trim();
                        columnNames.add(name);
                        if (Strings.CI.equals("Command", name)) {
                            isConfig = false;
                            break;
                        }
                    }
                    if (!isConfig) {
                        continue;
                    }

                    Elements rows = table.select("tbody tr");
                    for (Element row : rows) {
                        Elements cols = row.select("td");
                        Map<String, String> rowMap = new LinkedHashMap<>();
                        for (int i = 0; i < cols.size(); i++) {
                            rowMap.put(columnNames.get(i), cols.get(i).text().trim());
                        }

                        replaceKey.forEach((key, value) -> {
                            if (rowMap.containsKey(key)) {
                                rowMap.put(value, rowMap.get(key));
                                rowMap.remove(key);
                            }
                        });

                        if (!rowMap.isEmpty()) {
                            noSectionList.add(rowMap);
                        }
                    }


                }

            } catch (IOException e) {
                log.error(e.getMessage(), e);
                return 100;
            }
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        if (Strings.CI.equals("json", format)) {
            System.out.println(gson.toJson(noSectionList));
        }
        if (Strings.CI.equals("conf", format)) {

            noSectionList.forEach(c -> {
                String keyOfKey = "Key";
                String keyOfValue = "Default";


                System.out.println(c.get(keyOfKey) + "=" + c.get(keyOfValue));
            });
        }

        return 0;
    }
}
