package dragoncrafted87.vpp.bags.item;

import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

public record BagTooltipData(DefaultedList<ItemStack> inventory, int slotCount) implements TooltipData {
}
