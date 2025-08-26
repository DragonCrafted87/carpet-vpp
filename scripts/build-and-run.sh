#!/bin/bash

# Clear the console
clear

# Build the JAR
./gradlew build

if [ $? -ne 0 ]; then
    echo "Build failed. Exiting."
    exit 1
fi

# Copy the JAR, overwriting if exists
cp -f build/libs/minecraft-vpp-1.0.0.jar "D:/Games/MultiMC/instances/test/.minecraft/mods/"

# Launch the client as a background process
start "" "D:/Games/MultiMC/MultiMC.exe" -l "test" >/dev/null 2>&1 &

# Launch the server
./gradlew runServer
