package dragoncrafted87.vpp.mixin;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.EndPortalFrameBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.LootTable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

// getDrops reads the loot-table field and returns nothing when it is empty.
// Vanilla leaves that field empty on the end portal frame.
@Mixin(BlockBehaviour.class)
public class EndPortalFrameBlockMixin {
    private static final ResourceKey<LootTable> VPP_LOOT_TABLE =
            ResourceKey.create(
                    Registries.LOOT_TABLE,
                    Identifier.fromNamespaceAndPath("minecraft", "blocks/end_portal_frame"));

    @Redirect(
            method = "getDrops",
            at = @At(value = "INVOKE", target = "Ljava/util/Optional;isEmpty()Z"))
    private boolean vpp$frameHasLootTable(Optional<?> drops) {
        if ((Object) this instanceof EndPortalFrameBlock) {
            return false;
        }
        return drops.isEmpty();
    }

    @Redirect(
            method = "getDrops",
            at = @At(value = "INVOKE", target = "Ljava/util/Optional;get()Ljava/lang/Object;"))
    private Object vpp$frameLootTable(Optional<?> drops) {
        if ((Object) this instanceof EndPortalFrameBlock && drops.isEmpty()) {
            return VPP_LOOT_TABLE;
        }
        return drops.get();
    }
}
