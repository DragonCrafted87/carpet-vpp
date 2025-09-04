package dragoncrafted87.vpp.mixin.client;

import dragoncrafted87.vpp.bags.BaseBagItem;
import dragoncrafted87.vpp.bags.BaseBagItem.BagType;
import dragoncrafted87.vpp.bags.InventoryUtility;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.client.gui.DrawContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.client.gui.screen.ingame.InventoryScreen.class)
public abstract class InventoryScreenMixin extends HandledScreen<PlayerScreenHandler> {
    private InventoryScreenMixin() {
        super(null, null, null);
    }

    @Override
    protected boolean isClickOutsideBounds(double mouseX, double mouseY, int left, int top, int button) {
        boolean outsideMain = super.isClickOutsideBounds(mouseX, mouseY, left, top, button);
        if (!outsideMain) {
            return false;
        }
        if (this.client != null && this.client.player != null) {
            ItemStack backStack = InventoryUtility.findBagItem(this.client.player, BagType.SATCHEL, false);
            if (!backStack.isEmpty()) {
                BaseBagItem bagItem = (BaseBagItem) backStack.getItem();
                int slots = bagItem.getSlotCount();
                int rows = (int) Math.ceil(slots / 9.0);
                if (mouseY >= (top + this.backgroundHeight) && mouseY < (top + this.backgroundHeight) + 8 + (18 * rows)
                        && mouseX >= left && mouseX < (left + this.backgroundWidth)) {
                    return false;
                }
            }
            ItemStack leftPouchStack = InventoryUtility.findBagItem(this.client.player, BagType.POUCH, false);
            if (!leftPouchStack.isEmpty()) {
                BaseBagItem bagItem = (BaseBagItem) leftPouchStack.getItem();
                int slots = bagItem.getSlotCount();
                int columns = (int) Math.ceil(slots / 3.0);
                if (mouseX >= left - (columns * 18) && mouseX < left && mouseY >= (top + this.backgroundHeight) - 90
                        && mouseY < (top + this.backgroundHeight) - 22) {
                    return false;
                }
            }
            ItemStack rightPouchStack = InventoryUtility.findBagItem(this.client.player, BagType.POUCH, true);
            if (!rightPouchStack.isEmpty()) {
                BaseBagItem bagItem = (BaseBagItem) rightPouchStack.getItem();
                int slots = bagItem.getSlotCount();
                int columns = (int) Math.ceil(slots / 3.0);
                if (mouseX >= (left + this.backgroundWidth) && mouseX < (left + this.backgroundWidth) + (columns * 18)
                        && mouseY >= (top + this.backgroundHeight) - 90
                        && mouseY < (top + this.backgroundHeight) - 22) {
                    return false;
                }
            }
        }
        return true;
    }

    @Inject(method = "drawBackground", at = @At("TAIL"))
    private void vpp$drawBagSlots(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci) {
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
                int x = screenX;
                int y = screenY + backgroundHeight - 3;
                context.drawTexture(RenderLayer::getGuiTextured, InventoryUtility.SLOT_TEXTURE, x, y, 0.0f, 32.0f,
                        backgroundWidth, 4, 256, 256);
                y += 4;
                float u = 0;
                float v = 36;
                for (int slot = 0; slot < slots; slot++) {
                    if (slot % 9 == 0) {
                        x = screenX;
                        u = 0;
                        context.drawTexture(RenderLayer::getGuiTextured, InventoryUtility.SLOT_TEXTURE, x, y, u, v, 7,
                                18, 256, 256);
                        x += 7;
                        u += 7;
                    }
                    context.drawTexture(RenderLayer::getGuiTextured, InventoryUtility.SLOT_TEXTURE, x, y, u, v, 18, 18,
                            256, 256);
                    x += 18;
                    u += 18;
                    if ((slot + 1) % 9 == 0) {
                        context.drawTexture(RenderLayer::getGuiTextured, InventoryUtility.SLOT_TEXTURE, x, y, u, v, 7,
                                18, 256, 256);
                        y += 18;
                    }
                }
                x = screenX;
                context.drawTexture(RenderLayer::getGuiTextured, InventoryUtility.SLOT_TEXTURE, x, y, 0.0f, 54.0f,
                        backgroundWidth, 7, 256, 256);
            }
            // Draw left pouch slots
            ItemStack leftPouchStack = InventoryUtility.findBagItem(client.player, BagType.POUCH, false);
            if (!leftPouchStack.isEmpty()) {
                BaseBagItem bagItem = (BaseBagItem) leftPouchStack.getItem();
                int slots = bagItem.getSlotCount();
                int columns = (int) Math.ceil(slots / 3);
                int x = screenX;
                int y = screenY + 137;
                context.drawTexture(RenderLayer::getGuiTextured, InventoryUtility.SLOT_TEXTURE, x, y, 18.0f, 25.0f, 7,
                        7, 256, 256);
                for (int i = 0; i < columns; i++) {
                    x -= 11;
                    context.drawTexture(RenderLayer::getGuiTextured, InventoryUtility.SLOT_TEXTURE, x, y, 7.0f, 25.0f,
                            11, 7, 256, 256);
                }
                if (columns > 1) {
                    for (int i = 0; i < columns - 1; i++) {
                        x -= 7;
                        context.drawTexture(RenderLayer::getGuiTextured, InventoryUtility.SLOT_TEXTURE, x, y, 7.0f,
                                25.0f, 7, 7, 256, 256);
                    }
                }
                x -= 7;
                context.drawTexture(RenderLayer::getGuiTextured, InventoryUtility.SLOT_TEXTURE, x, y, 0.0f, 25.0f, 7, 7,
                        256, 256);
                x = screenX + 7;
                y -= 54;
                for (int slot = 0; slot < slots; slot++) {
                    if (slot % 3 == 0) {
                        x -= 18;
                        y += 54;
                    }
                    y -= 18;
                    context.drawTexture(RenderLayer::getGuiTextured, InventoryUtility.SLOT_TEXTURE, x, y, 7.0f, 7.0f,
                            18, 18, 256, 256);
                }
                x -= 7;
                y += 54;
                for (int i = 0; i < 3; i++) {
                    y -= 18;
                    context.drawTexture(RenderLayer::getGuiTextured, InventoryUtility.SLOT_TEXTURE, x, y, 0.0f, 7.0f, 7,
                            18, 256, 256);
                }
                x = screenX;
                y -= 7;
                context.drawTexture(RenderLayer::getGuiTextured, InventoryUtility.SLOT_TEXTURE, x, y, 18.0f, 0.0f, 7, 7,
                        256, 256);
                for (int i = 0; i < columns; i++) {
                    x -= 11;
                    context.drawTexture(RenderLayer::getGuiTextured, InventoryUtility.SLOT_TEXTURE, x, y, 7.0f, 0.0f,
                            11, 7, 256, 256);
                }
                if (columns > 1) {
                    for (int i = 0; i < columns - 1; i++) {
                        x -= 7;
                        context.drawTexture(RenderLayer::getGuiTextured, InventoryUtility.SLOT_TEXTURE, x, y, 7.0f,
                                0.0f, 7, 7, 256, 256);
                    }
                }
                x -= 7;
                context.drawTexture(RenderLayer::getGuiTextured, InventoryUtility.SLOT_TEXTURE, x, y, 0.0f, 0.0f, 7, 7,
                        256, 256);
            }
            // Draw right pouch slots (similar logic)
            ItemStack rightPouchStack = InventoryUtility.findBagItem(client.player, BagType.POUCH, true);
            if (!rightPouchStack.isEmpty()) {
                BaseBagItem bagItem = (BaseBagItem) rightPouchStack.getItem();
                int slots = bagItem.getSlotCount();
                int columns = (int) Math.ceil(slots / 3);
                int x = screenX + backgroundWidth - 7;
                int y = screenY + 137;
                context.drawTexture(RenderLayer::getGuiTextured, InventoryUtility.SLOT_TEXTURE, x, y, 25.0f, 25.0f, 7,
                        7, 256, 256);
                x += 7;
                for (int i = 0; i < columns; i++) {
                    context.drawTexture(RenderLayer::getGuiTextured, InventoryUtility.SLOT_TEXTURE, x, y, 7.0f, 25.0f,
                            11, 7, 256, 256);
                    x += 11;
                }
                if (columns > 1) {
                    for (int i = 0; i < columns - 1; i++) {
                        context.drawTexture(RenderLayer::getGuiTextured, InventoryUtility.SLOT_TEXTURE, x, y, 7.0f,
                                25.0f, 7, 7, 256, 256);
                        x += 7;
                    }
                }
                context.drawTexture(RenderLayer::getGuiTextured, InventoryUtility.SLOT_TEXTURE, x, y, 32.0f, 25.0f, 7,
                        7, 256, 256);
                x = screenX + backgroundWidth - 25;
                y -= 54;
                for (int slot = 0; slot < slots; slot++) {
                    if (slot % 3 == 0) {
                        x += 18;
                        y += 54;
                    }
                    y -= 18;
                    context.drawTexture(RenderLayer::getGuiTextured, InventoryUtility.SLOT_TEXTURE, x, y, 7.0f, 7.0f,
                            18, 18, 256, 256);
                }
                x += 18;
                y += 54;
                for (int i = 0; i < 3; i++) {
                    y -= 18;
                    context.drawTexture(RenderLayer::getGuiTextured, InventoryUtility.SLOT_TEXTURE, x, y, 32.0f, 7.0f,
                            7, 18, 256, 256);
                }
                x = screenX + backgroundWidth - 7;
                y -= 7;
                context.drawTexture(RenderLayer::getGuiTextured, InventoryUtility.SLOT_TEXTURE, x, y, 25.0f, 0.0f, 7, 7,
                        256, 256);
                x += 7;
                for (int i = 0; i < columns; i++) {
                    context.drawTexture(RenderLayer::getGuiTextured, InventoryUtility.SLOT_TEXTURE, x, y, 7.0f, 0.0f,
                            11, 7, 256, 256);
                    x += 11;
                }
                if (columns > 1) {
                    for (int i = 0; i < columns - 1; i++) {
                        context.drawTexture(RenderLayer::getGuiTextured, InventoryUtility.SLOT_TEXTURE, x, y, 7.0f,
                                0.0f, 7, 7, 256, 256);
                        x += 7;
                    }
                }
                context.drawTexture(RenderLayer::getGuiTextured, InventoryUtility.SLOT_TEXTURE, x, y, 32.0f, 0.0f, 7, 7,
                        256, 256);
            }
        }
    }
}
