package dragoncrafted87.vpp.core;

import dragoncrafted87.vpp.bags.screen.BagSlot;
import net.minecraft.core.NonNullList;

public interface MinecraftVPPScreenHandler {
    NonNullList<BagSlot> vpp$getSatchelSlots();

    NonNullList<BagSlot> vpp$getLeftPouchSlots();

    NonNullList<BagSlot> vpp$getRightPouchSlots();
}
