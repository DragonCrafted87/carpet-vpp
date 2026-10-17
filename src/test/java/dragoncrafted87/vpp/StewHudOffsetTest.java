package dragoncrafted87.vpp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StewHudOffsetTest {
    @Test
    void survivalAndAdventureStayOnTheStatusBarLine() {
        Assertions.assertEquals(100, StewHudOffset.hotbarOffset(159, false, false));
    }

    @Test
    void creativeMovesClearOfTheHotbar() {
        Assertions.assertEquals(114, StewHudOffset.hotbarOffset(159, true, false));
    }

    @Test
    void spectatorMovesClearOfTheHotbar() {
        Assertions.assertEquals(114, StewHudOffset.hotbarOffset(159, false, true));
    }
}
