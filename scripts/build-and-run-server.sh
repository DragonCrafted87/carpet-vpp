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

sleep 1

./gradlew runDatagen

if [ $? -ne 0 ]; then
    echo "Datagen failed. Exiting."
    exit 1
fi

sleep 1

# Launch the server
(set -x; ./gradlew runServer)
