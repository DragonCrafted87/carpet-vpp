package com.example;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TemplateModServer implements DedicatedServerModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("template-mod");

    @Override
    public void onInitializeServer() {
        LOGGER.info("DedicatedServerModInitializer");

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            Server.onServerLoaded(server);
        });
    }
}
