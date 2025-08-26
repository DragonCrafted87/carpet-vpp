package dragoncrafted87.vpp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dragoncrafted87.vpp.gui.BagTooltipComponent;
import dragoncrafted87.vpp.item.BagTooltipData;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import dragoncrafted87.vpp.item.BaseBagItem;
import dragoncrafted87.vpp.item.BaseBagItem.BagType;
import dragoncrafted87.vpp.screen.BagSlot;
import dragoncrafted87.vpp.MinecraftVPPScreenHandler;

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
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null && isBagSlotMismatch(client.player)) {
                InventoryUtility.updateBagSlots(client.player);
                if (DebugFlags.DEBUG_BAG_EVENTS) {
                    LOGGER.info("Detected mismatch, updating bag slots");
                }
            }
        });
    }

    private static boolean isBagSlotMismatch(PlayerEntity player) {
        MinecraftVPPScreenHandler handler = (MinecraftVPPScreenHandler) player.playerScreenHandler;
        // Check satchel
        ItemStack satchel = InventoryUtility.findBagItem(player, BagType.SATCHEL, false);
        boolean satchelPresent = !satchel.isEmpty();
        boolean satchelEnabled = handler.vpp$getSatchelSlots().get(0).isEnabled();
        if (satchelPresent != satchelEnabled)
            return true;
        // Left pouch
        ItemStack leftPouch = InventoryUtility.findBagItem(player, BagType.POUCH, false);
        boolean leftPresent = !leftPouch.isEmpty();
        boolean leftEnabled = handler.vpp$getLeftPouchSlots().get(0).isEnabled();
        if (leftPresent != leftEnabled)
            return true;
        // Right pouch
        ItemStack rightPouch = InventoryUtility.findBagItem(player, BagType.POUCH, true);
        boolean rightPresent = !rightPouch.isEmpty();
        boolean rightEnabled = handler.vpp$getRightPouchSlots().get(0).isEnabled();
        if (rightPresent != rightEnabled)
            return true;
        return false;
    }
}
