package dragoncrafted87.vpp.bags;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.collection.DefaultedList;
import java.util.Optional;
import dragoncrafted87.vpp.MinecraftVPP;
import dragoncrafted87.vpp.bags.screen.BagSlot;
import dragoncrafted87.vpp.core.MinecraftVPPScreenHandler;

public class InventoryUtility {
    public static final Identifier SLOT_TEXTURE = new Identifier("vpp", "textures/gui/slots.png");

    public static ItemStack findBagItem(PlayerEntity player, BaseBagItem.BagType type, boolean right) {
        ItemStack targetStack = ItemStack.EMPTY;
        Optional<TrinketComponent> _component = TrinketsApi.getTrinketComponent(player);
        if (_component.isPresent()) {
            TrinketComponent component = _component.get();
            for (Pair<SlotReference, ItemStack> pair : component.getAllEquipped()) {
                ItemStack slotStack = pair.getRight();
                if (slotStack.getItem() instanceof BaseBagItem) {
                    BaseBagItem bagItem = (BaseBagItem) slotStack.getItem();
                    if (bagItem.getType() == type) {
                        SlotReference slotRef = pair.getLeft();
                        if (type == BaseBagItem.BagType.SATCHEL || slotRef.index() == (right ? 1 : 0)) {
                            targetStack = slotStack;
                            break;
                        }
                    }
                }
            }
        }
        return targetStack;
    }

    public static void updateBagSlots(PlayerEntity player) {
        MinecraftVPPScreenHandler handler = (MinecraftVPPScreenHandler) player.playerScreenHandler;
        ItemStack satchelStack = findBagItem(player, BaseBagItem.BagType.SATCHEL, false);
        DefaultedList<BagSlot> satchelSlots = handler.vpp$getSatchelSlots();
        for (int i = 0; i < MinecraftVPP.MAX_SATCHEL_SLOTS; i++) {
            BagSlot slot = satchelSlots.get(i);
            slot.setInventory(null);
            slot.setEnabled(false);
        }
        if (!satchelStack.isEmpty()) {
            BaseBagItem satchelItem = (BaseBagItem) satchelStack.getItem();
            Inventory satchelInv = satchelItem.getInventory(satchelStack, player);
            for (int i = 0; i < satchelItem.getSlotCount(); i++) {
                BagSlot slot = satchelSlots.get(i);
                slot.setInventory(satchelInv);
                slot.setEnabled(true);
            }
        }
        ItemStack leftPouchStack = findBagItem(player, BaseBagItem.BagType.POUCH, false);
        DefaultedList<BagSlot> leftPouchSlots = handler.vpp$getLeftPouchSlots();
        for (int i = 0; i < MinecraftVPP.MAX_POUCH_SLOTS; i++) {
            BagSlot slot = leftPouchSlots.get(i);
            slot.setInventory(null);
            slot.setEnabled(false);
        }
        if (!leftPouchStack.isEmpty()) {
            BaseBagItem leftPouchItem = (BaseBagItem) leftPouchStack.getItem();
            Inventory leftPouchInv = leftPouchItem.getInventory(leftPouchStack, player);
            for (int i = 0; i < leftPouchItem.getSlotCount(); i++) {
                BagSlot slot = leftPouchSlots.get(i);
                slot.setInventory(leftPouchInv);
                slot.setEnabled(true);
            }
        }
        ItemStack rightPouchStack = findBagItem(player, BaseBagItem.BagType.POUCH, true);
        DefaultedList<BagSlot> rightPouchSlots = handler.vpp$getRightPouchSlots();
        for (int i = 0; i < MinecraftVPP.MAX_POUCH_SLOTS; i++) {
            BagSlot slot = rightPouchSlots.get(i);
            slot.setInventory(null);
            slot.setEnabled(false);
        }
        if (!rightPouchStack.isEmpty()) {
            BaseBagItem rightPouchItem = (BaseBagItem) rightPouchStack.getItem();
            Inventory rightPouchInv = rightPouchItem.getInventory(rightPouchStack, player);
            for (int i = 0; i < rightPouchItem.getSlotCount(); i++) {
                BagSlot slot = rightPouchSlots.get(i);
                slot.setInventory(rightPouchInv);
                slot.setEnabled(true);
            }
        }
    }
}
