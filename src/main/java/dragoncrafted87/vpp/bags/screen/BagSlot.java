package dragoncrafted87.vpp.bags.screen;

import dragoncrafted87.vpp.bags.BaseBagItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class BagSlot extends Slot {
    private final int index;
    public Inventory inventory;
    private boolean enabled = false;

    public BagSlot(int index, int x, int y) {
        super(null, index, x, y);
        this.index = index;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public void setEnabled(boolean state) {
        enabled = state;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        if (stack.getItem() instanceof BaseBagItem)
            return false;
        return enabled && inventory != null;
    }

    @Override
    public boolean canTakeItems(PlayerEntity playerEntity) {
        return enabled && inventory != null;
    }

    @Override
    public boolean isEnabled() {
        return enabled && inventory != null;
    }

    @Override
    public ItemStack getStack() {
        if (!enabled || inventory == null)
            return ItemStack.EMPTY;
        return this.inventory.getStack(this.index);
    }

    @Override
    public void setStack(ItemStack stack) {
        if (!enabled || inventory == null)
            return;
        this.inventory.setStack(this.index, stack);
        this.markDirty();
    }

    @Override
    public void setStackNoCallbacks(ItemStack stack) {
        if (!enabled || inventory == null)
            return;
        this.inventory.setStack(this.index, stack);
    }

    @Override
    public void markDirty() {
        if (!enabled || inventory == null)
            return;
        this.inventory.markDirty();
    }

    @Override
    public ItemStack takeStack(int amount) {
        if (!enabled || inventory == null)
            return ItemStack.EMPTY;
        return this.inventory.removeStack(this.index, amount);
    }

    @Override
    public int getMaxItemCount() {
        if (!enabled || inventory == null)
            return 0;
        return this.inventory.getMaxCountPerStack();
    }
}
