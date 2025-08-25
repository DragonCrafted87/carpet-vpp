package dragoncrafted87.vpp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dragoncrafted87.vpp.gui.BagTooltipComponent;
import dragoncrafted87.vpp.item.BagTooltipData;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;

public class MinecraftVPPClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("MinecraftVPPClient");

    @Override
    public void onInitializeClient() {
        LOGGER.info("MinecraftVPPClient initialized successfully");
        ClientPlayNetworking.registerGlobalReceiver(MinecraftVPPNetworking.ENABLE_SLOTS,
                (client, handler, packet, sender) -> {
                    client.execute(() -> {
                        if (client.player != null) {
                            InventoryUtility.updateBagSlots(client.player);
                        }
                    });
                });
        ClientPlayConnectionEvents.JOIN.register((netHandler, packetSender, client) -> {
            client.execute(() -> {
                if (client.player != null) {
                    InventoryUtility.updateBagSlots(client.player);
                    if (DebugFlags.DEBUG_BAG_EVENTS) {
                        LOGGER.info("Updated bag slots on client join");
                    }
                }
            });
        });
        TooltipComponentCallback.EVENT.register(data -> {
            if (data instanceof BagTooltipData d) {
                return new BagTooltipComponent(d);
            }
            return null;
        });
    }
}
