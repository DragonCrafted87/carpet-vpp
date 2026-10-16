package dragoncrafted87.vpp.mixin;

import dragoncrafted87.vpp.bags.InventoryUtility;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Trinkets removes its old range and appends a new one. Slots after that
// range keep the menu id they were given, and a click then reads a different
// list entry than the cell under the cursor.
@Mixin(value = InventoryMenu.class, priority = 1100)
public abstract class InventoryMenuSlotIndexMixin extends AbstractContainerMenu {
    protected InventoryMenuSlotIndexMixin() {
        super(null, 0);
    }

    @Inject(method = "trinkets$updateTrinketSlots", at = @At("RETURN"), remap = false)
    private void vpp$alignSlotIndices(boolean rebuild, CallbackInfo callbackInfo) {
        InventoryUtility.alignMenuSlotIndices(this.slots);
    }
}
