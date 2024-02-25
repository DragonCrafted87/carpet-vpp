package com.example;

import net.minecraft.server.MinecraftServer;

public class Server {
    public static MinecraftServer minecraft_server;

    public static void onServerLoaded(MinecraftServer ms) {
        minecraft_server = ms;

    }
}
