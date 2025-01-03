package dragoncrafted87.vpp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dragoncrafted87.vpp.gui.BagTooltipComponent;
import dragoncrafted87.vpp.item.BagTooltipData;
import dragoncrafted87.vpp.item.BaseBagItem;
import dragoncrafted87.vpp.item.BaseBagItem.BagType;
import dragoncrafted87.vpp.mixin.client.HandledScreenAccessor;
import dragoncrafted87.vpp.screen.BagSlot;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import dragoncrafted87.vpp.gui.BagTooltipComponent;
import dragoncrafted87.vpp.item.BagTooltipData;
import dragoncrafted87.vpp.item.BaseBagItem;
import dragoncrafted87.vpp.item.BaseBagItem.BagType;
import dragoncrafted87.vpp.screen.BagSlot;

public class MinecraftVPPClient implements ClientModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("MinecraftVPPClient");

    @Override
    public void onInitializeClient() {


        LOGGER.info("ClientModInitializer");


        ClientPlayNetworking.registerGlobalReceiver(MinecraftVPPNetworking.ENABLE_SLOTS, (client, handler, packet, sender) -> {
            client.execute(() -> {
                MinecraftVPPScreenHandler screenHandler = (MinecraftVPPScreenHandler) client.player.playerScreenHandler;

                ItemStack satchelStack = InventoryUtility.findBagItem(client.player, BagType.SATCHEL, false);
                DefaultedList<BagSlot> satchelSlots = screenHandler.vpp$getSatchelSlots();

                for (int i = 0; i < MinecraftVPP.MAX_SATCHEL_SLOTS; i++) {
                    BagSlot slot = satchelSlots.get(i);
                    slot.setInventory(null);
                    slot.setEnabled(false);
                }
                if (!satchelStack.isEmpty()) {
                    BaseBagItem satchelItem = (BaseBagItem) satchelStack.getItem();
                    Inventory satchelInv = satchelItem.getInventory(satchelStack);

                    for (int i = 0; i < satchelItem.getSlotCount(); i++) {
                        BagSlot slot = satchelSlots.get(i);
                        slot.setInventory(satchelInv);
                        slot.setEnabled(true);
                    }
                }

                ItemStack leftPouchStack = InventoryUtility.findBagItem(client.player, BagType.POUCH, false);
                DefaultedList<BagSlot> leftPouchSlots = screenHandler.vpp$getLeftPouchSlots();

                for (int i = 0; i < MinecraftVPP.MAX_POUCH_SLOTS; i++) {
                    BagSlot slot = leftPouchSlots.get(i);
                    slot.setInventory(null);
                    slot.setEnabled(false);
                }
                if (!leftPouchStack.isEmpty()) {
                    BaseBagItem leftPouchItem = (BaseBagItem) leftPouchStack.getItem();
                    Inventory leftPouchInv = leftPouchItem.getInventory(leftPouchStack);

                    for (int i = 0; i < leftPouchItem.getSlotCount(); i++) {
                        BagSlot slot = leftPouchSlots.get(i);
                        slot.setInventory(leftPouchInv);
                        slot.setEnabled(true);
                    }
                }

                ItemStack rightPouchStack = InventoryUtility.findBagItem(client.player, BagType.POUCH, true);
                DefaultedList<BagSlot> rightPouchSlots = screenHandler.vpp$getRightPouchSlots();

                for (int i = 0; i < MinecraftVPP.MAX_POUCH_SLOTS; i++) {
                    BagSlot slot = rightPouchSlots.get(i);
                    slot.setInventory(null);
                    slot.setEnabled(false);
                }
                if (!rightPouchStack.isEmpty()) {
                    BaseBagItem rightPouchItem = (BaseBagItem) rightPouchStack.getItem();
                    Inventory rightPouchInv = rightPouchItem.getInventory(rightPouchStack);

                    for (int i = 0; i < rightPouchItem.getSlotCount(); i++) {
                        BagSlot slot = rightPouchSlots.get(i);
                        slot.setInventory(rightPouchInv);
                        slot.setEnabled(true);
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
