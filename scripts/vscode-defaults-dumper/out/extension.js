// VS Code extension that dumps default settings to a file and quits.
// Activates on "*" (always at startup) when DUMP_OUTPUT env var is set.
const fs = require('fs');
const path = require('path');
const vscode = require('vscode');

async function dumpDefaults() {
  const outFile = process.env.DUMP_OUTPUT;
  if (!outFile) return;
  try {
    // Virtual default-settings document URI registered by
    // vs/workbench/services/preferences/common/preferences.ts:
    //   scheme    = vscode
    //   authority = "defaultsettings"
    //   path      = "/defaultSettings.json"
    const candidates = [
      'vscode://defaultsettings/defaultSettings.json',
      'vscode://defaultsettings/settings.json',
    ];
    let text = null;
    let lastErr = null;
    for (const u of candidates) {
      try {
        const doc = await vscode.workspace.openTextDocument(vscode.Uri.parse(u));
        text = doc.getText();
        if (text && text.length > 0) {
          fs.writeFileSync(outFile + '.uri', u, 'utf8');
          break;
        }
      } catch (e) {
        lastErr = e;
      }
    }
    if (text == null) {
      throw lastErr || new Error('no URI candidate worked');
    }
    fs.mkdirSync(path.dirname(outFile), { recursive: true });
    fs.writeFileSync(outFile, text, 'utf8');
    fs.writeFileSync(outFile + '.done', '', 'utf8');
  } catch (err) {
    fs.writeFileSync(outFile + '.error', String(err && err.stack || err), 'utf8');
  }
  setTimeout(() => vscode.commands.executeCommand('workbench.action.quit'), 500);
}

function activate(context) {
  if (process.env.DUMP_OUTPUT) {
    dumpDefaults();
  }
}
exports.activate = activate;
