package dragoncrafted87.vpp;

public final class StewHudOffset {
    private StewHudOffset() {}

    public static int hotbarOffset(int guiScaledHeight, boolean creative, boolean spectator) {
        int hotbarOffset = guiScaledHeight - 59;
        if (creative || spectator) {
            hotbarOffset += 14;
        }
        return hotbarOffset;
    }
}
