#!/bin/bash
set -euo pipefail
cd "$(dirname "$0")/.."
chmod +x ./gradlew
./gradlew --no-daemon configureLaunch downloadAssets
