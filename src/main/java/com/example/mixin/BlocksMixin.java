package com.example.mixin;

import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.EndPortalFrameBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.PillarBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

@Debug(export = true)
@Mixin(Blocks.class)
public class BlocksMixin {

    @Redirect(
        slice = @Slice(
        from = @At(
            value = "CONSTANT",
            args= {
            "stringValue=bedrock"
            },
            ordinal = 0
        )
        ),
        at = @At(
        value = "NEW",
        target = "Lnet/minecraft/block/Block;*",
        ordinal = 0
        ),
        method = "<clinit>")
    private static Block bedrock(AbstractBlock.Settings settings) {
        return new Block(AbstractBlock.Settings.of(Material.STONE).strength(75.0f, 2400.0f).dropsNothing().allowsSpawning(BlocksMixin::never));
    }

    @Redirect(
        slice = @Slice(
        from = @At(
            value = "CONSTANT",
            args= {
            "stringValue=deepslate"
            },
            ordinal = 0
        )
        ),
        at = @At(
        value = "NEW",
        target = "Lnet/minecraft/block/PillarBlock;*",
        ordinal = 0
        ),
        method = "<clinit>")
    private static PillarBlock deepslate(AbstractBlock.Settings settings) {
        return new PillarBlock(AbstractBlock.Settings.of(Material.STONE, MapColor.DEEPSLATE_GRAY).requiresTool().strength(1.63f, 6.0f).sounds(BlockSoundGroup.DEEPSLATE));
    }

    @Redirect(
        slice = @Slice(
        from = @At(
            value = "CONSTANT",
            args= {
            "stringValue=end_portal_frame"
            },
            ordinal = 0
        )
        ),
        at = @At(
        value = "NEW",
        target = "Lnet/minecraft/block/EndPortalFrameBlock;*",
        ordinal = 0
        ),
        method = "<clinit>")
    private static EndPortalFrameBlock end_portal_frame(AbstractBlock.Settings settings) {
        return new EndPortalFrameBlock(AbstractBlock.Settings.of(Material.STONE, MapColor.GREEN).sounds(BlockSoundGroup.GLASS).luminance((state) -> {return 1;}).strength(6.0F, 2400.0F));
    }

    @Shadow
    private static Boolean never(BlockState state, BlockView world, BlockPos pos, EntityType<?> type) {return false;}
}
