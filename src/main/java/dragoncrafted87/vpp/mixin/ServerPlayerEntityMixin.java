package dragoncrafted87.vpp.mixin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.GameRules;
import com.mojang.authlib.GameProfile;
import dragoncrafted87.vpp.MinecraftVPPNetworking;
import dragoncrafted87.vpp.MinecraftVPPScreenHandler;
import dragoncrafted87.vpp.InventoryUtility;
import dragoncrafted87.vpp.item.BaseBagItem;
import dragoncrafted87.vpp.item.BaseBagItem.BagType;
import dragoncrafted87.vpp.screen.BagSlot;
import net.minecraft.network.encryption.PlayerPublicKey;
@SuppressWarnings("deprecation")
@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @Inject(method = "onDeath", at = @At("HEAD"))
    private void vpp$attemptFixGraveMods(DamageSource source, CallbackInfo callbackInfo) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        MinecraftVPPScreenHandler handler = (MinecraftVPPScreenHandler) player.playerScreenHandler;
        if (!player.world.getGameRules().getBoolean(GameRules.KEEP_INVENTORY)) {
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
                PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());
                ServerPlayNetworking.send(player, MinecraftVPPNetworking.ENABLE_SLOTS, packet);
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
                PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());
                ServerPlayNetworking.send(player, MinecraftVPPNetworking.ENABLE_SLOTS, packet);
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
                PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());
                ServerPlayNetworking.send(player, MinecraftVPPNetworking.ENABLE_SLOTS, packet);
            }
        }
    }
}
