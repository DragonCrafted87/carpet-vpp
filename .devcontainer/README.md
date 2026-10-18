# Dev container

The image is OpenMandriva Rock with Temurin 25. Rock's own JDK packages
stop at 24, and this mod needs Java 25. Gradle is the wrapper in the
repo. The container user is `dev`. Opening the folder with **Dev
Containers: Reopen in Container** runs `.devcontainer/post-create.sh`,
which generates the Loom launch config, downloads Minecraft assets, and
sources `.devcontainer/bashrc` from `~/.bashrc`. Closing the VS Code
window stops the container (`shutdownAction`).

Source the helpers on the host or inside the container:

```bash
source .devcontainer/bashrc
vpp-help
```

Host commands start the container when it is not already up, then run
inside it:

```bash
vpp-test
vpp-game-test
vpp-client-test
vpp-server
vpp-client
vpp-play
vpp-exec ./gradlew build
vpp-down
```

`vpp-client` and `vpp-client-test` use the host Wayland socket when
`WAYLAND_DISPLAY` is set. With no display they start Xvfb on `:99`.
`vpp-play` starts the dedicated server, then the client joined to it,
and stops the server when the client exits. An interactive host shell
traps exit and hangup: the last such shell usually runs `vpp-down`.

Inside the container, `.vscode/launch.json` still has separate
**Minecraft Client** and **Minecraft Server** launches, plus datagen.
