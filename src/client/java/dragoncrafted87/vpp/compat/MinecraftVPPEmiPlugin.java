package dragoncrafted87.vpp.compat;

import dragoncrafted87.vpp.bags.BaseBagItem;
import dragoncrafted87.vpp.bags.BaseBagItem.BagType;
import dragoncrafted87.vpp.bags.InventoryUtility;
import dragoncrafted87.vpp.mixin.client.HandledScreenAccessor;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.widget.Bounds;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.ItemStack;

public class MinecraftVPPEmiPlugin implements EmiPlugin {
    @Override
    public void register(EmiRegistry registry) {
        registry.addExclusionArea(InventoryScreen.class, (screen, consumer) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            ItemStack leftPouchStack = InventoryUtility.findBagItem(client.player, BagType.POUCH, false);
            if (!leftPouchStack.isEmpty()) {
                BaseBagItem bagItem = (BaseBagItem) leftPouchStack.getItem();
                int slots = bagItem.getSlotCount();
                int columns = (int) Math.ceil(slots / 3);
                int x = ((HandledScreenAccessor) screen).getX() - (columns * 18);
                int y = ((HandledScreenAccessor) screen).getY() + 76;
                consumer.accept(new Bounds(x, y, columns * 18, 68));
            }
            ItemStack rightPouchStack = InventoryUtility.findBagItem(client.player, BagType.POUCH, true);
            if (!rightPouchStack.isEmpty()) {
                BaseBagItem bagItem = (BaseBagItem) rightPouchStack.getItem();
                int slots = bagItem.getSlotCount();
                int columns = (int) Math.ceil(slots / 3);
                int x = ((HandledScreenAccessor) screen).getX() + 176;
                int y = ((HandledScreenAccessor) screen).getY() + 76;
                consumer.accept(new Bounds(x, y, columns * 18, 68));
            }
        });
    }
}
