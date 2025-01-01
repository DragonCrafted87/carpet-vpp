package com.example.mixin;

import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.example.TemplateMod;

import net.minecraft.block.BeaconBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.TickPriority;
import net.minecraft.world.World;

@Debug(export = true)
@Mixin(BeaconBlockEntity.class)
public class BeaconBlockEntityMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private static void extendTick(World world, BlockPos pos, BlockState state, BeaconBlockEntity blockEntity,
            CallbackInfo info) {

        if (world.getTime() % 80L == 0L) {
            if (world.isReceivingRedstonePower(pos)) {
                world.createAndScheduleBlockTick(pos, Blocks.BEACON, 1, TickPriority.VERY_HIGH);
                if (world instanceof ServerWorld) {
                    ServerWorld serverWorld = (ServerWorld) world;

                    // set chunk to be force loaded
                    serverWorld.setChunkForced(pos.getX() >> 4, pos.getZ() >> 4, true);
                }
            } else {
                if (world instanceof ServerWorld) {
                    ServerWorld serverWorld = (ServerWorld) world;

                    // set chunk to be force loaded
                    serverWorld.setChunkForced(pos.getX() >> 4, pos.getZ() >> 4, false);
                }
            }
        }

    }

}
