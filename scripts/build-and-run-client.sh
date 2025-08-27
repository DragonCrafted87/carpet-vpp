#!/bin/bash

set -x

# Clear the console
clear

# Build the JAR
./gradlew build --warning-mode fail

if [ $? -ne 0 ]; then
    echo "Build failed. Exiting."
    exit 1
fi

sleep 1

# Launch the server
./gradlew runClient
