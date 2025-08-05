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

@CommandLine.Command(name = "gravitino", aliases = {"gr"}, description = "Gravitino")

@Slf4j
public class GravitinoConfig extends CommonOptions implements Callable<Integer> {
    @CommandLine.Option(names = {"-n", "--no-section"}, description = "")
    boolean noSection;

    @CommandLine.Option(names = {"-ver", "-fv", "--gravitino-version"}, description = "gravitino version e.g. 0.9.1", defaultValue = "current")
    String gravitinoVersion;

    @CommandLine.Option(names = {"-f", "--format"}, description = "format", defaultValue = "json")
    String format;
    @CommandLine.Option(names = {"iceberg-rest"}, description = "icebergRest")
    boolean icebergRest;

    @Override
    public Integer call() throws Exception {
        List<Map<String, String>> noSectionList = new LinkedList<>();
        List<Map<String, String>> filtered = new LinkedList<>();

        for (String url :
                new String[]{
                        "https://gravitino.apache.org/docs/%s/gravitino-server-config",
                        "https://gravitino.apache.org/docs/%s/security/how-to-authenticate",
                        "https://gravitino.apache.org/docs/%s/security/how-to-use-cors",
                        "https://gravitino.apache.org/docs/%s/security/how-to-use-https",
                        "https://gravitino.apache.org/docs/%s/iceberg-rest-service"

                }
        ) {
            try {
                if (Strings.CI.equals("current", gravitinoVersion)) {
                    url = Strings.CS.replace(url, "%s/", "/");
                } else {
                    url = String.format(url, gravitinoVersion);
                }
                Connection connection = Jsoup.connect(url).followRedirects(true);
                Document doc = connection.timeout(10 * 1000).get();
                Elements tables = doc.select("table");


                for (Element table : tables) {

                    if (!(table.html().contains("Configuration item") && table.html().contains("Description"))) {
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


        noSectionList.forEach(c -> {
            String keyOfKey = "Configuration item";
            String keyOfValue = "Default value";

            if (icebergRest) {
                if (c.get(keyOfKey).startsWith("gravitino.iceberg-rest.")) {
                    filtered.add(c);
                }
            } else {
                if (!c.get(keyOfKey).startsWith("gravitino.iceberg-rest.")) {
                    if (
                            "package".equalsIgnoreCase(c.get(keyOfKey)) ||
                            "cloud.name".equalsIgnoreCase(c.get(keyOfKey)) ||
                            "cloud.region-code".equalsIgnoreCase(c.get(keyOfKey)) ||
                            c.get(keyOfKey).startsWith("spark.sql.catalog.") ||
                            "cloud.region-code".equalsIgnoreCase(c.get(keyOfKey))
                    ) {
                        //
                    } else {
                        filtered.add(c);
                    }
                }
                if ("gravitino.iceberg-rest.classpath".equalsIgnoreCase(c.get(keyOfKey))) {
                    filtered.add(c);
                }
            }

        });

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        if (Strings.CI.equals("json", format)) {
            System.out.println(gson.toJson(filtered));
        }
        if (Strings.CI.equals("conf", format)) {

            filtered.forEach(c -> {
                String keyOfKey = "Configuration item";
                String keyOfValue = "Default value";


                System.out.println(c.get(keyOfKey) + "=" + c.get(keyOfValue));
            });
        }

        return 0;
    }
}
