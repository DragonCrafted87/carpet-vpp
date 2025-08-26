package dragoncrafted87.vpp.mixin.client;

import dragoncrafted87.vpp.bags.BaseBagItem;
import dragoncrafted87.vpp.stew.StewInfo;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(at = @At("RETURN"), method = "getTooltip")
    public void onInjectTooltip(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> ci) {
        StewInfo.onInjectTooltip(this, ci.getReturnValue());
    }

    // Trinkets calls isItemEqual to check whether it should unequip old and equip
    // new[](https://github.com/emilyploszaj/trinkets/blob/37ee13d6/src/main/java/dev/emi/trinkets/mixin/LivingEntityMixin.java#L196-L199)
    // Excluding ourselves from this check to force unequip/equip when switching bag
    // items fixes a duplication bug (GH-12)
    // Gross and hacky but oh well, can't mixin mixins.
    @Inject(method = "isItemEqual", at = @At("HEAD"), cancellable = true)
    private void vpp$grossTrinketsEquipFix(ItemStack newStack, CallbackInfoReturnable<Boolean> callbackInfo) {
        ItemStack self = (ItemStack) (Object) this;
        if (self.getItem() instanceof BaseBagItem && newStack.getItem() instanceof BaseBagItem) {
            callbackInfo.setReturnValue(false);
        }
    }
}
