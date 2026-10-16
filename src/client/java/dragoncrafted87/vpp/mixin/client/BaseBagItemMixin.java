package dragoncrafted87.vpp.mixin.client;

import dragoncrafted87.vpp.bags.BaseBagItem;
import dragoncrafted87.vpp.bags.item.BagTooltipData;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Optional;

@Mixin(BaseBagItem.class)
public abstract class BaseBagItemMixin extends Item {
    public BaseBagItemMixin(Properties settings) {
        super(settings);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        BaseBagItem bag = (BaseBagItem) (Object) this;
        NonNullList<ItemStack> stacks = NonNullList.create();
        var inventory = bag.getInventory(stack);
        for (int i = 0; i < bag.getSlotCount(); i++) {
            stacks.add(inventory.getItem(i));
        }
        if (stacks.stream().allMatch(ItemStack::isEmpty))
            return Optional.empty();
        return Optional.of(new BagTooltipData(stacks, bag.getSlotCount()));
    }
}
