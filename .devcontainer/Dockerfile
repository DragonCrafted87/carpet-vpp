# OpenMandriva Lx Rock, the stable release the desktop runs.
# Rock's packaged JDK stops at 24, so this image unpacks Temurin 25
# for Minecraft 26.3. The wrapper in the repo is the Gradle that builds.
FROM openmandriva/minimal:rock

# Names are the Rock packages, matching the desktop: lib64*-devel,
# rootcerts, pkgconf, shadow.
# fonts-ttf-dejavu: an empty fontconfig cache breaks the client.
# OM does not put java on PATH by default.
# lib64stdc++6 exports condition_variable::wait@GLIBCXX_3.4.11, which
# vscode-server's Node loads.
# Rock ships user omv at uid 1001. updateRemoteUserUID will not move
# dev onto a uid that already exists, so the container user would not
# match the host and could not write the workspace.
# userdel -r exits 12: /home/omv is owned by root, and there is no mail spool.
ENV JAVA_HOME=/usr/lib/jvm/java-25-temurin

RUN dnf --refresh --assumeyes --nodocs --setopt=install_weak_deps=False upgrade \
    && dnf --assumeyes --nodocs --setopt=install_weak_deps=False install \
        lib64SDL2_2.0_1 \
        alsa-lib \
        binutils \
        rootcerts \
        curl \
        fontconfig \
        fonts-ttf-dejavu \
        git \
        gzip \
        lib64stdc++6 \
        mesa \
        procps-ng \
        pulseaudio \
        shadow \
        sudo \
        tar \
        unzip \
        vulkan-loader \
        which \
        x11-server-xvfb \
    && fc-cache -f \
    && curl -fsSL -o /tmp/jdk.tar.gz \
        "https://api.adoptium.net/v3/binary/latest/25/ga/linux/x64/jdk/hotspot/normal/eclipse?project=jdk" \
    && mkdir -p /usr/lib/jvm \
    && tar -xzf /tmp/jdk.tar.gz -C /usr/lib/jvm \
    && jdkdir="$(find /usr/lib/jvm -mindepth 1 -maxdepth 1 -type d -name 'jdk-25*' | head -1)" \
    && ln -sfn "${jdkdir}" /usr/lib/jvm/java-25-temurin \
    && rm -f /tmp/jdk.tar.gz \
    && useradd -m -s /bin/bash -u 1000 dev \
    && echo 'dev ALL=(ALL) NOPASSWD: ALL' > /etc/sudoers.d/dev \
    && chmod 0440 /etc/sudoers.d/dev \
    && dnf clean all \
    && rm -rf /var/cache/dnf/*

RUN userdel omv

ENV PATH="${JAVA_HOME}/bin:${PATH}"

USER dev
WORKDIR /workspaces/carpet-vpp
