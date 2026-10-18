package dragoncrafted87.vpp.stew;

import net.minecraft.SharedConstants;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.component.SuspiciousStewEffects;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

public class StewHighlightTest {
    @BeforeAll
    static void bootMinecraft() {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
    }

    @Test
    void differentEffectRestartsTheHighlight() {
        Assertions.assertTrue(StewHighlight.effectsDiffer(true, stew(100), true, otherStew()));
    }

    @Test
    void differentDurationRestartsTheHighlight() {
        Assertions.assertTrue(StewHighlight.effectsDiffer(true, stew(100), true, stew(200)));
    }

    @Test
    void sameEffectDoesNotRestartTheHighlight() {
        Assertions.assertFalse(StewHighlight.effectsDiffer(true, stew(100), true, stew(100)));
    }

    @Test
    void aNonStewDoesNotRestartTheHighlight() {
        Assertions.assertFalse(
                StewHighlight.effectsDiffer(true, stew(100), false, SuspiciousStewEffects.EMPTY));
        Assertions.assertFalse(
                StewHighlight.effectsDiffer(false, SuspiciousStewEffects.EMPTY, true, stew(100)));
        Assertions.assertFalse(
                StewHighlight.effectsDiffer(
                        false, SuspiciousStewEffects.EMPTY, false, SuspiciousStewEffects.EMPTY));
    }

    private static SuspiciousStewEffects stew(int duration) {
        return new SuspiciousStewEffects(
                List.of(new SuspiciousStewEffects.Entry(MobEffects.SPEED, duration)));
    }

    private static SuspiciousStewEffects otherStew() {
        return new SuspiciousStewEffects(
                List.of(new SuspiciousStewEffects.Entry(MobEffects.NIGHT_VISION, 100)));
    }
}
