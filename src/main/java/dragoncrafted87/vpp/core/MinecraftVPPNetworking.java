package dragoncrafted87.vpp.core;

import dragoncrafted87.vpp.MinecraftVPP;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public class MinecraftVPPNetworking {
    public record EnableSlotsPayload() implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<EnableSlotsPayload> ID =
                new CustomPacketPayload.Type<>(
                        Identifier.fromNamespaceAndPath(MinecraftVPP.MOD_ID, "enable_slots"));
        public static final StreamCodec<FriendlyByteBuf, EnableSlotsPayload> CODEC =
                StreamCodec.unit(new EnableSlotsPayload());

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return ID;
        }
    }
}
