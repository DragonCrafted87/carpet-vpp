package dragoncrafted87.vpp;

import dragoncrafted87.vpp.bags.BaseBagItem.BagType;
import dragoncrafted87.vpp.bags.InventoryUtility;
import dragoncrafted87.vpp.bags.item.BagTooltipData;
import dragoncrafted87.vpp.core.MinecraftVPPNetworking;
import dragoncrafted87.vpp.core.MinecraftVPPScreenHandler;
import dragoncrafted87.vpp.gui.BagTooltipComponent;
import dragoncrafted87.vpp.mixin.client.InGameHudAccessor;
import dragoncrafted87.vpp.stew.StewInfo;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.ClientTooltipComponentCallback;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MinecraftVPPClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("MinecraftVPPClient");
    private static final Identifier STEW_HUD =
            Identifier.fromNamespaceAndPath(MinecraftVPP.MOD_ID, "stew_hud");

    @Override
    public void onInitializeClient() {
        LOGGER.info("MinecraftVPPClient initialized successfully");
        ClientPlayNetworking.registerGlobalReceiver(
                MinecraftVPPNetworking.EnableSlotsPayload.ID,
                (payload, context) -> {
                    context.client()
                            .execute(
                                    () -> {
                                        if (context.player() != null) {
                                            InventoryUtility.updateBagSlots(context.player());
                                        }
                                    });
                });
        ClientPlayConnectionEvents.JOIN.register(
                (netHandler, packetSender, client) -> {
                    client.execute(
                            () -> {
                                if (client.player != null) {
                                    InventoryUtility.updateBagSlots(client.player);
                                    if (DebugFlags.DEBUG_BAG_EVENTS) {
                                        LOGGER.info("Updated bag slots on client join");
                                    }
                                }
                            });
                });
        ClientTooltipComponentCallback.EVENT.register(
                data -> {
                    if (data instanceof BagTooltipData tooltipData) {
                        return new BagTooltipComponent(tooltipData);
                    }
                    return null;
                });
        ClientTickEvents.END_CLIENT_TICK.register(
                client -> {
                    if (client.player != null && isBagSlotMismatch(client.player)) {
                        InventoryUtility.updateBagSlots(client.player);
                        if (DebugFlags.DEBUG_BAG_EVENTS) {
                            LOGGER.info("Detected mismatch, updating bag slots");
                        }
                    }
                });
        HudElementRegistry.attachElementAfter(
                VanillaHudElements.HELD_ITEM_TOOLTIP,
                STEW_HUD,
                (graphics, tickCounter) -> {
                    Minecraft client = Minecraft.getInstance();
                    if (client.player == null) {
                        return;
                    }
                    ItemStack currentStack = client.player.getMainHandItem();
                    int heldItemTooltipFade =
                            ((InGameHudAccessor) client.gui.hud).getHeldItemTooltipFade();
                    if (heldItemTooltipFade > 0 && !currentStack.isEmpty()) {
                        Profiler.get().push("vppHeldItemTooltip");
                        MutableComponent name =
                                Component.empty()
                                        .append(currentStack.getHoverName())
                                        .withStyle(currentStack.getRarity().color());
                        if (currentStack.has(DataComponents.CUSTOM_NAME)) {
                            name.withStyle(ChatFormatting.ITALIC);
                        }
                        int width = client.font.width(name);
                        int j = (client.getWindow().getGuiScaledWidth() - width) / 2;
                        int hotbarOffset = client.getWindow().getGuiScaledHeight() - 59;
                        if (client.gameMode != null && !client.gameMode.hasExperience()) {
                            hotbarOffset += 14;
                        }
                        int opacity = (int) ((float) heldItemTooltipFade * 256.0F / 10.0F);
                        if (opacity > 255) {
                            opacity = 255;
                        }
                        if (opacity > 0) {
                            graphics.fill(
                                    j - 2,
                                    hotbarOffset - 2,
                                    j + width + 2,
                                    hotbarOffset + 9 + 2,
                                    client.options.getBackgroundColor(0));
                            List<Component> effectTexts = StewInfo.getStewEffectTexts(currentStack);
                            if (!effectTexts.isEmpty() && DebugFlags.DEBUG_STEW_INFO) {
                                LOGGER.info(
                                        "Displaying stew effects in HUD for item: {}",
                                        currentStack.getHoverName().getString());
                            }
                            for (int i = 0; i < effectTexts.size(); i++) {
                                Component text = effectTexts.get(i);
                                int textX =
                                        (client.getWindow().getGuiScaledWidth()
                                                        - client.font.width(text))
                                                / 2;
                                graphics.text(
                                        client.font,
                                        text,
                                        textX,
                                        hotbarOffset - (i * 14) - 14,
                                        13421772 + (opacity << 24),
                                        true);
                            }
                        }
                        Profiler.get().pop();
                    }
                });
        ItemTooltipCallback.EVENT.register(
                (stack, context, type, lines) -> {
                    StewInfo.onInjectTooltip(stack, lines);
                    if (stack.getItem() instanceof dragoncrafted87.vpp.bags.BaseBagItem bag) {
                        lines.add(
                                Component.translatable(
                                                "tooltip.vpp.slots",
                                                Component.literal(
                                                                String.valueOf(bag.getSlotCount()))
                                                        .withStyle(ChatFormatting.BLUE))
                                        .withStyle(ChatFormatting.GRAY));
                    }
                });
    }

    private static boolean isBagSlotMismatch(Player player) {
        MinecraftVPPScreenHandler handler = (MinecraftVPPScreenHandler) player.inventoryMenu;
        ItemStack satchel = InventoryUtility.findBagItem(player, BagType.SATCHEL, false);
        boolean satchelPresent = !satchel.isEmpty();
        boolean satchelEnabled = handler.vpp$getSatchelSlots().get(0).isActive();
        if (satchelPresent != satchelEnabled) return true;
        ItemStack leftPouch = InventoryUtility.findBagItem(player, BagType.POUCH, false);
        boolean leftPresent = !leftPouch.isEmpty();
        boolean leftEnabled = handler.vpp$getLeftPouchSlots().get(0).isActive();
        if (leftPresent != leftEnabled) return true;
        ItemStack rightPouch = InventoryUtility.findBagItem(player, BagType.POUCH, true);
        boolean rightPresent = !rightPouch.isEmpty();
        boolean rightEnabled = handler.vpp$getRightPouchSlots().get(0).isActive();
        return rightPresent != rightEnabled;
    }
}
