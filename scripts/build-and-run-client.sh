#!/bin/bash

# Clear the console
clear

# Build the JAR
(set -x; ./gradlew build --warning-mode fail)

if [ $? -ne 0 ]; then
    echo "Build failed. Exiting."
    exit 1
fi

sleep 1

# Launch the server
(set -x; ./gradlew runClient)
