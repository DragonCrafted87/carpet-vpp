package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ClientModInitializer;

public class TemplateModClient implements ClientModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("template-mod");

    @Override
    public void onInitializeClient() {
        LOGGER.info("ClientModInitializer");
    }
}
