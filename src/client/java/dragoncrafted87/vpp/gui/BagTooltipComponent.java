package dragoncrafted87.vpp.gui;

import com.google.common.math.IntMath;
import dragoncrafted87.vpp.bags.InventoryUtility;
import dragoncrafted87.vpp.bags.item.BagTooltipData;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.RenderLayer;
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
    public int getHeight(TextRenderer textRenderer) {
        return (18 * IntMath.divide(slotCount, 6, RoundingMode.UP)) + 2;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return 18 * (slotCount < 6 ? slotCount : 6);
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, int width, int height, DrawContext context) {
        int originalX = x;
        // Draw all slots first
        int tx = x;
        int ty = y;
        for (int i = 0; i < slotCount; i++) {
            this.drawSlot(context, tx, ty);
            tx += 18;
            if ((i + 1) % 6 == 0) {
                ty += 18;
                tx = originalX;
            }
        }
        // Draw all items
        context.getMatrices().push();
        context.getMatrices().translate(0.0f, 0.0f, 150.0f);
        tx = x;
        ty = y;
        for (int i = 0; i < slotCount; i++) {
            ItemStack itemStack = this.inventory.get(i);
            context.drawItem(itemStack, tx + 1, ty + 1);
            tx += 18;
            if ((i + 1) % 6 == 0) {
                ty += 18;
                tx = originalX;
            }
        }
        context.getMatrices().pop();
        // Draw all stack counts
        context.getMatrices().push();
        context.getMatrices().translate(0.0f, 0.0f, 250.0f);
        tx = x;
        ty = y;
        for (int i = 0; i < slotCount; i++) {
            ItemStack itemStack = this.inventory.get(i);
            if (itemStack.getCount() > 1) {
                String count = String.valueOf(itemStack.getCount());
                context.drawTextWithShadow(textRenderer, count, tx + 19 - 2 - textRenderer.getWidth(count), ty + 6 + 3,
                        16777215);
            }
            tx += 18;
            if ((i + 1) % 6 == 0) {
                ty += 18;
                tx = originalX;
            }
        }
        context.getMatrices().pop();
    }

    private void drawSlot(DrawContext context, int x, int y) {
        context.drawTexture(RenderLayer::getGuiTextured, InventoryUtility.SLOT_TEXTURE, x, y, 7.0f, 7.0f, 18, 18, 256, 256);
    }
}
