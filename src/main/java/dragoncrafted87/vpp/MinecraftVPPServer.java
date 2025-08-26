package dragoncrafted87.vpp;

import com.mojang.authlib.GameProfile;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.recipe.Recipe;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.OperatorEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;

public class MinecraftVPPServer implements DedicatedServerModInitializer {
    public static MinecraftServer minecraft_server;
    public static final Logger LOGGER = LoggerFactory.getLogger("MinecraftVPPServer");
    @Override
    public void onInitializeServer() {
        LOGGER.info("MinecraftVPPServer initialized successfully");
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            onServerLoaded(server);
        });
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
                GameProfile profile = handler.player.getGameProfile();
                server.getPlayerManager().getOpList().add(new OperatorEntry(profile, 4, false));
                try {
                    server.getPlayerManager().getOpList().save();
                } catch (IOException e) {
                    LOGGER.error("Failed to save ops.json", e);
                }
                LOGGER.info("Opped {} in development server", handler.player.getName().getString());
                Collection<Recipe<?>> recipes = server.getRecipeManager().values();
                handler.player.unlockRecipes(recipes);
                LOGGER.info("Unlocked all recipes for {} in development server", handler.player.getName().getString());
            });
        }
    }
    private static void onServerLoaded(MinecraftServer ms) {
        minecraft_server = ms;
    }
}
