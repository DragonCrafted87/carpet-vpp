# Dev container

The container is Java 25 with Gradle, Xvfb, and Mesa, so unit tests, the dedicated server, and the client can run without a desktop GPU. Open the folder in VS Code and choose **Dev Containers: Reopen in Container**. The first start runs `.devcontainer/setup.sh`, which generates the Loom launch config and downloads Minecraft assets. Later starts run `.devcontainer/xvfb.sh`, which opens display `:99`.

Inside the container, the launch configs in `.vscode/launch.json` are separate:

- **Minecraft Client** starts the Fabric client.
- **Minecraft Server** starts the dedicated server with `nogui`.
- **Minecraft Datagen** writes generated data under `src/main/generated`.

The tasks in `.vscode/tasks.json` run `./gradlew test`, `./gradlew runGameTest`, and `./gradlew runClientGameTest`.

From the host, with Docker installed, the same container can run one command:

```bash
./.devcontainer/run-in-container.sh ./gradlew test
./.devcontainer/run-in-container.sh ./gradlew runGameTest
./.devcontainer/run-in-container.sh ./gradlew runServer
```

With no arguments, that script opens a shell in the container. It uses the `devcontainer` command when that is on `PATH`, and otherwise `npx @devcontainers/cli`. The first run builds the image from `.devcontainer/Dockerfile`.

`DragonCrafted87/prayers` on `develop` was not readable from this environment (GitHub returned 404), so this script follows the public devcontainer CLI (`up`, then `exec`) and was not checked against that repository.
