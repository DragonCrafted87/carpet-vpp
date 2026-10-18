package dragoncrafted87.vpp.mixin;

import dragoncrafted87.vpp.bags.BaseBagItem;
import dragoncrafted87.vpp.bags.BaseBagItem.BagType;
import dragoncrafted87.vpp.bags.InventoryUtility;
import dragoncrafted87.vpp.bags.screen.BagSlot;
import dragoncrafted87.vpp.core.MinecraftVPPNetworking;
import dragoncrafted87.vpp.core.MinecraftVPPScreenHandler;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gamerules.GameRules;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class ServerPlayerEntityMixin {
    @Inject(method = "die", at = @At("HEAD"))
    private void vpp$attemptFixGraveMods(DamageSource source, CallbackInfo callbackInfo) {
        ServerPlayer player = (ServerPlayer) (Object) this;
        MinecraftVPPScreenHandler handler = (MinecraftVPPScreenHandler) player.inventoryMenu;
        ServerLevel level = (ServerLevel) player.level();
        if (!level.getServer().getGameRules().get(GameRules.KEEP_INVENTORY)) {
            clearBag(
                    player,
                    handler.vpp$getSatchelSlots(),
                    InventoryUtility.findBagItem(player, BagType.SATCHEL, false));
            clearBag(
                    player,
                    handler.vpp$getLeftPouchSlots(),
                    InventoryUtility.findBagItem(player, BagType.POUCH, false));
            clearBag(
                    player,
                    handler.vpp$getRightPouchSlots(),
                    InventoryUtility.findBagItem(player, BagType.POUCH, true));
        }
    }

    private static void clearBag(
            ServerPlayer player, NonNullList<BagSlot> bagSlots, ItemStack bagStack) {
        if (bagStack.isEmpty()) {
            return;
        }
        BaseBagItem bagItem = (BaseBagItem) bagStack.getItem();
        int slots = bagItem.getSlotCount();
        for (int i = 0; i < slots; i++) {
            BagSlot slot = bagSlots.get(i);
            slot.setInventory(null);
            slot.setEnabled(false);
        }
        ServerPlayNetworking.send(player, new MinecraftVPPNetworking.EnableSlotsPayload());
    }
}
