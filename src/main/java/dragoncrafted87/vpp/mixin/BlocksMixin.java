package dragoncrafted87.vpp.mixin;

import net.minecraft.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Blocks.class)
public class BlocksMixin {
    @ModifyConstant(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=bedrock")), constant = @Constant(floatValue = -1.0F, ordinal = 0))
    private static float modifyBedrockHardness(float constant) {
        return 75.0f;
    }

    @ModifyConstant(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=bedrock")), constant = @Constant(floatValue = 3600000.0F, ordinal = 0))
    private static float modifyBedrockResistance(float constant) {
        return 2400.0f;
    }

    @ModifyConstant(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=deepslate")), constant = @Constant(floatValue = 3.0F, ordinal = 0))
    private static float modifyDeepslateHardness(float constant) {
        return 1.63f;
    }

    @ModifyConstant(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=end_portal_frame")), constant = @Constant(floatValue = -1.0F, ordinal = 0))
    private static float modifyEndPortalFrameHardness(float constant) {
        return 6.0f;
    }

    @ModifyConstant(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=end_portal_frame")), constant = @Constant(floatValue = 3600000.0F, ordinal = 0))
    private static float modifyEndPortalFrameResistance(float constant) {
        return 2400.0f;
    }
}
