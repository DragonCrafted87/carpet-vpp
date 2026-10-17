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

# Launch the client
(set -x; ./gradlew runClient)
