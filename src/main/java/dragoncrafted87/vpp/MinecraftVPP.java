package dragoncrafted87.vpp;

import dragoncrafted87.vpp.bags.BaseBagItem;
import dragoncrafted87.vpp.bags.BaseBagItem.BagType;
import dragoncrafted87.vpp.bags.InventoryUtility;
import dragoncrafted87.vpp.beacons.BeaconChunkLoaderData;
import dragoncrafted87.vpp.core.MinecraftVPPNetworking;

import eu.pb4.trinkets.api.component.TrinketDataComponents;
import eu.pb4.trinkets.api.component.TrinketEquippable;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLevelEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class MinecraftVPP implements ModInitializer {
    public static final String MOD_ID = "vpp";
    public static final Logger LOGGER = LoggerFactory.getLogger("MinecraftVPP");
    public static final int MAX_SATCHEL_SLOTS = 18;
    public static final int MAX_POUCH_SLOTS = 6;
    public static final ResourceKey<CreativeModeTab> ITEM_GROUP_KEY =
            ResourceKey.create(
                    Registries.CREATIVE_MODE_TAB,
                    Identifier.fromNamespaceAndPath(MOD_ID, "itemgroup"));
    public static final Item SATCHEL_STRAP = new Item(itemProperties("satchel_strap"));
    public static final Item BAG_UPGRADE = new Item(itemProperties("bag_upgrade"));
    public static final BaseBagItem SATCHEL =
            new BaseBagItem(
                    itemProperties("satchel")
                            .stacksTo(1)
                            .component(TrinketDataComponents.EQUIPMENT, trinket("chest/satchel")),
                    MAX_SATCHEL_SLOTS / 2,
                    BagType.SATCHEL);
    public static final BaseBagItem UPGRADED_SATCHEL =
            new BaseBagItem(
                    itemProperties("upgraded_satchel")
                            .stacksTo(1)
                            .rarity(Rarity.RARE)
                            .component(TrinketDataComponents.EQUIPMENT, trinket("chest/satchel")),
                    MAX_SATCHEL_SLOTS,
                    BagType.SATCHEL);
    public static final BaseBagItem POUCH =
            new BaseBagItem(
                    itemProperties("pouch")
                            .stacksTo(1)
                            .component(TrinketDataComponents.EQUIPMENT, trinket("legs/pouch")),
                    MAX_POUCH_SLOTS / 2,
                    BagType.POUCH);
    public static final BaseBagItem UPGRADED_POUCH =
            new BaseBagItem(
                    itemProperties("upgraded_pouch")
                            .stacksTo(1)
                            .rarity(Rarity.RARE)
                            .component(TrinketDataComponents.EQUIPMENT, trinket("legs/pouch")),
                    MAX_POUCH_SLOTS,
                    BagType.POUCH);
    // A loading ticket keeps the chunk in memory. The beacon and a command
    // block in range only keep ticking when the ticket also simulates, and
    // the dimension stays active when no player is in it.
    public static final TicketType BEACON =
            new TicketType(
                    300,
                    TicketType.FLAG_LOADING
                            | TicketType.FLAG_SIMULATION
                            | TicketType.FLAG_KEEP_DIMENSION_ACTIVE);

    private static Item.Properties itemProperties(String path) {
        return new Item.Properties()
                .setId(
                        ResourceKey.create(
                                Registries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, path)));
    }

    private static TrinketEquippable trinket(String slot) {
        return TrinketEquippable.DEFAULT.withSlots(slot).withEquipOnInteract(true);
    }

    @Override
    public void onInitialize() {
        LOGGER.info("MinecraftVPP initialized successfully");
        Registry.register(
                BuiltInRegistries.ITEM,
                Identifier.fromNamespaceAndPath(MOD_ID, "satchel_strap"),
                SATCHEL_STRAP);
        Registry.register(
                BuiltInRegistries.ITEM,
                Identifier.fromNamespaceAndPath(MOD_ID, "bag_upgrade"),
                BAG_UPGRADE);
        Registry.register(
                BuiltInRegistries.ITEM,
                Identifier.fromNamespaceAndPath(MOD_ID, "satchel"),
                SATCHEL);
        Registry.register(
                BuiltInRegistries.ITEM,
                Identifier.fromNamespaceAndPath(MOD_ID, "upgraded_satchel"),
                UPGRADED_SATCHEL);
        Registry.register(
                BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, "pouch"), POUCH);
        Registry.register(
                BuiltInRegistries.ITEM,
                Identifier.fromNamespaceAndPath(MOD_ID, "upgraded_pouch"),
                UPGRADED_POUCH);
        Registry.register(
                BuiltInRegistries.CREATIVE_MODE_TAB,
                ITEM_GROUP_KEY,
                FabricCreativeModeTab.builder()
                        .title(Component.translatable("itemGroup.vpp.itemgroup"))
                        .icon(() -> new ItemStack(MinecraftVPP.SATCHEL))
                        .build());
        CreativeModeTabEvents.modifyOutputEvent(ITEM_GROUP_KEY)
                .register(
                        content -> {
                            content.accept(SATCHEL_STRAP);
                            content.accept(BAG_UPGRADE);
                            content.accept(SATCHEL);
                            content.accept(UPGRADED_SATCHEL);
                            content.accept(POUCH);
                            content.accept(UPGRADED_POUCH);
                        });
        PayloadTypeRegistry.clientboundPlay()
                .register(
                        MinecraftVPPNetworking.EnableSlotsPayload.ID,
                        MinecraftVPPNetworking.EnableSlotsPayload.CODEC);
        ServerLevelEvents.LOAD.register(
                (server, level) -> {
                    BeaconChunkLoaderData data = BeaconChunkLoaderData.get(level);
                    Set<BlockPos> toRemove = new HashSet<>();
                    for (BlockPos pos : new HashSet<>(data.activeBeacons)) {
                        if (level.getBlockState(pos).is(Blocks.BEACON)) {
                            ChunkPos chunkPos = ChunkPos.containing(pos);
                            level.getChunkSource().addTicketWithRadius(BEACON, chunkPos, 5);
                            if (DebugFlags.DEBUG_BEACON_LOADING) {
                                LOGGER.info(
                                        "Bootstrapped chunk ticket for saved beacon at {} on world"
                                                + " load",
                                        pos.toString());
                            }
                        } else {
                            toRemove.add(pos);
                            if (DebugFlags.DEBUG_BEACON_LOADING) {
                                LOGGER.info(
                                        "Removed stale beacon entry at {} on world load (no longer"
                                                + " a beacon block)",
                                        pos.toString());
                            }
                        }
                    }
                    for (BlockPos pos : toRemove) {
                        data.removeBeacon(pos);
                    }
                });
        Identifier afterPhase = Identifier.fromNamespaceAndPath(MOD_ID, "after");
        ServerPlayConnectionEvents.JOIN.addPhaseOrdering(Event.DEFAULT_PHASE, afterPhase);
        ServerPlayConnectionEvents.JOIN.register(
                afterPhase,
                (handler, sender, server) -> {
                    InventoryUtility.updateBagSlots(handler.player);
                    ServerPlayNetworking.send(
                            handler.player, new MinecraftVPPNetworking.EnableSlotsPayload());
                });
    }
}
