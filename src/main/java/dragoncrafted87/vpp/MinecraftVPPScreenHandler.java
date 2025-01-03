package dragoncrafted87.vpp;

import net.minecraft.util.collection.DefaultedList;
import dragoncrafted87.vpp.screen.BagSlot;

public interface MinecraftVPPScreenHandler {
    DefaultedList<BagSlot> vpp$getSatchelSlots();
    DefaultedList<BagSlot> vpp$getLeftPouchSlots();
    DefaultedList<BagSlot> vpp$getRightPouchSlots();
}
