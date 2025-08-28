package dragoncrafted87.vpp.core;

import dragoncrafted87.vpp.MinecraftVPP;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public class MinecraftVPPNetworking {
    public static class EnableSlotsPayload implements CustomPayload {
        public static final CustomPayload.Id<EnableSlotsPayload> ID = new CustomPayload.Id<>(
                Identifier.of(MinecraftVPP.MOD_ID, "enable_slots"));
        public static final PacketCodec<PacketByteBuf, EnableSlotsPayload> CODEC = PacketCodec
                .ofStatic(EnableSlotsPayload::write, EnableSlotsPayload::new);

        public EnableSlotsPayload() {
        }

        public EnableSlotsPayload(PacketByteBuf buf) {
        }

        private static void write(PacketByteBuf buf, EnableSlotsPayload payload) {
        }

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }
}
