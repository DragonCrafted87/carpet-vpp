package dragoncrafted87.vpp.core;

import dragoncrafted87.vpp.bags.screen.BagSlot;
import net.minecraft.util.collection.DefaultedList;

public interface MinecraftVPPScreenHandler {
    DefaultedList<BagSlot> vpp$getSatchelSlots();
    DefaultedList<BagSlot> vpp$getLeftPouchSlots();
    DefaultedList<BagSlot> vpp$getRightPouchSlots();
}
