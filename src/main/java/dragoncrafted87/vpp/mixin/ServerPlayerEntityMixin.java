package dragoncrafted87.vpp.mixin;

import dragoncrafted87.vpp.bags.BaseBagItem;
import dragoncrafted87.vpp.bags.BaseBagItem.BagType;
import dragoncrafted87.vpp.bags.InventoryUtility;
import dragoncrafted87.vpp.bags.screen.BagSlot;
import dragoncrafted87.vpp.core.MinecraftVPPNetworking;
import dragoncrafted87.vpp.core.MinecraftVPPScreenHandler;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @Inject(method = "onDeath", at = @At("HEAD"))
    private void vpp$attemptFixGraveMods(DamageSource source, CallbackInfo callbackInfo) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        MinecraftVPPScreenHandler handler = (MinecraftVPPScreenHandler) player.playerScreenHandler;
        if (!player.getWorld().getGameRules().getBoolean(GameRules.KEEP_INVENTORY)) {
            ItemStack backStack = InventoryUtility.findBagItem(player, BagType.SATCHEL, false);
            if (!backStack.isEmpty()) {
                BaseBagItem bagItem = (BaseBagItem) backStack.getItem();
                int slots = bagItem.getSlotCount();
                DefaultedList<BagSlot> bagSlots = handler.vpp$getSatchelSlots();
                for (int i = 0; i < slots; i++) {
                    BagSlot slot = bagSlots.get(i);
                    slot.setInventory(null);
                    slot.setEnabled(false);
                }
                ServerPlayNetworking.send(player, new MinecraftVPPNetworking.EnableSlotsPayload());
            }
            ItemStack leftPouchStack = InventoryUtility.findBagItem(player, BagType.POUCH, false);
            if (!leftPouchStack.isEmpty()) {
                BaseBagItem bagItem = (BaseBagItem) leftPouchStack.getItem();
                int slots = bagItem.getSlotCount();
                DefaultedList<BagSlot> bagSlots = handler.vpp$getLeftPouchSlots();
                for (int i = 0; i < slots; i++) {
                    BagSlot slot = bagSlots.get(i);
                    slot.setInventory(null);
                    slot.setEnabled(false);
                }
                ServerPlayNetworking.send(player, new MinecraftVPPNetworking.EnableSlotsPayload());
            }
            ItemStack rightPouchStack = InventoryUtility.findBagItem(player, BagType.POUCH, true);
            if (!rightPouchStack.isEmpty()) {
                BaseBagItem bagItem = (BaseBagItem) rightPouchStack.getItem();
                int slots = bagItem.getSlotCount();
                DefaultedList<BagSlot> bagSlots = handler.vpp$getRightPouchSlots();
                for (int i = 0; i < slots; i++) {
                    BagSlot slot = bagSlots.get(i);
                    slot.setInventory(null);
                    slot.setEnabled(false);
                }
                ServerPlayNetworking.send(player, new MinecraftVPPNetworking.EnableSlotsPayload());
            }
        }
    }
}
