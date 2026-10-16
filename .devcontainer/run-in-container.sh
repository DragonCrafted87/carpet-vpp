#!/usr/bin/env bash
# Run a command in this repo's dev container.
# From the host:
#   ./.devcontainer/run-in-container.sh ./gradlew test
# With no arguments, open a shell in the container.
set -euo pipefail

here="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
repo="$(cd "${here}/.." && pwd)"

die() {
    printf 'error: %s\n' "$*" >&2
    exit 1
}

need_cli() {
    if command -v devcontainer >/dev/null 2>&1; then
        return 0
    fi
    command -v npx >/dev/null 2>&1 || die "devcontainer or npx is required"
    command -v docker >/dev/null 2>&1 || die "docker is required"
}

devcontainer_cli() {
    if command -v devcontainer >/dev/null 2>&1; then
        devcontainer "$@"
    else
        npx --yes @devcontainers/cli "$@"
    fi
}

need_cli
devcontainer_cli up --workspace-folder "$repo" >/dev/null

if [[ "$#" -eq 0 ]]; then
    exec devcontainer_cli exec --workspace-folder "$repo" bash
fi

exec devcontainer_cli exec --workspace-folder "$repo" "$@"
