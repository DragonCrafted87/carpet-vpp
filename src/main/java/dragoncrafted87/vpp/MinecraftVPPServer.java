package dragoncrafted87.vpp;

import com.mojang.authlib.GameProfile;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.permissions.PermissionLevel;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.ServerOpListEntry;
import net.minecraft.world.item.crafting.RecipeHolder;

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
        ServerLifecycleEvents.SERVER_STARTED.register(
                server -> {
                    onServerLoaded(server);
                });
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            ServerPlayConnectionEvents.JOIN.register(
                    (handler, sender, server) -> {
                        ServerPlayer player = handler.player;
                        GameProfile profile = player.getGameProfile();
                        server.getPlayerList()
                                .getOps()
                                .add(
                                        new ServerOpListEntry(
                                                new NameAndId(profile),
                                                LevelBasedPermissionSet.forLevel(
                                                        PermissionLevel.OWNERS),
                                                false));
                        try {
                            server.getPlayerList().getOps().save();
                        } catch (IOException e) {
                            LOGGER.error("Failed to save ops.json", e);
                        }
                        LOGGER.info("Opped {} in development server", player.getName().getString());
                        Collection<RecipeHolder<?>> recipes =
                                server.getRecipeManager().getRecipes();
                        player.awardRecipes(recipes);
                        LOGGER.info(
                                "Unlocked all recipes for {} in development server",
                                player.getName().getString());
                    });
        }
    }

    private static void onServerLoaded(MinecraftServer ms) {
        minecraft_server = ms;
    }
}
