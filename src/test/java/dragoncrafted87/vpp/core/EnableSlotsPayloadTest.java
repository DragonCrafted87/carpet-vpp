package dragoncrafted87.vpp.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EnableSlotsPayloadTest {
    @Test
    void payloadIdIsStable() {
        Assertions.assertEquals("vpp:enable_slots", MinecraftVPPNetworking.EnableSlotsPayload.ID.id().toString());
        Assertions.assertEquals(MinecraftVPPNetworking.EnableSlotsPayload.ID,
                new MinecraftVPPNetworking.EnableSlotsPayload().type());
    }
}
