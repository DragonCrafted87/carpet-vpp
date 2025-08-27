package dragoncrafted87.vpp.mixin.client;
import dragoncrafted87.vpp.bags.BaseBagItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(ItemStack.class)
public class ItemStackMixin {
    // Trinkets calls isItemEqual to check whether it should unequip old and equip
    // new
    // Excluding ourselves from this check to force unequip/equip when switching bag
    // items fixes a duplication bug (GH-12)
    // Gross and hacky but oh well, can't mixin mixins.
    @Inject(method = "areItemsEqual(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z", at = @At("HEAD"), cancellable = true)
    private static void vpp$trinketsEquipFix(ItemStack left, ItemStack right, CallbackInfoReturnable<Boolean> callbackInfo) {
        if (left.getItem() instanceof BaseBagItem && right.getItem() instanceof BaseBagItem) {
            callbackInfo.setReturnValue(false);
        }
    }
}
