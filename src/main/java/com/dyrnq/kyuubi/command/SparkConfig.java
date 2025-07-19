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

@CommandLine.Command(name = "spark", aliases = {"s"}, description = "spark")

@Slf4j
public class SparkConfig extends CommonOptions implements Callable<Integer> {
    @CommandLine.Option(names = {"-n", "--no-section"}, description = "")
    boolean noSection;

    @CommandLine.Option(names = {"-sv", "--spark-version"}, description = "spark version e.g. 3.5.4", defaultValue = "latest")
    String sparkVersion;

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
                        "https://spark.apache.org/docs/%s/configuration.html",
                        "https://spark.apache.org/docs/%s/security.html",
                        "https://spark.apache.org/docs/%s/monitoring.html",
                        "https://spark.apache.org/docs/%s/running-on-yarn.html",
                        "https://spark.apache.org/docs/%s/running-on-kubernetes.html",
                        "https://spark.apache.org/docs/%s/spark-standalone.html"

                }
        ) {
            try {
                if (!Strings.CI.equals("latest", sparkVersion)) {
                    url = Strings.CS.replace(url, "spark.apache.org", "archive.apache.org/dist/spark");
                }
                Connection connection = Jsoup.connect(String.format(url, sparkVersion));
                Document doc = connection.timeout(10 * 1000).get();
                Elements tables = doc.select("table");


                for (Element table : tables) {

                    if (!(table.html().contains("Property Name") || table.html().contains("System property"))) {
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
                String keyOfKey = null;
                String keyOfValue = null;

                if (c.containsKey("Property Name")) {
                    keyOfKey = "Property Name";
                }

                if (c.containsKey("System property")) {
                    keyOfKey = "System property";
                }

                if (c.containsKey("Default Value")) {
                    keyOfValue = "Default Value";
                }

                if (c.containsKey("Default")) {
                    keyOfValue = "Default";
                }


                System.out.println(c.get(keyOfKey) + "=" + c.get(keyOfValue));
            });
        }

        return 0;
    }
}
