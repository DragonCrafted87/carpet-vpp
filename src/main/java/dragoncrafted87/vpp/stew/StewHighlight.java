package dragoncrafted87.vpp.stew;

import net.minecraft.world.item.component.SuspiciousStewEffects;

public final class StewHighlight {
    private StewHighlight() {}

    /**
     * Vanilla restarts the held-item name only when the item or the hover name changes. Every
     * suspicious stew shares both, so a new effect has to be detected here.
     */
    public static boolean effectsDiffer(
            boolean selectedIsStew,
            SuspiciousStewEffects selected,
            boolean previousIsStew,
            SuspiciousStewEffects previous) {
        if (!selectedIsStew || !previousIsStew) {
            return false;
        }
        return !selected.equals(previous);
    }
}
