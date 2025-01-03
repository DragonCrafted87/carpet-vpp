package dragoncrafted87.vpp;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import dragoncrafted87.vpp.item.BaseBagItem;
import dragoncrafted87.vpp.item.BaseBagItem.BagType;

public class MinecraftVPP implements ModInitializer {
    public static final String MOD_ID = "vpp";

    public static final Logger LOGGER = LoggerFactory.getLogger("MinecraftVPP");

    public static final int MAX_SATCHEL_SLOTS = 18;
    public static final int MAX_POUCH_SLOTS = 6;

    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "itemgroup"), () -> new ItemStack(MinecraftVPP.SATCHEL));

    public static final Item SATCHEL_STRAP = new Item(new FabricItemSettings().group(ITEM_GROUP));

    public static final BaseBagItem SATCHEL = new BaseBagItem(new FabricItemSettings().group(MinecraftVPP.ITEM_GROUP).maxCount(1), MAX_SATCHEL_SLOTS / 2, BagType.SATCHEL);
    public static final BaseBagItem UPGRADED_SATCHEL = new BaseBagItem(new FabricItemSettings().group(MinecraftVPP.ITEM_GROUP).maxCount(1).rarity(Rarity.RARE), MAX_SATCHEL_SLOTS, BagType.SATCHEL);
    public static final BaseBagItem POUCH = new BaseBagItem(new FabricItemSettings().group(MinecraftVPP.ITEM_GROUP).maxCount(1), MAX_POUCH_SLOTS / 2, BagType.POUCH);
    public static final BaseBagItem UPGRADED_POUCH = new BaseBagItem(new FabricItemSettings().group(MinecraftVPP.ITEM_GROUP).maxCount(1).rarity(Rarity.RARE), MAX_POUCH_SLOTS, BagType.POUCH);

    @Override
    public void onInitialize() {

        LOGGER.info("ModInitializer");

        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "satchel_strap"), SATCHEL_STRAP);

        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "satchel"), SATCHEL);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "upgraded_satchel"), UPGRADED_SATCHEL);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "pouch"), POUCH);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "upgraded_pouch"), UPGRADED_POUCH);

    }
}
