package dragoncrafted87.vpp.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import dragoncrafted87.vpp.bags.BaseBagItem;
import dragoncrafted87.vpp.bags.BaseBagItem.BagType;
import dragoncrafted87.vpp.bags.InventoryUtility;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractInventoryScreen<PlayerScreenHandler> {
    private InventoryScreenMixin() {
        super(null, null, null);
    }

    @Inject(method = "isClickOutsideBounds", at = @At("TAIL"), cancellable = true)
    private void vpp$adjustOutsideBounds(double mouseX, double mouseY, int left, int top, int button,
            CallbackInfoReturnable<Boolean> callbackInfo) {
        if (this.client != null && this.client.player != null) {
            ItemStack backStack = InventoryUtility.findBagItem(this.client.player, BagType.SATCHEL, false);
            if (!backStack.isEmpty()) {
                BaseBagItem bagItem = (BaseBagItem) backStack.getItem();
                int slots = bagItem.getSlotCount();
                int rows = (int) Math.ceil(slots / 9);
                if (mouseY < (top + this.backgroundHeight) + 8 + (18 * rows) && mouseY >= (top + this.backgroundHeight)
                        && mouseX >= left && mouseX < (left + this.backgroundWidth)) {
                    callbackInfo.setReturnValue(false);
                }
            }
            ItemStack leftPouchStack = InventoryUtility.findBagItem(this.client.player, BagType.POUCH, false);
            if (!leftPouchStack.isEmpty()) {
                BaseBagItem bagItem = (BaseBagItem) leftPouchStack.getItem();
                int slots = bagItem.getSlotCount();
                int columns = (int) Math.ceil(slots / 3);
                if (mouseX >= left - (columns * 18) && mouseX < left && mouseY >= (top + this.backgroundHeight) - 90
                        && mouseY < (top + this.backgroundHeight) - 22) {
                    callbackInfo.setReturnValue(false);
                }
            }
            ItemStack rightPouchStack = InventoryUtility.findBagItem(this.client.player, BagType.POUCH, true);
            if (!rightPouchStack.isEmpty()) {
                BaseBagItem bagItem = (BaseBagItem) rightPouchStack.getItem();
                int slots = bagItem.getSlotCount();
                int columns = (int) Math.ceil(slots / 3);
                if (mouseX >= (left + this.backgroundWidth) && mouseX < (left + this.backgroundWidth) + (columns * 18)
                        && mouseY >= (top + this.backgroundHeight) - 90
                        && mouseY < (top + this.backgroundHeight) - 22) {
                    callbackInfo.setReturnValue(false);
                }
            }
        }
    }

    @Inject(method = "drawBackground", at = @At("TAIL"))
    private void vpp$drawBagSlots(MatrixStack matrices, float delta, int mouseX, int mouseY, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            int screenX = this.x;
            int screenY = this.y;
            int backgroundWidth = this.backgroundWidth;
            int backgroundHeight = this.backgroundHeight;
            // Draw satchel row
            ItemStack backStack = InventoryUtility.findBagItem(client.player, BagType.SATCHEL, false);
            if (!backStack.isEmpty()) {
                BaseBagItem bagItem = (BaseBagItem) backStack.getItem();
                int slots = bagItem.getSlotCount();
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderTexture(0, InventoryUtility.SLOT_TEXTURE);
                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                int x = screenX;
                int y = screenY + backgroundHeight - 3;
                DrawableHelper.drawTexture(matrices, x, y, 0f, 32f, backgroundWidth, 4, 256, 256);
                y += 4;
                float u = 0;
                float v = 36;
                for (int slot = 0; slot < slots; slot++) {
                    if (slot % 9 == 0) {
                        x = screenX;
                        u = 0;
                        DrawableHelper.drawTexture(matrices, x, y, u, v, 7, 18, 256, 256);
                        x += 7;
                        u += 7;
                    }
                    DrawableHelper.drawTexture(matrices, x, y, u, v, 18, 18, 256, 256);
                    x += 18;
                    u += 18;
                    if ((slot + 1) % 9 == 0) {
                        DrawableHelper.drawTexture(matrices, x, y, u, v, 7, 18, 256, 256);
                        y += 18;
                    }
                }
                x = screenX;
                DrawableHelper.drawTexture(matrices, x, y, 0f, 54f, backgroundWidth, 7, 256, 256);
            }
            // Draw left pouch slots
            ItemStack leftPouchStack = InventoryUtility.findBagItem(client.player, BagType.POUCH, false);
            if (!leftPouchStack.isEmpty()) {
                BaseBagItem bagItem = (BaseBagItem) leftPouchStack.getItem();
                int slots = bagItem.getSlotCount();
                int columns = (int) Math.ceil(slots / 3);
                int x = screenX;
                int y = screenY + 137;
                RenderSystem.setShaderTexture(0, InventoryUtility.SLOT_TEXTURE);
                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                DrawableHelper.drawTexture(matrices, x, y, 18f, 25f, 7, 7, 256, 256);
                for (int i = 0; i < columns; i++) {
                    x -= 11;
                    DrawableHelper.drawTexture(matrices, x, y, 7f, 25f, 11, 7, 256, 256);
                }
                if (columns > 1) {
                    for (int i = 0; i < columns - 1; i++) {
                        x -= 7;
                        DrawableHelper.drawTexture(matrices, x, y, 7f, 25f, 7, 7, 256, 256);
                    }
                }
                x -= 7;
                DrawableHelper.drawTexture(matrices, x, y, 0f, 25f, 7, 7, 256, 256);
                x = screenX + 7;
                y -= 54;
                for (int slot = 0; slot < slots; slot++) {
                    if (slot % 3 == 0) {
                        x -= 18;
                        y += 54;
                    }
                    y -= 18;
                    DrawableHelper.drawTexture(matrices, x, y, 7f, 7f, 18, 18, 256, 256);
                }
                x -= 7;
                y += 54;
                for (int i = 0; i < 3; i++) {
                    y -= 18;
                    DrawableHelper.drawTexture(matrices, x, y, 0f, 7f, 7, 18, 256, 256);
                }
                x = screenX;
                y -= 7;
                DrawableHelper.drawTexture(matrices, x, y, 18f, 0f, 7, 7, 256, 256);
                for (int i = 0; i < columns; i++) {
                    x -= 11;
                    DrawableHelper.drawTexture(matrices, x, y, 7f, 0f, 11, 7, 256, 256);
                }
                if (columns > 1) {
                    for (int i = 0; i < columns - 1; i++) {
                        x -= 7;
                        DrawableHelper.drawTexture(matrices, x, y, 7f, 0f, 7, 7, 256, 256);
                    }
                }
                x -= 7;
                DrawableHelper.drawTexture(matrices, x, y, 0f, 0f, 7, 7, 256, 256);
            }
            // Draw right pouch slots (similar logic)
            ItemStack rightPouchStack = InventoryUtility.findBagItem(client.player, BagType.POUCH, true);
            if (!rightPouchStack.isEmpty()) {
                BaseBagItem bagItem = (BaseBagItem) rightPouchStack.getItem();
                int slots = bagItem.getSlotCount();
                int columns = (int) Math.ceil(slots / 3);
                int x = screenX + backgroundWidth - 7;
                int y = screenY + 137;
                RenderSystem.setShaderTexture(0, InventoryUtility.SLOT_TEXTURE);
                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                DrawableHelper.drawTexture(matrices, x, y, 25f, 25f, 7, 7, 256, 256);
                x += 7;
                for (int i = 0; i < columns; i++) {
                    DrawableHelper.drawTexture(matrices, x, y, 7f, 25f, 11, 7, 256, 256);
                    x += 11;
                }
                if (columns > 1) {
                    for (int i = 0; i < columns - 1; i++) {
                        DrawableHelper.drawTexture(matrices, x, y, 7f, 25f, 7, 7, 256, 256);
                        x += 7;
                    }
                }
                DrawableHelper.drawTexture(matrices, x, y, 32f, 25f, 7, 7, 256, 256);
                x = screenX + backgroundWidth - 25;
                y -= 54;
                for (int slot = 0; slot < slots; slot++) {
                    if (slot % 3 == 0) {
                        x += 18;
                        y += 54;
                    }
                    y -= 18;
                    DrawableHelper.drawTexture(matrices, x, y, 7f, 7f, 18, 18, 256, 256);
                }
                x += 18;
                y += 54;
                for (int i = 0; i < 3; i++) {
                    y -= 18;
                    DrawableHelper.drawTexture(matrices, x, y, 32f, 7f, 7, 18, 256, 256);
                }
                x = screenX + backgroundWidth - 7;
                y -= 7;
                DrawableHelper.drawTexture(matrices, x, y, 25f, 0f, 7, 7, 256, 256);
                x += 7;
                for (int i = 0; i < columns; i++) {
                    DrawableHelper.drawTexture(matrices, x, y, 7f, 0f, 11, 7, 256, 256);
                    x += 11;
                }
                if (columns > 1) {
                    for (int i = 0; i < columns - 1; i++) {
                        DrawableHelper.drawTexture(matrices, x, y, 7f, 0f, 7, 7, 256, 256);
                        x += 7;
                    }
                }
                DrawableHelper.drawTexture(matrices, x, y, 32f, 0f, 7, 7, 256, 256);
            }
        }
    }
}
