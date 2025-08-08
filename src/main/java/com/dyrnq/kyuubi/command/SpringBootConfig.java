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

@CommandLine.Command(name = "spring-boot", aliases = {"sb"}, description = "spring-boot")

@Slf4j
public class SpringBootConfig extends CommonOptions implements Callable<Integer> {
    @CommandLine.Option(names = {"-n", "--no-section"}, description = "")
    boolean noSection;

    @CommandLine.Option(names = {"-ver", "-sv", "--spring-boot-version"}, description = "spring-boot version e.g. 3.4", defaultValue = "current")
    String springBootVersion;

    @CommandLine.Option(names = {"-f", "--format"}, description = "format", defaultValue = "json")
    String format;


    @Override
    public Integer call() throws Exception {
//        List<Map<String, String>> noSectionList = new LinkedList<>();
        List<KyuubiConfigSection> result = new ArrayList<>();
        // https://docs.spring.io/spring-boot/3.4/appendix/application-properties/index.html
        for (String url :
                new String[]{
                        "https://docs.spring.io/spring-boot/%s/appendix/application-properties/index.html",

                }
        ) {
            try {
                url = String.format(url, springBootVersion);
                if (Strings.CI.equals("current", springBootVersion)) {
                    url = Strings.CS.replace(url, "current/", "");
                }
                log.info("url={}", url);

                Connection connection = Jsoup.connect(url);
                Document doc = connection.timeout(2 * 60 * 1000).get();


                Elements sects = doc.select("div.sect1");

                for (Element c : sects) {
                    Element table = c.selectFirst("table");
                    Element h2 = c.selectFirst("h2");
                    if (table == null || h2 == null) {
                        log.info(c.html());
                        continue;
                    }
                    //log.info(c.html());
                    String title = h2.text();
                    List<Map<String, String>> subSectionList = new LinkedList<>();


                    Element thead = table.selectFirst("thead");
                    List<String> columnNames = new ArrayList<>();
                    Elements ths = null;
                    if (thead != null) {
                        ths = thead.select("th");
                    } else {
                        ths = table.selectFirst("tr").select("th");
                    }
                    for (Element th : ths) {
                        String name = th.text().trim();
                        columnNames.add(name);
                    }


                    Elements rows = table.select("tbody tr");
                    for (Element row : rows) {
                        Elements cols = row.select("td");
                        Map<String, String> rowMap = new LinkedHashMap<>();
                        for (int i = 0; i < cols.size(); i++) {
                            rowMap.put(columnNames.get(i), cols.get(i).text().trim());
                        }


                        if (!rowMap.isEmpty()) {
                            subSectionList.add(rowMap);
                        }
                    }


                    KyuubiConfigSection kyuubiConfigSection = new KyuubiConfigSection();

                    kyuubiConfigSection.setName(title);
                    kyuubiConfigSection.setList(subSectionList);
                    result.add(kyuubiConfigSection);
                }


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
                    System.out.println(c.get("Name") + "=" + c.get("Default Value"));
                });
            }

        }

        return 0;
    }
}
