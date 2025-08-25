package dragoncrafted87.vpp.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.stat.StatHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import dragoncrafted87.vpp.InventoryUtility;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    @Inject(at = @At("RETURN"), method = "<init>")
    private void vpp$updateSlotsAfterInit(MinecraftClient client, ClientWorld world,
            ClientPlayNetworkHandler networkHandler, StatHandler stats, ClientRecipeBook recipeBook,
            boolean lastSneaking, boolean lastSprinting, CallbackInfo ci) {
        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
        InventoryUtility.updateBagSlots(player);
    }
}
