package com.dyrnq.kyuubi.command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

@CommandLine.Command(name = "debezium", aliases = {"de"}, description = "debezium")

@Slf4j
public class DebeziumConfig extends CommonOptions implements Callable<Integer> {

    @CommandLine.Option(names = {"-n", "--no-section"}, description = "")
    boolean noSection;

    @CommandLine.Option(names = {"-ver", "-dv", "--debezium-version"}, description = "debezium version e.g. 3.3", defaultValue = "stable")
    String debeziumVersion;

    @CommandLine.Option(names = {"-f", "--format"}, description = "format", defaultValue = "json")
    String format;

    @CommandLine.Option(names = {"-t", "--type"}, description = "mysql", defaultValue = "mysql")
    String type;

    @Override
    public Integer call() throws Exception {
        //https://debezium.io/documentation/reference/stable/connectors/mysql.html
        List<Map<String, String>> noSectionList = new LinkedList<>();
        Map<String, String> replaceKey = new LinkedHashMap<>();


        for (String url :
                new String[]{
                        "https://debezium.io/documentation/reference/%s/connectors/" + type + ".html",

                }
        ) {
            try {

                Connection connection = Jsoup.connect(String.format(url, debeziumVersion));
                Document doc = connection.timeout(60 * 1000).get();
                Elements tables = doc.select("div.dlist");


                for (Element table : tables) {

                    String id = table.id();
                    if (StringUtils.isBlank(id) || !id.startsWith(type + "-property-")) {
                        continue;
                    }
                    Element dl = table.selectFirst("dl");
                    if (dl == null) {
                        continue;
                    }
                    Element dt = dl.selectFirst("dt");
                    Element dd = dl.selectFirst("dd");
                    if (dt == null) {
                        continue;
                    }
                    String key = dt.text();
                    Map<String, String> rowMap = new LinkedHashMap<>();
                    rowMap.put("Key", key);
                    if (dd != null) {
                        Element dl_ = dd.selectFirst("dl");
                        Elements dt_ = dl_.select("dt");
                        Elements dd_ = dl_.select("dd");

                        for (int i = 0; i < dt_.size(); i++) {
                            Element dt__ = dt_.get(i);
                            Element dd__ = dd_.get(i);
                            rowMap.put(dt__.text(), dd__.text());
                        }
                    }
                    noSectionList.add(rowMap);

                }

                Elements tables_ = doc.select("table");

                for (Element table_ : tables_) {
                    Element e = table_.selectFirst(">thead");
                    if (e != null && (e.text().contains("Property") && e.text().contains("Default") && e.text().contains("Description"))) {
                        List<String> columnNames = new ArrayList<>();

                        Elements ths = e.select("tr th");


                        for (Element th : ths) {
                            String name = th.text().trim();
                            if (Strings.CI.equals(name, "Property")) {
                                columnNames.add("Key");
                            } else if (Strings.CI.equals(name, "Default")) {
                                columnNames.add("Default value");
                            } else {
                                columnNames.add(name);
                            }
                        }

                        Elements rows = table_.select(">tbody>tr");

                        for (Element row : rows) {
                            Map<String, String> rowMap = new LinkedHashMap<>();
                            Elements cols = row.select(">td");

                            for (int i = 0; i < cols.size(); i++) {
                                rowMap.put(columnNames.get(i), cols.get(i).text().trim());
                            }
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
                String keyOfValue = "Default value";


                System.out.println(c.get(keyOfKey) + "=" + c.get(keyOfValue));
            });
        }


        return 0;
    }
}
