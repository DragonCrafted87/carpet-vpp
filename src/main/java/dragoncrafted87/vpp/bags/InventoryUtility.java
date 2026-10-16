package dragoncrafted87.vpp.bags;

import dragoncrafted87.vpp.MinecraftVPP;
import dragoncrafted87.vpp.bags.screen.BagSlot;
import dragoncrafted87.vpp.core.MinecraftVPPScreenHandler;

import eu.pb4.trinkets.api.TrinketSlotAccess;
import eu.pb4.trinkets.api.TrinketsApi;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class InventoryUtility {
    public static final Identifier SLOT_TEXTURE =
            Identifier.fromNamespaceAndPath("vpp", "textures/gui/slots.png");

    public static ItemStack findBagItem(Player player, BaseBagItem.BagType type, boolean right) {
        ItemStack targetStack = ItemStack.EMPTY;
        for (TrinketSlotAccess access : TrinketsApi.getAttachment(player).allEquipped(false)) {
            ItemStack slotStack = access.get();
            if (slotStack.getItem() instanceof BaseBagItem bagItem && bagItem.getType() == type) {
                if (type == BaseBagItem.BagType.SATCHEL || access.index() == (right ? 1 : 0)) {
                    targetStack = slotStack;
                    break;
                }
            }
        }
        return targetStack;
    }

    public static void alignMenuSlotIndices(List<Slot> slots) {
        for (int i = 0; i < slots.size(); i++) {
            slots.get(i).index = i;
        }
    }

    public static void updateBagSlots(Player player) {
        MinecraftVPPScreenHandler handler = (MinecraftVPPScreenHandler) player.inventoryMenu;
        ItemStack satchelStack = findBagItem(player, BaseBagItem.BagType.SATCHEL, false);
        NonNullList<BagSlot> satchelSlots = handler.vpp$getSatchelSlots();
        for (int i = 0; i < MinecraftVPP.MAX_SATCHEL_SLOTS; i++) {
            BagSlot slot = satchelSlots.get(i);
            slot.setInventory(null);
            slot.setEnabled(false);
        }
        if (!satchelStack.isEmpty()) {
            BaseBagItem satchelItem = (BaseBagItem) satchelStack.getItem();
            Container satchelInv = satchelItem.getInventory(satchelStack, player);
            for (int i = 0; i < satchelItem.getSlotCount(); i++) {
                BagSlot slot = satchelSlots.get(i);
                slot.setInventory(satchelInv);
                slot.setEnabled(true);
            }
        }
        ItemStack leftPouchStack = findBagItem(player, BaseBagItem.BagType.POUCH, false);
        NonNullList<BagSlot> leftPouchSlots = handler.vpp$getLeftPouchSlots();
        for (int i = 0; i < MinecraftVPP.MAX_POUCH_SLOTS; i++) {
            BagSlot slot = leftPouchSlots.get(i);
            slot.setInventory(null);
            slot.setEnabled(false);
        }
        if (!leftPouchStack.isEmpty()) {
            BaseBagItem leftPouchItem = (BaseBagItem) leftPouchStack.getItem();
            Container leftPouchInv = leftPouchItem.getInventory(leftPouchStack, player);
            for (int i = 0; i < leftPouchItem.getSlotCount(); i++) {
                BagSlot slot = leftPouchSlots.get(i);
                slot.setInventory(leftPouchInv);
                slot.setEnabled(true);
            }
        }
        ItemStack rightPouchStack = findBagItem(player, BaseBagItem.BagType.POUCH, true);
        NonNullList<BagSlot> rightPouchSlots = handler.vpp$getRightPouchSlots();
        for (int i = 0; i < MinecraftVPP.MAX_POUCH_SLOTS; i++) {
            BagSlot slot = rightPouchSlots.get(i);
            slot.setInventory(null);
            slot.setEnabled(false);
        }
        if (!rightPouchStack.isEmpty()) {
            BaseBagItem rightPouchItem = (BaseBagItem) rightPouchStack.getItem();
            Container rightPouchInv = rightPouchItem.getInventory(rightPouchStack, player);
            for (int i = 0; i < rightPouchItem.getSlotCount(); i++) {
                BagSlot slot = rightPouchSlots.get(i);
                slot.setInventory(rightPouchInv);
                slot.setEnabled(true);
            }
        }
    }
}
