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

@CommandLine.Command(name = "bookkeeper", aliases = {"bk"}, description = "BookKeeper")

@Slf4j
public class BookKeeperConfig extends CommonOptions implements Callable<Integer> {
    @CommandLine.Option(names = {"-n", "--no-section"}, description = "")
    boolean noSection;

    @CommandLine.Option(names = {"-ver", "-av", "--bookKeeper-version"}, description = "bookKeeper version e.g. 4.16.7", defaultValue = "next")
    String bookKeeperVersion;

    @CommandLine.Option(names = {"-f", "--format"}, description = "format", defaultValue = "json")
    String format;

    @Override
    public Integer call() throws Exception {
//        List<Map<String, String>> noSectionList = new LinkedList<>();
        List<KyuubiConfigSection> result = new ArrayList<>();

        for (String url :
                new String[]{
                        "https://bookkeeper.apache.org/docs/%s/reference/config",

                }
        ) {
            try {


                Connection connection = Jsoup.connect(String.format(url, bookKeeperVersion));
                Document doc = connection.timeout(2 * 60 * 1000).get();

                Elements title = doc.select("h2");


                title.forEach(c -> {
                    List<Map<String, String>> subSectionList = new LinkedList<>();

                    Element next = c.nextElementSibling();
                    while (next != null) {
                        if ("table".equalsIgnoreCase(next.tagName())) {
                            break;
                        }
                        next = next.nextElementSibling();
                    }


                    if (next != null) {
                        Element thead = next.selectFirst("thead");
                        Elements ths = thead.select("th");
                        List<String> columnNames = new ArrayList<>();
                        for (Element th : ths) {
                            columnNames.add(th.text().trim());
                        }
                        Elements rows = next.select("tbody tr");
                        for (Element row : rows) {
                            Elements cols = row.select("td");
                            Map<String, String> rowMap = new LinkedHashMap<>();
                            for (int i = 0; i < cols.size(); i++) {
                                rowMap.put(columnNames.get(i), cols.get(i).text().trim());
                            }
                            subSectionList.add(rowMap);
                        }
                    } else {
                        log.warn("no table ");
                    }
                    result.add(new KyuubiConfigSection(c.text(), subSectionList));
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
                    System.out.println(c.get("Parameter") + "=" + c.get("Default"));
                });
            }

        }

        return 0;
    }
}

