package dragoncrafted87.vpp.mixin.client;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import static net.minecraft.client.gui.DrawableHelper.fill;
import dragoncrafted87.vpp.DebugFlags;  // Import DebugFlags
import dragoncrafted87.vpp.MinecraftVPPClient;
import dragoncrafted87.vpp.StewInfo;  // Import the utility
import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow @Final private MinecraftClient client;
    @Shadow private int heldItemTooltipFade;
    @Shadow private ItemStack currentStack;
    @Shadow private int scaledWidth;
    @Shadow public abstract TextRenderer getTextRenderer();
    @Shadow private int scaledHeight;

    @Inject(at = @At("HEAD"), method = "renderHeldItemTooltip")
    public void onInjectTooltip(MatrixStack matrixStack, CallbackInfo info) {
        this.client.getProfiler().push("selectedItemName");
        if (this.heldItemTooltipFade > 0 && !this.currentStack.isEmpty()) {
            MutableText mutableText = Text.empty().append(this.currentStack.getName()).formatted(this.currentStack.getRarity().formatting);
            if (this.currentStack.hasCustomName()) {
                mutableText.formatted(Formatting.ITALIC);
            }
            int mainItemNameWidth = this.getTextRenderer().getWidth(mutableText);
            int j = (this.scaledWidth - mainItemNameWidth) / 2;
            int hotbarOffset = this.scaledHeight - 59;
            if (!this.client.interactionManager.hasStatusBars()) {
                hotbarOffset += 14;
            }
            int opacity = (int)((float)this.heldItemTooltipFade * 256.0F / 10.0F);
            if (opacity > 255) {
                opacity = 255;
            }
            if (opacity > 0) {
                matrixStack.push();
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                int var10001 = j - 2;
                int var10002 = hotbarOffset - 2;
                int var10003 = j + mainItemNameWidth + 2;
                this.getTextRenderer().getClass();
                fill(matrixStack, var10001, var10002, var10003, hotbarOffset + 9 + 2, this.client.options.getTextBackgroundColor(0));

                // Now add effects using consolidated method
                List<Text> effectTexts = StewInfo.getStewEffectTexts(currentStack);
                if (!effectTexts.isEmpty() && DebugFlags.DEBUG_STEW_INFO) {
                    MinecraftVPPClient.LOGGER.info("Displaying stew effects in HUD for item: {}", currentStack.getName().getString());
                }
                for (int i = 0; i < effectTexts.size(); i++) {
                    Text completeText = effectTexts.get(i);
                    j = (this.scaledWidth - getTextRenderer().getWidth(completeText)) / 2;
                    this.getTextRenderer().drawWithShadow(matrixStack, completeText, (float)j, (float)hotbarOffset - (i * 14) - 14, 13421772 + (opacity << 24));
                }

                RenderSystem.disableBlend();
                matrixStack.pop();
            }
        }
    }
}
