package com.example.mixin.client;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

import com.example.StewInfo;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(at = @At("RETURN"), method = "getTooltip")
    public void onInjectTooltip(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> ci) {
        StewInfo.onInjectTooltip(this, ci.getReturnValue());
    }
}
