#!/bin/bash
# shellcheck disable=SC2181

# Clear the console
clear

# Build the JAR
(set -x; ./gradlew build --warning-mode fail)

if [ $? -ne 0 ]; then
    echo "Build failed. Exiting."
    exit 1
fi

# Copy the JAR, overwriting if exists
mod_version=$(grep -E '^mod_version=' gradle.properties | cut -d= -f2)
archives_base_name=$(grep -E '^archives_base_name=' gradle.properties | cut -d= -f2)
cp -f "build/libs/${archives_base_name}-${mod_version}.jar" "D:/Games/MultiMC/instances/test/.minecraft/mods/"

# Launch the client as a background process
start "" "D:/Games/MultiMC/MultiMC.exe" -l "test" >/dev/null 2>&1 &

sleep 1

# Launch the server
(set -x; ./gradlew runServer)
