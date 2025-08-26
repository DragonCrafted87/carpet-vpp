package dragoncrafted87.vpp;

import com.mojang.blaze3d.systems.RenderSystem;
import dragoncrafted87.vpp.bags.BaseBagItem.BagType;
import dragoncrafted87.vpp.bags.InventoryUtility;
import dragoncrafted87.vpp.core.MinecraftVPPScreenHandler;
import dragoncrafted87.vpp.core.MinecraftVPPNetworking;
import dragoncrafted87.vpp.gui.BagTooltipComponent;
import dragoncrafted87.vpp.bags.item.BagTooltipData;
import dragoncrafted87.vpp.mixin.client.InGameHudAccessor;
import dragoncrafted87.vpp.stew.StewInfo;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

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
        // Replacement for InGameHudMixin: Add stew effects to held item HUD
        HudRenderCallback.EVENT.register((matrices, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            ItemStack currentStack = client.player.getMainHandStack(); // Check main hand; adjust for offhand if needed
            int heldItemTooltipFade = ((InGameHudAccessor) client.inGameHud).getHeldItemTooltipFade();
            if (heldItemTooltipFade > 0 && !currentStack.isEmpty()) {
                client.getProfiler().push("vppHeldItemTooltip");
                MutableText name = Text.empty().append(currentStack.getName())
                        .formatted(currentStack.getRarity().formatting);
                if (currentStack.hasCustomName()) {
                    name.formatted(Formatting.ITALIC);
                }
                int width = client.textRenderer.getWidth(name);
                int j = (client.getWindow().getScaledWidth() - width) / 2;
                int hotbarOffset = client.getWindow().getScaledHeight() - 59;
                if (!client.interactionManager.hasStatusBars()) {
                    hotbarOffset += 14;
                }
                int opacity = (int) ((float) heldItemTooltipFade * 256.0F / 10.0F);
                if (opacity > 255) {
                    opacity = 255;
                }
                if (opacity > 0) {
                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                    DrawableHelper.fill(matrices, j - 2, hotbarOffset - 2, j + width + 2, hotbarOffset + 9 + 2,
                            client.options.getTextBackgroundColor(0));
                    List<Text> effectTexts = StewInfo.getStewEffectTexts(currentStack);
                    if (!effectTexts.isEmpty() && DebugFlags.DEBUG_STEW_INFO) {
                        LOGGER.info("Displaying stew effects in HUD for item: {}", currentStack.getName().getString());
                    }
                    for (int i = 0; i < effectTexts.size(); i++) {
                        Text text = effectTexts.get(i);
                        j = (client.getWindow().getScaledWidth() - client.textRenderer.getWidth(text)) / 2;
                        client.textRenderer.drawWithShadow(matrices, text, (float) j,
                                (float) (hotbarOffset - (i * 14) - 14), 13421772 + (opacity << 24));
                    }
                    RenderSystem.disableBlend();
                }
                client.getProfiler().pop();
            }
        });
        // Partial replacement for ItemStackMixin: Add stew effects to item tooltip
        ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
            StewInfo.onInjectTooltip(stack, lines);
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
