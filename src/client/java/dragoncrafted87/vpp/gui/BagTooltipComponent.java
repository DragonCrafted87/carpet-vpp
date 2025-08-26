package dragoncrafted87.vpp.gui;

import com.google.common.math.IntMath;
import com.mojang.blaze3d.systems.RenderSystem;
import dragoncrafted87.vpp.bags.InventoryUtility;
import dragoncrafted87.vpp.bags.item.BagTooltipData;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

import java.math.RoundingMode;

public class BagTooltipComponent implements TooltipComponent {
    private final DefaultedList<ItemStack> inventory;
    private final int slotCount;
    public BagTooltipComponent(BagTooltipData data) {
        this.inventory = data.inventory();
        this.slotCount = data.slotCount();
    }
    @Override
    public int getHeight() {
        return (18 * IntMath.divide(slotCount, 6, RoundingMode.UP)) + 2;
    }
    @Override
    public int getWidth(TextRenderer textRenderer) {
        return 18 * (slotCount < 6 ? slotCount : 6);
    }
    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, MatrixStack matrices, ItemRenderer itemRenderer, int z) {
        int originalX = x;
        for (int i = 0; i < slotCount; i++) {
            ItemStack itemStack = this.inventory.get(i);
            this.drawSlot(matrices, x, y, z);
            itemRenderer.renderInGuiWithOverrides(itemStack, x + 1, y + 1, i);
            itemRenderer.renderGuiItemOverlay(textRenderer, itemStack, x + 1, y + 1);
            x += 18;
            if ((i + 1) % 6 == 0) {
                y += 18;
                x = originalX;
            }
        }
    }
    private void drawSlot(MatrixStack matrices, int x, int y, int z) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, InventoryUtility.SLOT_TEXTURE);
        DrawableHelper.drawTexture(matrices, x, y, z, 7, 7, 18, 18, 256, 256);
    }
}
