package dragoncrafted87.vpp.mixin.client;

import dragoncrafted87.vpp.stew.StewHighlight;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.SuspiciousStewEffects;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.client.gui.Hud.class)
public class HudMixin {
    @Shadow private ItemStack lastToolHighlight;

    @Inject(method = "tick()V", at = @At("HEAD"))
    private void vpp$resetHighlightForStewType(CallbackInfo ci) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        ItemStack selected = player.getInventory().getSelectedItem();
        if (StewHighlight.effectsDiffer(
                selected.is(Items.SUSPICIOUS_STEW),
                selected.getOrDefault(
                        DataComponents.SUSPICIOUS_STEW_EFFECTS, SuspiciousStewEffects.EMPTY),
                this.lastToolHighlight.is(Items.SUSPICIOUS_STEW),
                this.lastToolHighlight.getOrDefault(
                        DataComponents.SUSPICIOUS_STEW_EFFECTS, SuspiciousStewEffects.EMPTY))) {
            this.lastToolHighlight = ItemStack.EMPTY;
        }
    }
}
