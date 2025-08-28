package dragoncrafted87.vpp.mixin.client;

import dragoncrafted87.vpp.bags.BaseBagItem;
import dragoncrafted87.vpp.bags.item.BagTooltipData;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import java.util.Optional;

@Mixin(BaseBagItem.class)
public abstract class BaseBagItemMixin extends Item {
    public BaseBagItemMixin(Settings settings) {
        super(settings);
    }

    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        BaseBagItem bag = (BaseBagItem) (Object) this;
        DefaultedList<ItemStack> stacks = DefaultedList.of();
        var inventory = bag.getInventory(stack);
        for (int i = 0; i < bag.getSlotCount(); i++) {
            stacks.add(inventory.getStack(i));
        }
        if (stacks.stream().allMatch(ItemStack::isEmpty))
            return Optional.empty();
        return Optional.of(new BagTooltipData(stacks, bag.getSlotCount()));
    }
}
