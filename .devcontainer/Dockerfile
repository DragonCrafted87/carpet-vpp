FROM eclipse-temurin:25-jdk

RUN apt-get update && apt-get install -y --no-install-recommends \
        ca-certificates \
        git \
        libasound2 \
        libgl1 \
        libgl1-mesa-dri \
        mesa-utils \
        mesa-vulkan-drivers \
        libpulse0 \
        libsdl2-2.0-0 \
        libxcursor1 \
        libxi6 \
        libxrandr2 \
        libxtst6 \
        xvfb \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /workspace
