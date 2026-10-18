package dragoncrafted87.vpp.mixin.client;

import dragoncrafted87.vpp.bags.BaseBagItem;
import dragoncrafted87.vpp.bags.BaseBagItem.BagType;
import dragoncrafted87.vpp.bags.InventoryUtility;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = RecipeBookComponent.class, priority = 950)
public class RecipeBookWidgetMixin {
    @Shadow protected Minecraft minecraft;
    @Shadow private int xOffset;

    @Inject(method = "updateScreenPosition", at = @At("RETURN"), cancellable = true)
    private void vpp$modifyRecipeBookPosition(
            int width, int backgroundWidth, CallbackInfoReturnable<Integer> callbackInfo) {
        if (this.minecraft != null && this.minecraft.player != null && this.isVisible()) {
            ItemStack leftPouchStack =
                    InventoryUtility.findBagItem(this.minecraft.player, BagType.POUCH, false);
            if (!leftPouchStack.isEmpty()) {
                BaseBagItem bagItem = (BaseBagItem) leftPouchStack.getItem();
                int slots = bagItem.getSlotCount();
                int columns = (int) Math.ceil(slots / 3.0);
                int x = callbackInfo.getReturnValue();
                if (this.xOffset != 86) {
                    int diff = this.xOffset - 86;
                    x -= diff;
                }
                x += 18 * columns;
                callbackInfo.setReturnValue(x);
            }
        }
    }

    @Shadow
    public boolean isVisible() {
        return false;
    }
}
