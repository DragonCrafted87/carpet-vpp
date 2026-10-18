package dragoncrafted87.vpp.mixin;

import dragoncrafted87.vpp.DebugFlags;
import dragoncrafted87.vpp.MinecraftVPP;
import dragoncrafted87.vpp.beacons.BeaconChunkLoaderData;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BeaconBlockEntity.class)
public abstract class BeaconBlockEntityMixin {
    @Inject(
            method = "tick",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lnet/minecraft/world/level/block/entity/BeaconBlockEntity;updateBase(Lnet/minecraft/world/level/Level;III)I",
                            shift = At.Shift.AFTER))
    private static void extendTick(
            Level world,
            BlockPos pos,
            BlockState state,
            BeaconBlockEntity blockEntity,
            CallbackInfo info) {
        if (world.getGameTime() % 80L == 0L) {
            if (world instanceof ServerLevel serverWorld) {
                ChunkPos chunkPos = ChunkPos.containing(pos);
                int pyramidLevel = ((BeaconBlockEntityAccessor) blockEntity).getPyramidLevel();
                boolean isActive = world.hasNeighborSignal(pos) && pyramidLevel > 0;
                BeaconChunkLoaderData data = BeaconChunkLoaderData.get(serverWorld);
                if (isActive) {
                    int radius = 1 + pyramidLevel;
                    serverWorld
                            .getChunkSource()
                            .addTicketWithRadius(MinecraftVPP.BEACON, chunkPos, radius);
                    data.addBeacon(pos);
                    if (DebugFlags.DEBUG_BEACON_LOADING) {
                        MinecraftVPP.LOGGER.info(
                                "Beacon at {} is active, loading chunk with radius {}",
                                pos.toString(),
                                radius);
                    }
                } else {
                    data.removeBeacon(pos);
                    if (DebugFlags.DEBUG_BEACON_LOADING) {
                        MinecraftVPP.LOGGER.info(
                                "Beacon at {} is inactive, skipping chunk ticket refresh",
                                pos.toString());
                    }
                }
            }
        }
    }
}
