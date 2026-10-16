package dragoncrafted87.vpp.bags.item;

import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public record BagTooltipData(NonNullList<ItemStack> inventory, int slotCount) implements TooltipComponent {
}
