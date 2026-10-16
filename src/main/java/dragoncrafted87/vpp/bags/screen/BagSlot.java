package dragoncrafted87.vpp.bags.screen;

import dragoncrafted87.vpp.bags.BaseBagItem;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class BagSlot extends Slot {
    private final int index;
    public Container inventory;
    private boolean enabled = false;

    public BagSlot(int index, int x, int y) {
        super(null, index, x, y);
        this.index = index;
    }

    public void setInventory(Container inventory) {
        this.inventory = inventory;
    }

    public void setEnabled(boolean state) {
        enabled = state;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        if (stack.getItem() instanceof BaseBagItem)
            return false;
        return enabled && inventory != null;
    }

    @Override
    public boolean mayPickup(Player player) {
        return enabled && inventory != null;
    }

    @Override
    public boolean isActive() {
        return enabled && inventory != null;
    }

    @Override
    public ItemStack getItem() {
        if (!enabled || inventory == null)
            return ItemStack.EMPTY;
        return this.inventory.getItem(this.index);
    }

    @Override
    public void set(ItemStack stack) {
        if (!enabled || inventory == null)
            return;
        this.inventory.setItem(this.index, stack);
    }

    @Override
    public void setByPlayer(ItemStack stack) {
        this.set(stack);
        this.setChanged();
    }

    @Override
    public void setChanged() {
        if (!enabled || inventory == null)
            return;
        this.inventory.setChanged();
    }

    @Override
    public ItemStack remove(int amount) {
        if (!enabled || inventory == null)
            return ItemStack.EMPTY;
        return this.inventory.removeItem(this.index, amount);
    }

    @Override
    public int getMaxStackSize() {
        if (!enabled || inventory == null)
            return 0;
        return this.inventory.getMaxStackSize();
    }
}
