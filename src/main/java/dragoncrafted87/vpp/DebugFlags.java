package dragoncrafted87.vpp;

public class DebugFlags {
    public static final boolean DEBUG_BEACON_LOADING = false; // Logs beacon chunk loading events
    public static final boolean DEBUG_STEW_INFO = false; // Logs suspicious stew tooltip/info events
    // Loom client and server runs pass -Dvpp.debug. A release jar leaves it unset.
    public static final boolean DEBUG_BAG_EVENTS = Boolean.getBoolean("vpp.debug");
}
