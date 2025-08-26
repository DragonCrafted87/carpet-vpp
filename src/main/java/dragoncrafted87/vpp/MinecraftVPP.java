package dragoncrafted87.vpp;

import dragoncrafted87.vpp.bags.BaseBagItem;
import dragoncrafted87.vpp.bags.BaseBagItem.BagType;
import dragoncrafted87.vpp.bags.InventoryUtility;
import dragoncrafted87.vpp.beacons.BeaconChunkLoaderData;
import dragoncrafted87.vpp.core.MinecraftVPPNetworking;
import dragoncrafted87.vpp.core.MinecraftVPPScreenHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.buffer.Unpooled;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class MinecraftVPP implements ModInitializer {
    public static final String MOD_ID = "vpp";
    public static final Logger LOGGER = LoggerFactory.getLogger("MinecraftVPP");
    public static final int MAX_SATCHEL_SLOTS = 18;
    public static final int MAX_POUCH_SLOTS = 6;
    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "itemgroup"),
            () -> new ItemStack(MinecraftVPP.SATCHEL));
    public static final Item SATCHEL_STRAP = new Item(new FabricItemSettings().group(ITEM_GROUP));
    public static final BaseBagItem SATCHEL = new BaseBagItem(
            new FabricItemSettings().group(MinecraftVPP.ITEM_GROUP).maxCount(1), MAX_SATCHEL_SLOTS / 2,
            BagType.SATCHEL);
    public static final BaseBagItem UPGRADED_SATCHEL = new BaseBagItem(
            new FabricItemSettings().group(MinecraftVPP.ITEM_GROUP).maxCount(1).rarity(Rarity.RARE), MAX_SATCHEL_SLOTS,
            BagType.SATCHEL);
    public static final BaseBagItem POUCH = new BaseBagItem(
            new FabricItemSettings().group(MinecraftVPP.ITEM_GROUP).maxCount(1), MAX_POUCH_SLOTS / 2, BagType.POUCH);
    public static final BaseBagItem UPGRADED_POUCH = new BaseBagItem(
            new FabricItemSettings().group(MinecraftVPP.ITEM_GROUP).maxCount(1).rarity(Rarity.RARE), MAX_POUCH_SLOTS,
            BagType.POUCH);
    public static final ChunkTicketType<BlockPos> BEACON = ChunkTicketType.create("vpp_beacon",
            Comparator.comparingLong(BlockPos::asLong), 300); // 15 sec expiry
    @Override
    public void onInitialize() {
        LOGGER.info("MinecraftVPP initialized successfully");
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "satchel_strap"), SATCHEL_STRAP);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "satchel"), SATCHEL);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "upgraded_satchel"), UPGRADED_SATCHEL);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "pouch"), POUCH);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "upgraded_pouch"), UPGRADED_POUCH);
        ServerWorldEvents.LOAD.register((server, world) -> {
            BeaconChunkLoaderData data = BeaconChunkLoaderData.get(world);
            Set<BlockPos> toRemove = new HashSet<>();
            for (BlockPos pos : new HashSet<>(data.activeBeacons)) { // Copy to avoid concurrent mod
                if (world.getBlockState(pos).isOf(Blocks.BEACON)) {
                    ChunkPos chunkPos = new ChunkPos(pos);
                    world.getChunkManager().addTicket(BEACON, chunkPos, 5, pos); // Bootstrap with max radius
                    if (DebugFlags.DEBUG_BEACON_LOADING) {
                        LOGGER.info("Bootstrapped chunk ticket for saved beacon at {} on world load", pos.toString());
                    }
                } else {
                    toRemove.add(pos);
                    if (DebugFlags.DEBUG_BEACON_LOADING) {
                        LOGGER.info("Removed stale beacon entry at {} on world load (no longer a beacon block)",
                                pos.toString());
                    }
                }
            }
            for (BlockPos pos : toRemove) {
                data.removeBeacon(pos);
            }
        });
        Identifier afterPhase = new Identifier(MOD_ID, "after");
        ServerPlayConnectionEvents.JOIN.addPhaseOrdering(Event.DEFAULT_PHASE, afterPhase);
        ServerPlayConnectionEvents.JOIN.register(afterPhase, (handler, sender, server) -> {
            InventoryUtility.updateBagSlots(handler.player);
            PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());
            sender.sendPacket(MinecraftVPPNetworking.ENABLE_SLOTS, packet);
        });
    }
}
