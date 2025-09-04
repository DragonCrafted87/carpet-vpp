package dragoncrafted87.vpp;

import dragoncrafted87.vpp.bags.BaseBagItem;
import dragoncrafted87.vpp.bags.BaseBagItem.BagType;
import dragoncrafted87.vpp.bags.InventoryUtility;
import dragoncrafted87.vpp.beacons.BeaconChunkLoaderData;
import dragoncrafted87.vpp.core.MinecraftVPPNetworking;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class MinecraftVPP implements ModInitializer {
    public static final String MOD_ID = "vpp";
    public static final Logger LOGGER = LoggerFactory.getLogger("MinecraftVPP");
    public static final int MAX_SATCHEL_SLOTS = 18;
    public static final int MAX_POUCH_SLOTS = 6;
    public static RegistryKey<ItemGroup> ITEM_GROUP_KEY = RegistryKey.of(RegistryKeys.ITEM_GROUP,
            Identifier.of(MOD_ID, "itemgroup"));
    public static final Item SATCHEL_STRAP = new Item(
            new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, "satchel_strap"))));
    public static final BaseBagItem SATCHEL = new BaseBagItem(
            new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, "satchel")))
                    .maxCount(1),
            MAX_SATCHEL_SLOTS / 2,
            BagType.SATCHEL);
    public static final BaseBagItem UPGRADED_SATCHEL = new BaseBagItem(
            new Item.Settings()
                    .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, "upgraded_satchel")))
                    .maxCount(1).rarity(Rarity.RARE),
            MAX_SATCHEL_SLOTS,
            BagType.SATCHEL);
    public static final BaseBagItem POUCH = new BaseBagItem(
            new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, "pouch")))
                    .maxCount(1),
            MAX_POUCH_SLOTS / 2, BagType.POUCH);
    public static final BaseBagItem UPGRADED_POUCH = new BaseBagItem(
            new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, "upgraded_pouch")))
                    .maxCount(1).rarity(Rarity.RARE),
            MAX_POUCH_SLOTS,
            BagType.POUCH);
    public static final ChunkTicketType<BlockPos> BEACON = ChunkTicketType.create("vpp_beacon",
            Comparator.comparingLong(BlockPos::asLong), 300); // 15 sec expiry

    @Override
    public void onInitialize() {
        LOGGER.info("MinecraftVPP initialized successfully");
        Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "satchel_strap"), SATCHEL_STRAP);
        Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "satchel"), SATCHEL);
        Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "upgraded_satchel"), UPGRADED_SATCHEL);
        Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "pouch"), POUCH);
        Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "upgraded_pouch"), UPGRADED_POUCH);
        Registry.register(Registries.ITEM_GROUP, ITEM_GROUP_KEY,
                FabricItemGroup.builder()
                        .displayName(Text.translatable("itemGroup.vpp.itemgroup"))
                        .icon(() -> new ItemStack(MinecraftVPP.SATCHEL))
                        .build());
        ItemGroupEvents.modifyEntriesEvent(ITEM_GROUP_KEY).register(content -> {
            content.add(SATCHEL_STRAP);
            content.add(SATCHEL);
            content.add(UPGRADED_SATCHEL);
            content.add(POUCH);
            content.add(UPGRADED_POUCH);
        });
        PayloadTypeRegistry.playS2C().register(MinecraftVPPNetworking.EnableSlotsPayload.ID,
                MinecraftVPPNetworking.EnableSlotsPayload.CODEC);
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
        Identifier afterPhase = Identifier.of(MOD_ID, "after");
        ServerPlayConnectionEvents.JOIN.addPhaseOrdering(Event.DEFAULT_PHASE, afterPhase);
        ServerPlayConnectionEvents.JOIN.register(afterPhase, (handler, sender, server) -> {
            InventoryUtility.updateBagSlots(handler.player);
            sender.sendPacket(new MinecraftVPPNetworking.EnableSlotsPayload());
        });
    }
}
