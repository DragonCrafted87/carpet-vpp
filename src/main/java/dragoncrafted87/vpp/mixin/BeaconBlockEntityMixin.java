package dragoncrafted87.vpp.mixin;

import dragoncrafted87.vpp.DebugFlags;
import dragoncrafted87.vpp.MinecraftVPP;
import dragoncrafted87.vpp.beacons.BeaconChunkLoaderData;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BeaconBlockEntity.class)
public abstract class BeaconBlockEntityMixin {
    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/BeaconBlockEntity;updateLevel(Lnet/minecraft/world/World;III)I", shift = At.Shift.AFTER))
    private static void extendTick(World world, BlockPos pos, BlockState state, BeaconBlockEntity blockEntity,
            CallbackInfo info) {
        if (world.getTime() % 80L == 0L) {
            if (world instanceof ServerWorld serverWorld) {
                ChunkPos chunkPos = new ChunkPos(pos);
                int pyramidLevel = ((BeaconBlockEntityAccessor) blockEntity).getPyramidLevel();
                boolean isActive = world.isReceivingRedstonePower(pos) && pyramidLevel > 0;
                BeaconChunkLoaderData data = BeaconChunkLoaderData.get(serverWorld);
                if (isActive) {
                    int radius = 1 + pyramidLevel;
                    serverWorld.getChunkManager().addTicket(MinecraftVPP.BEACON, chunkPos, radius);
                    data.addBeacon(pos);
                    if (DebugFlags.DEBUG_BEACON_LOADING) {
                        MinecraftVPP.LOGGER.info("Beacon at {} is active, loading chunk with radius {}", pos.toString(),
                                radius);
                    }
                } else {
                    data.removeBeacon(pos);
                    if (DebugFlags.DEBUG_BEACON_LOADING) {
                        MinecraftVPP.LOGGER.info("Beacon at {} is inactive, skipping chunk ticket refresh",
                                pos.toString());
                    }
                }
            }
        }
    }
}
