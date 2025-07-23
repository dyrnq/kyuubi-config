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

import java.util.*;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "amoro", aliases = {"am"}, description = "amoro")

@Slf4j
public class AmoroConfig extends CommonOptions implements Callable<Integer> {
    @CommandLine.Option(names = {"-n", "--no-section"}, description = "")
    boolean noSection;

    @CommandLine.Option(names = {"-ver", "-av", "--amoro-version"}, description = "amoro version e.g. 0.8.0", defaultValue = "latest")
    String amoroVersion;

    @CommandLine.Option(names = {"-f", "--format"}, description = "format", defaultValue = "json")
    String format;


    @Override
    public Integer call() throws Exception {
        List<Map<String, String>> noSectionList = new LinkedList<>();
//        List<KyuubiConfigSection> result = new ArrayList<>();

        for (String url :
                new String[]{
                        "https://amoro.apache.org/docs/%s/configurations/",

                }
        ) {
            Connection connection = Jsoup.connect(String.format(url, amoroVersion));
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


                    if (!rowMap.isEmpty()) {
                        noSectionList.add(rowMap);
                    }
                }


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


