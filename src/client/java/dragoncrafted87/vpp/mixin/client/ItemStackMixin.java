package dragoncrafted87.vpp.mixin.client;

import dragoncrafted87.vpp.bags.BaseBagItem;

import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    // Trinkets calls isSameItem to check whether it should unequip old and equip
    // new. Excluding bags from this check forces unequip/equip when switching bag
    // items and avoids a duplication bug.
    @Inject(
            method =
                    "isSameItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z",
            at = @At("HEAD"),
            cancellable = true)
    private static void vpp$trinketsEquipFix(
            ItemStack left, ItemStack right, CallbackInfoReturnable<Boolean> callbackInfo) {
        if (left.getItem() instanceof BaseBagItem && right.getItem() instanceof BaseBagItem) {
            callbackInfo.setReturnValue(false);
        }
    }
}
