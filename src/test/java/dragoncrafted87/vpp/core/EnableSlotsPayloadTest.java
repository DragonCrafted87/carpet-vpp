package dragoncrafted87.vpp.core;

import io.netty.buffer.Unpooled;

import net.minecraft.network.FriendlyByteBuf;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EnableSlotsPayloadTest {
    @Test
    void payloadIdIsStable() {
        Assertions.assertEquals(
                "vpp:enable_slots", MinecraftVPPNetworking.EnableSlotsPayload.ID.id().toString());
        Assertions.assertEquals(
                MinecraftVPPNetworking.EnableSlotsPayload.ID,
                new MinecraftVPPNetworking.EnableSlotsPayload().type());
    }

    @Test
    void freshInstanceEncodes() {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        MinecraftVPPNetworking.EnableSlotsPayload payload =
                new MinecraftVPPNetworking.EnableSlotsPayload();
        MinecraftVPPNetworking.EnableSlotsPayload.CODEC.encode(buf, payload);
        Assertions.assertEquals(0, buf.readableBytes());
        Assertions.assertEquals(
                payload, MinecraftVPPNetworking.EnableSlotsPayload.CODEC.decode(buf));
    }
}
