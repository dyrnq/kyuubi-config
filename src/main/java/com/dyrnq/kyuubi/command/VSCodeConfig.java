package com.dyrnq.kyuubi.command;

import com.dyrnq.kyuubi.dto.KyuubiConfigSection;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Strings;
import picocli.CommandLine;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CommandLine.Command(name = "vscode", aliases = {"vsc"}, description = "vscode")
@Slf4j
public class VSCodeConfig extends CommonOptions implements Callable<Integer> {
    @CommandLine.Option(names = {"-n", "--no-section"}, description = "")
    boolean noSection;

    @CommandLine.Option(names = {"-ver", "-vcv", "--vscode-version"}, description = "vscode version (informational, the docs page is versionless)", defaultValue = "current")
    String vscodeVersion;

    @CommandLine.Option(names = {"-f", "--format"}, description = "format", defaultValue = "json")
    String format;

    /**
     * Local source of truth: a JSON5 dump produced by the
     * vscode-defaults-dumper extension, which loads a tiny VS Code extension
     * that opens the virtual {@code vscode://defaultsettings/settings.json}
     * document and writes it to this path. This is the official source - the
     * document is generated at runtime by VS Code's own configurationRegistry
     * from every registered configuration across all built-in and installed
     * extensions. No public Microsoft-hosted equivalent exists; community
     * mirrors (tsinis gist, etc.) are derived from the same file.
     */
    @CommandLine.Option(names = {"-src", "--source"}, description = "path to the pre-dumped vscode defaults JSON5 (produced by the dumper extension)", defaultValue = "/tmp/vscode-defaults.json")
    String sourcePath;

    /**
     * Match a single JSON5 line comment that starts at column 0 (after
     * optional leading whitespace) with "//". Anything else is left alone,
     * including "//" sequences inside string values (e.g. URLs).
     */
    private static final Pattern LINE_COMMENT = Pattern.compile("^\\s*//.*$", Pattern.MULTILINE);

    /**
     * Match a setting line like {@code "editor.fontSize": 14,} and capture the
     * key plus the raw value. Used to associate each setting with its
     * preceding // description comments.
     */
    private static final Pattern SETTING_LINE = Pattern.compile(
            "^\\s*\"([^\"]+)\"\\s*:\\s*(.+?),?\\s*$", Pattern.MULTILINE);

    @Override
    public Integer call() throws Exception {
        Path src = Path.of(sourcePath);
        if (!Files.isRegularFile(src)) {
            log.error("vscode : dump file not found at {} - run usage-example.sh which dumps via the vscode-defaults-dumper extension first", src);
            return 100;
        }
        String body;
        try {
            body = Files.readString(src, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("vscode : failed to read {} ({})", src, e.getMessage());
            return 100;
        }

        // Step 1: extract per-setting descriptions by scanning the raw text
        // before stripping comments (so we know which // line(s) belong to
        // which setting).
        //
        // Multi-line values (objects/arrays that span multiple lines) break
        // naive line-by-line parsing, so we track bracket depth: while inside
        // a multi-line value, skip the lines and reset the pending comment
        // buffer (the comment immediately following the closing brace belongs
        // to the NEXT setting, not the multi-line one).
        LinkedHashMap<String, String> descByKey = new LinkedHashMap<>();
        StringBuilder pendingComments = new StringBuilder();
        int depth = 0;
        for (String rawLine : body.split("\n")) {
            String line = rawLine;
            // Strip trailing whitespace/CR for matching, but keep indent for
            // SETTING_LINE regex.
            String trimmed = line.trim();
            if (depth > 0) {
                // Inside a multi-line value - skip until bracket depth closes.
                for (int i = 0; i < line.length(); i++) {
                    char c = line.charAt(i);
                    if (c == '{' || c == '[') depth++;
                    else if (c == '}' || c == ']') depth--;
                }
                if (depth <= 0) {
                    depth = 0;
                    pendingComments.setLength(0);
                }
                continue;
            }
            if (LINE_COMMENT.matcher(line).matches()) {
                // strip the leading // and whitespace, then collect
                String text = line.replaceFirst("^\\s*//\\s*", "").trim();
                if (text.isEmpty()) continue;
                // Skip section headers: short single-line labels with no
                // period (e.g. "// Editor", "// Files", "// Extensions").
                // They describe the *category*, not the setting.
                if (text.length() < 25 && !text.endsWith(".") && !text.startsWith("-")) continue;
                if (pendingComments.length() > 0) pendingComments.append(' ');
                pendingComments.append(text);
                continue;
            }
            Matcher m = SETTING_LINE.matcher(line);
            if (m.find()) {
                descByKey.put(m.group(1), pendingComments.toString());
                pendingComments.setLength(0);
                // If the value on this line starts a multi-line object/array,
                // set depth so subsequent lines are skipped.
                String value = m.group(2).trim();
                if (value.equals("{") || value.equals("[")) {
                    depth = 1;
                }
            }
        }

        // Step 2: strip all // line comments and parse the rest as JSON.
        // The dump is an array of objects, one per contributor (each built-in
        // extension plus core). Flatten into a single map keyed by setting
        // id. Later objects override earlier ones on key collision (rare).
        String jsonBody = LINE_COMMENT.matcher(body).replaceAll("");
        JsonObject root = new JsonObject();
        try {
            JsonElement parsed = JsonParser.parseString(jsonBody);
            if (!parsed.isJsonArray()) {
                log.error("vscode : expected top-level array, got {}", parsed);
                return 100;
            }
            for (JsonElement el : parsed.getAsJsonArray()) {
                if (el.isJsonObject()) {
                    for (Map.Entry<String, JsonElement> entry : el.getAsJsonObject().entrySet()) {
                        root.add(entry.getKey(), entry.getValue());
                    }
                }
            }
        } catch (Exception e) {
            log.error("vscode : failed to parse JSON ({})", e.getMessage());
            return 100;
        }
        log.info("vscode : parsed {} settings from {}", root.size(), src);

        // Step 3: build sections grouped by top-level category (the bit before
        // the first '.' in the key, e.g. "editor.fontSize" -> "editor").
        Map<String, List<Map<String, String>>> byCategory = new TreeMap<>();
        for (Map.Entry<String, JsonElement> entry : root.entrySet()) {
            String key = entry.getKey();
            // skip per-language overrides like [typescript] scopes which have
            // a '[' in the key; those are language-default variants.
            if (key.startsWith("[") || key.contains("[")) continue;
            String category = key.contains(".") ? key.substring(0, key.indexOf('.')) : "(other)";
            String defaultStr = entry.getValue().toString();
            String desc = descByKey.getOrDefault(key, "");
            Map<String, String> row = new LinkedHashMap<>();
            row.put("Name", key);
            row.put("Default", defaultStr);
            row.put("Desc", desc);
            byCategory.computeIfAbsent(category, k -> new ArrayList<>()).add(row);
        }

        List<KyuubiConfigSection> result = new ArrayList<>();
        for (Map.Entry<String, List<Map<String, String>>> e : byCategory.entrySet()) {
            result.add(new KyuubiConfigSection(e.getKey(), e.getValue()));
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        if (Strings.CI.equals("json", format)) {
            if (noSection) {
                List<Map<String, String>> flat = new LinkedList<>();
                for (KyuubiConfigSection s : result) flat.addAll(s.getList());
                System.out.println(gson.toJson(flat));
            } else {
                System.out.println(gson.toJson(result));
            }
        }
        if (Strings.CI.equals("conf", format)) {
            for (KyuubiConfigSection s : result) {
                if (!noSection) {
                    System.out.println("# " + s.getName());
                }
                for (Map<String, String> row : s.getList()) {
                    // for scalar defaults emit "key=value"; for nested/array
                    // values emit "key=<json>" so they round-trip.
                    System.out.println(row.get("Name") + "=" + row.get("Default"));
                }
            }
        }

        return 0;
    }
}
