#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "${BASH_SOURCE[0]}")/.."
chmod +x ./gradlew
./gradlew --no-daemon configureLaunch downloadAssets

BASHRC="${PWD}/.devcontainer/bashrc"
if [[ -f "${BASHRC}" ]] && ! grep -Fqs '.devcontainer/bashrc' "${HOME}/.bashrc" 2>/dev/null; then
    printf '\n# carpet-vpp helpers\nsource "%s"\n' "${BASHRC}" >>"${HOME}/.bashrc"
fi

echo "devcontainer ready: java $(${JAVA_HOME}/bin/java -version 2>&1 | head -1)"
echo "helpers: source .devcontainer/bashrc  (vpp-help)"
