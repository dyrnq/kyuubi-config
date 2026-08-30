#!/usr/bin/env bash
# Dump VS Code's default settings to a JSON5 file by loading a tiny local
# extension that opens the virtual vscode://defaultsettings/settings.json
# document. The document is generated at runtime by VS Code's own
# configurationRegistry from every registered configuration across all
# built-in and installed extensions - so this is the authoritative source,
# not a community mirror.
#
# Requires a local VS Code install (`code` in PATH, or /usr/bin/code).
# On Linux, a display is required; set DISPLAY=:0 or run inside a session.
#
# Usage: dump.sh <output-json5-path>
set -e

OUT="${1:?usage: dump.sh <output-path>}"
DUMP_DIR="$(mktemp -d)"
EXT_DIR="$(cd "$(dirname "$0")" && pwd)"

cleanup() { rm -rf "$DUMP_DIR" "${OUT}.done" "${OUT}.error" "${OUT}.uri" 2>/dev/null || true; }
trap cleanup EXIT

if ! command -v code >/dev/null 2>&1; then
    echo "ERROR: 'code' not in PATH; install VS Code or set up the code CLI" >&2
    exit 1
fi

# Run VS Code with our tiny extension as a dev extension. When the extension
# activates (it sets DUMP_OUTPUT=<OUT> below), it opens the virtual default
# settings document and writes its content to <OUT>.
DUMP_OUTPUT="$OUT" \
    timeout 25 code \
        --no-sandbox \
        --user-data-dir "$DUMP_DIR/userdata" \
        --extensions-dir "$DUMP_DIR/extensions" \
        --extensionDevelopmentPath "$EXT_DIR" \
        > "$DUMP_DIR/code.out" 2>&1 || true

# Wait up to 20s for the dump to land (the extension writes <OUT>.done when
# it has finished copying the virtual document to disk).
for _ in $(seq 1 20); do
    [[ -f "${OUT}.done" ]] && break
    [[ -f "${OUT}.error" ]] && { cat "${OUT}.error" >&2; exit 1; }
    sleep 1
done

if [[ ! -f "${OUT}.done" ]]; then
    echo "ERROR: dump timed out; code output:" >&2
    tail -20 "$DUMP_DIR/code.out" >&2
    exit 1
fi

# Verify size: a valid dump is ~500KB (2000+ settings). Anything < 10KB is
# almost certainly an error or stub.
size=$(wc -c < "$OUT")
if [[ "$size" -lt 10240 ]]; then
    echo "ERROR: dump too small (${size} bytes); expected ~500KB" >&2
    head -20 "$OUT" >&2
    exit 1
fi

echo "dumped vscode defaults: ${size} bytes -> $OUT"
