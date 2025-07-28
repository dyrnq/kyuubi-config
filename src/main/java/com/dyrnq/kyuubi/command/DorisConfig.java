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
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "doris", aliases = {"d"}, description = "doris")

@Slf4j
public class DorisConfig extends CommonOptions implements Callable<Integer> {

    @CommandLine.Option(names = {"-n", "--no-section"}, description = "")
    boolean noSection;

    @CommandLine.Option(names = {"-ver", "-dv", "--doris-version"}, description = "doris version e.g. 3.0", defaultValue = "latest")
    String dorisVersion;

    @CommandLine.Option(names = {"-f", "--format"}, description = "format", defaultValue = "json")
    String format;

    @CommandLine.Option(names = {"-t", "--type"}, description = "fe", defaultValue = "fe")
    String type;

    @Override
    public Integer call() throws Exception {


//        List<KyuubiConfigSection> result = new ArrayList<>();
        List<Map<String, String>> noSectionList = new LinkedList<>();
        for (String url :
                new String[]{
                        "https://doris.apache.org/docs/VERSION/admin-manual/config/%s-config",

                }
        ) {
            try {
                if (Strings.CI.equals("latest", dorisVersion)) {
                    url = url.replaceFirst("/VERSION", "/");
                } else {
                    url = url.replaceFirst("/VERSION", "/" + dorisVersion);
                }
                url = String.format(url, type);

                Connection connection = Jsoup.connect(url);
                Document doc = connection.timeout(2 * 60 * 1000).get();

                Elements title = doc.select("h4");


                title.forEach(c -> {
                    Element defaultElement = null;

                    {
                        Element next = c.nextElementSibling();
                        int count = 0;
                        while (next != null && count <= 1) {
                            String nextText = next.text();
                            String tagName = next.tagName();
                            if ("ul".equalsIgnoreCase(tagName)) {
                                Elements lis = next.select("li");
                                for (Element x : lis) {
                                    String liText = x.text().trim();
                                    if (
                                            liText.startsWith("Default:") || liText.startsWith("Default：") || liText.startsWith("Default value:") || liText.startsWith("Default value：")
                                    ) {
                                        defaultElement = x;
                                        break;
                                    }
                                }
                            }

                            // 如果找到了默认元素，直接跳出循环
                            if (defaultElement != null) {
                                break;
                            }

                            // 无论是否是 ul 标签，都递增计数器
                            next = next.nextElementSibling();
                            count++;

                            log.info("count={}", count);
                        }
                    }
                    if (defaultElement == null) {
                        int count = 0;
                        Element next = c.nextElementSibling();
                        while (next != null && count <= 5) {
                            String nextText = next.text().trim();
                            String tagName = next.tagName();
                            if (("p".equalsIgnoreCase(tagName)) && (
                                    nextText.startsWith("Default:") || nextText.startsWith("Default：") || nextText.startsWith("Default value:") || nextText.startsWith("Default value：")
                            )) {
                                defaultElement = next;
                                break;
                            }
                            next = next.nextElementSibling();
                            count++;
                        }
                    }


                    if (defaultElement != null) {
                        Map<String, String> rowMap = new LinkedHashMap<>();
                        String key = c.text();
                        rowMap.put("Key", key);
                        String val = defaultElement.text();
                        val = val.replace("Default:", "");
                        val = val.replace("Default value:", "");
                        val = val.replace("Default：", "");
                        val = val.replace("Default value：", "");

                        if (Strings.CI.equals("plugin_dir", key)) {
                            if (Strings.CS.contains(val, "\"")
                                && !Strings.CS.endsWith(val, "\"")
                            ) {
                                val = val + "\"";
                            }

                        }
                        val = StringUtils.trim(val);

                        if (val != null && val.endsWith(";")) {
                            val = val.substring(0, val.length() - 1);
                        }

                        rowMap.put("Default", val);


                        if (Strings.CI.equals("min_clone_task_timeout_sec And max_clone_task_timeout_sec", key)) {
                            Map<String, String> rowMap_t1 = new LinkedHashMap<>();
                            Map<String, String> rowMap_t2 = new LinkedHashMap<>();
                            rowMap_t1.put("Key", "min_clone_task_timeout_sec");
                            rowMap_t1.put("Default", val);
                            rowMap_t2.put("Key", "max_clone_task_timeout_sec");
                            rowMap_t2.put("Default", val);

                            noSectionList.add(rowMap_t1);
                            noSectionList.add(rowMap_t2);

                        } else {
                            noSectionList.add(rowMap);
                        }
                    } else {
                        log.warn("no table ");
                        Map<String, String> rowMap = new LinkedHashMap<>();
                        rowMap.put("Key", c.text());
                        rowMap.put("Default", "None");
                        noSectionList.add(rowMap);
                    }
                });
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
