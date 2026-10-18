# Development runtime

These requirements describe the development launch and the release jar
as implemented for Vanilla Plus Plus (`vpp`). They record the current
behavior.

The key words SHALL, SHALL NOT, RECOMMENDED, NOT RECOMMENDED, and
OPTIONAL in this document are to be interpreted as described in
RFC 2119.

## Dev game

Loom client and dedicated-server runs SHALL pass `-Dvpp.debug=true` to
the Minecraft JVM.

The Loom client SHALL pass `--username Dev` once. It SHALL NOT pass
`--uuid`. Minecraft then derives the offline id from that name.
Passing `--username` again on the Gradle command line is NOT
RECOMMENDED. The game rejects a duplicated option.

Bag equip, unequip, swap, and container-write logs SHALL be emitted
only when the Minecraft JVM property `vpp.debug` is true. Beacon and
stew debug logs stay compiled off, as their own documents say.

In a Fabric development environment, a player who joins the dedicated
server SHALL be added to ops at owner level, and SHALL be granted every
recipe the server has loaded. A server that is not a development
environment SHALL NOT do either.

## Server properties

`setupRunServer` SHALL create `run/` when it is missing. When
`run/eula.txt` is missing, it SHALL write `eula=true`. It SHALL NOT
rewrite an existing EULA file.

When `run/server.properties` is missing, `setupRunServer` SHALL copy
`.devcontainer/server.properties` there. That template sets
`enable-command-block=true` and `online-mode=false`. When the
destination already exists, the task SHALL NOT overwrite it. Editing
the copied file is OPTIONAL. Later launches SHALL leave those edits in
place.

`runServer` and the devcontainer post-create step SHALL run this setup.
The post-create step SHALL also run `configureLaunch` and
`downloadAssets`.

## Combined launch

`vpp-client` and `vpp-server` remain separate launches. Using them
instead of the combined launch is OPTIONAL.

Running the client and the dedicated server together is RECOMMENDED
through `vpp-play`. That helper SHALL start the dedicated server, then
the client joined to it, and SHALL stop the server when the client
exits.

`vpp-play` SHALL prepare with one Gradle invocation:
`compileJava`, `compileClientJava`, `setupRunServer`, `downloadAssets`,
and `configureLaunch`. It SHALL NOT start `runServer` and `runClient`
together. Those tasks share the Gradle project lock, and stopping the
wrapper does not stop a server JVM the daemon owns.

After that preparation, the server JVM SHALL be
`net.fabricmc.devlaunchinjector.Main` with the server Knot main, `nogui`,
and `-Dvpp.debug=true`, using the Loom argument file and `launch.cfg`.
Its working directory SHALL be `run/`. Its output SHALL go to
`run/logs/vpp-server.log`.

The client JVM SHALL use the client Knot main, `-Dvpp.debug=true`,
`--username Dev`, and `--quickPlayMultiplayer`. When the server port is
25565, that address SHALL be `localhost`. Otherwise it SHALL be
`localhost` and the port. Asset arguments stay in `launch.cfg`. The
client command SHALL NOT repeat them.

The server port SHALL be the `server-port` value in
`run/server.properties` when that line is present, and 25565 otherwise.
When that port is already open, `vpp-play` SHALL NOT start a server.
When the server process exits before the port opens, or the port is
still closed after 180 seconds, `vpp-play` SHALL stop and SHALL name
the server log.

The server process SHALL be stopped on client exit, on interrupt, and
on terminate. The stop SHALL send TERM, then KILL if the process is
still alive after 5 seconds. A second stop of the same launch SHALL
NOT send that signal again.

When `setsid` is available, the server JVM SHALL be started with it so
a terminal interrupt reaches the client and the helper stops the server
by the child pid.

When neither `DISPLAY` nor `WAYLAND_DISPLAY` is set, the client launch
SHALL start Xvfb on display `:99` before the game. When a display is
already set, it SHALL leave that display in place.

## Release jar

Datagen SHALL write `build/generated/resources` and SHALL NOT add that
directory as a `processResources` input. Dev runs other than
`runDatagen` SHALL depend on `runDatagen` and SHALL put that directory
on the classpath.

The mod jar SHALL depend on `runDatagen` and SHALL include that
directory, excluding `.cache`. Writing datagen output into
`build/resources/main` is NOT RECOMMENDED. Gradle 9 treats that as an
implicit dependency of `processResources`.

`./gradlew build` SHALL fail when the Gradle JVM property `vpp.debug`
is true or the project property `vpp.debug` is `true`. The Minecraft
`-Dvpp.debug=true` on a dev run SHALL NOT by itself fail that guard.
The GitHub build step SHALL run `./gradlew build` with those properties
unset. There is no Modrinth publish workflow in this repository.

## Version on a branch

On a branch whose short name is not the default branch, a commit SHALL
stage a `mod_version` different from the default branch's
`gradle.properties`. The hook SHALL reject a staged value that matches.
The default branch SHALL skip that check. This branch's version is
`2.0.0`.
