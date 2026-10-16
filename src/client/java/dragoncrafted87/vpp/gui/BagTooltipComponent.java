package dragoncrafted87.vpp.gui;

import com.google.common.math.IntMath;
import dragoncrafted87.vpp.bags.InventoryUtility;
import dragoncrafted87.vpp.bags.item.BagTooltipData;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix3x2fStack;

import java.math.RoundingMode;

public class BagTooltipComponent implements ClientTooltipComponent {
    private final NonNullList<ItemStack> inventory;
    private final int slotCount;

    public BagTooltipComponent(BagTooltipData data) {
        this.inventory = data.inventory();
        this.slotCount = data.slotCount();
    }

    @Override
    public int getHeight(Font font) {
        return (18 * IntMath.divide(slotCount, 6, RoundingMode.UP)) + 2;
    }

    @Override
    public int getWidth(Font font) {
        return 18 * (slotCount < 6 ? slotCount : 6);
    }

    @Override
    public void extractImage(Font font, int x, int y, int width, int height, GuiGraphicsExtractor context) {
        int originalX = x;
        int tx = x;
        int ty = y;
        for (int i = 0; i < slotCount; i++) {
            context.blit(RenderPipelines.GUI_TEXTURED, InventoryUtility.SLOT_TEXTURE, tx, ty, 7.0f, 7.0f, 18, 18, 256, 256);
            tx += 18;
            if ((i + 1) % 6 == 0) {
                ty += 18;
                tx = originalX;
            }
        }
        Matrix3x2fStack matrices = context.pose();
        matrices.pushMatrix();
        tx = x;
        ty = y;
        for (int i = 0; i < slotCount; i++) {
            ItemStack itemStack = this.inventory.get(i);
            context.item(itemStack, tx + 1, ty + 1);
            tx += 18;
            if ((i + 1) % 6 == 0) {
                ty += 18;
                tx = originalX;
            }
        }
        matrices.popMatrix();
        matrices.pushMatrix();
        tx = x;
        ty = y;
        for (int i = 0; i < slotCount; i++) {
            ItemStack itemStack = this.inventory.get(i);
            if (itemStack.getCount() > 1) {
                String count = String.valueOf(itemStack.getCount());
                context.text(font, count, tx + 19 - 2 - font.width(count), ty + 6 + 3, 16777215, true);
            }
            tx += 18;
            if ((i + 1) % 6 == 0) {
                ty += 18;
                tx = originalX;
            }
        }
        matrices.popMatrix();
    }
}
