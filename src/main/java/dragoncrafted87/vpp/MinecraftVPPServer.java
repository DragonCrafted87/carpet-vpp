package dragoncrafted87.vpp;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinecraftVPPServer implements DedicatedServerModInitializer {
    public static MinecraftServer minecraft_server;
    public static final Logger LOGGER = LoggerFactory.getLogger("MinecraftVPPServer");

    @Override
    public void onInitializeServer() {
        LOGGER.info("MinecraftVPPServer initialized successfully");
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            onServerLoaded(server);
        });
    }

    private static void onServerLoaded(MinecraftServer ms) {
        minecraft_server = ms;
    }
}
