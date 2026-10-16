package dragoncrafted87.vpp.mixin.client;

import dragoncrafted87.vpp.bags.BaseBagItem;
import dragoncrafted87.vpp.bags.BaseBagItem.BagType;
import dragoncrafted87.vpp.bags.InventoryUtility;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.client.gui.screens.inventory.InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractContainerScreen<InventoryMenu> {
    private InventoryScreenMixin() {
        super(null, null, Component.empty());
    }

    @Override
    protected boolean hasClickedOutside(double mouseX, double mouseY, int left, int top) {
        boolean outsideMain = super.hasClickedOutside(mouseX, mouseY, left, top);
        if (!outsideMain) {
            return false;
        }
        if (this.minecraft != null && this.minecraft.player != null) {
            ItemStack backStack = InventoryUtility.findBagItem(this.minecraft.player, BagType.SATCHEL, false);
            if (!backStack.isEmpty()) {
                BaseBagItem bagItem = (BaseBagItem) backStack.getItem();
                int slots = bagItem.getSlotCount();
                int rows = (int) Math.ceil(slots / 9.0);
                if (mouseY >= (top + this.imageHeight) && mouseY < (top + this.imageHeight) + 8 + (18 * rows)
                        && mouseX >= left && mouseX < (left + this.imageWidth)) {
                    return false;
                }
            }
            ItemStack leftPouchStack = InventoryUtility.findBagItem(this.minecraft.player, BagType.POUCH, false);
            if (!leftPouchStack.isEmpty()) {
                BaseBagItem bagItem = (BaseBagItem) leftPouchStack.getItem();
                int slots = bagItem.getSlotCount();
                int columns = (int) Math.ceil(slots / 3.0);
                if (mouseX >= left - (columns * 18) && mouseX < left && mouseY >= (top + this.imageHeight) - 90
                        && mouseY < (top + this.imageHeight) - 22) {
                    return false;
                }
            }
            ItemStack rightPouchStack = InventoryUtility.findBagItem(this.minecraft.player, BagType.POUCH, true);
            if (!rightPouchStack.isEmpty()) {
                BaseBagItem bagItem = (BaseBagItem) rightPouchStack.getItem();
                int slots = bagItem.getSlotCount();
                int columns = (int) Math.ceil(slots / 3.0);
                if (mouseX >= (left + this.imageWidth) && mouseX < (left + this.imageWidth) + (columns * 18)
                        && mouseY >= (top + this.imageHeight) - 90
                        && mouseY < (top + this.imageHeight) - 22) {
                    return false;
                }
            }
        }
        return true;
    }

    @Inject(method = "extractBackground", at = @At("TAIL"))
    private void vpp$drawBagSlots(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();
        if (client.player != null) {
            int screenX = this.leftPos;
            int screenY = this.topPos;
            int imageWidth = this.imageWidth;
            int imageHeight = this.imageHeight;
            // Draw satchel row
            ItemStack backStack = InventoryUtility.findBagItem(client.player, BagType.SATCHEL, false);
            if (!backStack.isEmpty()) {
                BaseBagItem bagItem = (BaseBagItem) backStack.getItem();
                int slots = bagItem.getSlotCount();
                int x = screenX;
                int y = screenY + imageHeight - 3;
                context.blit(RenderPipelines.GUI_TEXTURED, InventoryUtility.SLOT_TEXTURE, x, y, 0.0f, 32.0f,
                        imageWidth, 4, 256, 256);
                y += 4;
                float u = 0;
                float v = 36;
                for (int slot = 0; slot < slots; slot++) {
                    if (slot % 9 == 0) {
                        x = screenX;
                        u = 0;
                        context.blit(RenderPipelines.GUI_TEXTURED, InventoryUtility.SLOT_TEXTURE, x, y, u, v, 7,
                                18, 256, 256);
                        x += 7;
                        u += 7;
                    }
                    context.blit(RenderPipelines.GUI_TEXTURED, InventoryUtility.SLOT_TEXTURE, x, y, u, v, 18, 18,
                            256, 256);
                    x += 18;
                    u += 18;
                    if ((slot + 1) % 9 == 0) {
                        context.blit(RenderPipelines.GUI_TEXTURED, InventoryUtility.SLOT_TEXTURE, x, y, u, v, 7,
                                18, 256, 256);
                        y += 18;
                    }
                }
                x = screenX;
                context.blit(RenderPipelines.GUI_TEXTURED, InventoryUtility.SLOT_TEXTURE, x, y, 0.0f, 54.0f,
                        imageWidth, 7, 256, 256);
            }
            // Draw left pouch slots
            ItemStack leftPouchStack = InventoryUtility.findBagItem(client.player, BagType.POUCH, false);
            if (!leftPouchStack.isEmpty()) {
                BaseBagItem bagItem = (BaseBagItem) leftPouchStack.getItem();
                int slots = bagItem.getSlotCount();
                int columns = (int) Math.ceil(slots / 3);
                int x = screenX;
                int y = screenY + 137;
                context.blit(RenderPipelines.GUI_TEXTURED, InventoryUtility.SLOT_TEXTURE, x, y, 18.0f, 25.0f, 7,
                        7, 256, 256);
                for (int i = 0; i < columns; i++) {
                    x -= 11;
                    context.blit(RenderPipelines.GUI_TEXTURED, InventoryUtility.SLOT_TEXTURE, x, y, 7.0f, 25.0f,
                            11, 7, 256, 256);
                }
                if (columns > 1) {
                    for (int i = 0; i < columns - 1; i++) {
                        x -= 7;
                        context.blit(RenderPipelines.GUI_TEXTURED, InventoryUtility.SLOT_TEXTURE, x, y, 7.0f,
                                25.0f, 7, 7, 256, 256);
                    }
                }
                x -= 7;
                context.blit(RenderPipelines.GUI_TEXTURED, InventoryUtility.SLOT_TEXTURE, x, y, 0.0f, 25.0f, 7, 7,
                        256, 256);
                x = screenX + 7;
                y -= 54;
                for (int slot = 0; slot < slots; slot++) {
                    if (slot % 3 == 0) {
                        x -= 18;
                        y += 54;
                    }
                    y -= 18;
                    context.blit(RenderPipelines.GUI_TEXTURED, InventoryUtility.SLOT_TEXTURE, x, y, 7.0f, 7.0f,
                            18, 18, 256, 256);
                }
                x -= 7;
                y += 54;
                for (int i = 0; i < 3; i++) {
                    y -= 18;
                    context.blit(RenderPipelines.GUI_TEXTURED, InventoryUtility.SLOT_TEXTURE, x, y, 0.0f, 7.0f, 7,
                            18, 256, 256);
                }
                x = screenX;
                y -= 7;
                context.blit(RenderPipelines.GUI_TEXTURED, InventoryUtility.SLOT_TEXTURE, x, y, 18.0f, 0.0f, 7, 7,
                        256, 256);
                for (int i = 0; i < columns; i++) {
                    x -= 11;
                    context.blit(RenderPipelines.GUI_TEXTURED, InventoryUtility.SLOT_TEXTURE, x, y, 7.0f, 0.0f,
                            11, 7, 256, 256);
                }
                if (columns > 1) {
                    for (int i = 0; i < columns - 1; i++) {
                        x -= 7;
                        context.blit(RenderPipelines.GUI_TEXTURED, InventoryUtility.SLOT_TEXTURE, x, y, 7.0f,
                                0.0f, 7, 7, 256, 256);
                    }
                }
                x -= 7;
                context.blit(RenderPipelines.GUI_TEXTURED, InventoryUtility.SLOT_TEXTURE, x, y, 0.0f, 0.0f, 7, 7,
                        256, 256);
            }
            // Draw right pouch slots (similar logic)
            ItemStack rightPouchStack = InventoryUtility.findBagItem(client.player, BagType.POUCH, true);
            if (!rightPouchStack.isEmpty()) {
                BaseBagItem bagItem = (BaseBagItem) rightPouchStack.getItem();
                int slots = bagItem.getSlotCount();
                int columns = (int) Math.ceil(slots / 3);
                int x = screenX + imageWidth - 7;
                int y = screenY + 137;
                context.blit(RenderPipelines.GUI_TEXTURED, InventoryUtility.SLOT_TEXTURE, x, y, 25.0f, 25.0f, 7,
                        7, 256, 256);
                x += 7;
                for (int i = 0; i < columns; i++) {
                    context.blit(RenderPipelines.GUI_TEXTURED, InventoryUtility.SLOT_TEXTURE, x, y, 7.0f, 25.0f,
                            11, 7, 256, 256);
                    x += 11;
                }
                if (columns > 1) {
                    for (int i = 0; i < columns - 1; i++) {
                        context.blit(RenderPipelines.GUI_TEXTURED, InventoryUtility.SLOT_TEXTURE, x, y, 7.0f,
                                25.0f, 7, 7, 256, 256);
                        x += 7;
                    }
                }
                context.blit(RenderPipelines.GUI_TEXTURED, InventoryUtility.SLOT_TEXTURE, x, y, 32.0f, 25.0f, 7,
                        7, 256, 256);
                x = screenX + imageWidth - 25;
                y -= 54;
                for (int slot = 0; slot < slots; slot++) {
                    if (slot % 3 == 0) {
                        x += 18;
                        y += 54;
                    }
                    y -= 18;
                    context.blit(RenderPipelines.GUI_TEXTURED, InventoryUtility.SLOT_TEXTURE, x, y, 7.0f, 7.0f,
                            18, 18, 256, 256);
                }
                x += 18;
                y += 54;
                for (int i = 0; i < 3; i++) {
                    y -= 18;
                    context.blit(RenderPipelines.GUI_TEXTURED, InventoryUtility.SLOT_TEXTURE, x, y, 32.0f, 7.0f,
                            7, 18, 256, 256);
                }
                x = screenX + imageWidth - 7;
                y -= 7;
                context.blit(RenderPipelines.GUI_TEXTURED, InventoryUtility.SLOT_TEXTURE, x, y, 25.0f, 0.0f, 7, 7,
                        256, 256);
                x += 7;
                for (int i = 0; i < columns; i++) {
                    context.blit(RenderPipelines.GUI_TEXTURED, InventoryUtility.SLOT_TEXTURE, x, y, 7.0f, 0.0f,
                            11, 7, 256, 256);
                    x += 11;
                }
                if (columns > 1) {
                    for (int i = 0; i < columns - 1; i++) {
                        context.blit(RenderPipelines.GUI_TEXTURED, InventoryUtility.SLOT_TEXTURE, x, y, 7.0f,
                                0.0f, 7, 7, 256, 256);
                        x += 7;
                    }
                }
                context.blit(RenderPipelines.GUI_TEXTURED, InventoryUtility.SLOT_TEXTURE, x, y, 32.0f, 0.0f, 7, 7,
                        256, 256);
            }
        }
    }
}
