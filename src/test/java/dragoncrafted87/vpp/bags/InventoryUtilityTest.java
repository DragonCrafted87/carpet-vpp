package dragoncrafted87.vpp.bags;

import net.minecraft.SharedConstants;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.inventory.Slot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

public class InventoryUtilityTest {
    @BeforeAll
    static void bootMinecraft() {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
    }

    @Test
    void alignMenuSlotIndicesRewritesIdsToListPositions() {
        Slot first = new Slot(null, 0, 0, 0);
        Slot second = new Slot(null, 1, 18, 0);
        Slot third = new Slot(null, 2, 36, 0);
        first.index = 5;
        second.index = 6;
        third.index = 9;

        InventoryUtility.alignMenuSlotIndices(List.of(first, second, third));

        Assertions.assertEquals(0, first.index);
        Assertions.assertEquals(1, second.index);
        Assertions.assertEquals(2, third.index);

        InventoryUtility.alignMenuSlotIndices(List.of(first, second, third));

        Assertions.assertEquals(0, first.index);
        Assertions.assertEquals(1, second.index);
        Assertions.assertEquals(2, third.index);
    }

    @Test
    void alignMenuSlotIndicesLeavesAnEmptyListAlone() {
        InventoryUtility.alignMenuSlotIndices(List.of());
    }
}
