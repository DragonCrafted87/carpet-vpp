package dragoncrafted87.vpp.gametest;

import dragoncrafted87.vpp.MinecraftVPP;
import dragoncrafted87.vpp.bags.BaseBagItem;

import eu.pb4.trinkets.api.component.TrinketDataComponents;
import eu.pb4.trinkets.api.component.TrinketEquippable;

import net.fabricmc.fabric.api.gametest.v1.CustomTestMethodInvoker;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.lang.reflect.Method;

public class VppGameTest implements CustomTestMethodInvoker {
    @GameTest
    public void breakableVanillaBlocks(GameTestHelper helper) {
        assertDestroyTime(helper, Blocks.BEDROCK, 75.0f);
        assertResistance(helper, Blocks.BEDROCK, 2400.0f);
        assertDestroyTime(helper, Blocks.DEEPSLATE, 1.63f);
        assertDestroyTime(helper, Blocks.END_PORTAL_FRAME, 6.0f);
        assertResistance(helper, Blocks.END_PORTAL_FRAME, 2400.0f);
        helper.succeed();
    }

    @GameTest
    public void bagItemsAreRegistered(GameTestHelper helper) {
        assertRegistered(helper, "pouch", MinecraftVPP.POUCH);
        assertRegistered(helper, "upgraded_pouch", MinecraftVPP.UPGRADED_POUCH);
        assertRegistered(helper, "satchel", MinecraftVPP.SATCHEL);
        assertRegistered(helper, "upgraded_satchel", MinecraftVPP.UPGRADED_SATCHEL);
        assertRegistered(helper, "satchel_strap", MinecraftVPP.SATCHEL_STRAP);
        assertRegistered(helper, "bag_upgrade", MinecraftVPP.BAG_UPGRADE);
        assertTrinketSlot(helper, MinecraftVPP.POUCH, "legs/pouch");
        assertTrinketSlot(helper, MinecraftVPP.SATCHEL, "chest/satchel");
        assertBagStoresContents(helper, MinecraftVPP.POUCH);
        helper.succeed();
    }

    @Override
    public void invokeTestMethod(GameTestHelper context, Method method)
            throws ReflectiveOperationException {
        method.invoke(this, context);
    }

    private static void assertBagStoresContents(GameTestHelper helper, BaseBagItem bag) {
        ItemStack stack = new ItemStack(bag);
        NonNullList<ItemStack> stored = NonNullList.withSize(bag.getSlotCount(), ItemStack.EMPTY);
        stored.set(0, new ItemStack(Items.DIAMOND, 2));
        stack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(stored));
        Container inventory = bag.getInventory(stack);
        if (!inventory.getItem(0).is(Items.DIAMOND) || inventory.getItem(0).getCount() != 2) {
            throw helper.assertionException("bag did not read its stored diamond stack");
        }
        if (!inventory.getItem(1).isEmpty()) {
            throw helper.assertionException("empty bag slot was not empty");
        }
    }

    private static void assertTrinketSlot(GameTestHelper helper, BaseBagItem bag, String slot) {
        TrinketEquippable equipment = new ItemStack(bag).get(TrinketDataComponents.EQUIPMENT);
        if (equipment == null || !equipment.allowedSlots().equals(java.util.List.of(slot))) {
            throw helper.assertionException(
                    "%s slots were %s", bag, equipment == null ? null : equipment.allowedSlots());
        }
    }

    private static void assertDestroyTime(GameTestHelper helper, Block block, float expected) {
        float actual = block.defaultDestroyTime();
        if (actual != expected) {
            throw helper.assertionException(
                    "%s destroy time was %s, expected %s", block, actual, expected);
        }
    }

    private static void assertResistance(GameTestHelper helper, Block block, float expected) {
        float actual = block.getExplosionResistance();
        if (actual != expected) {
            throw helper.assertionException(
                    "%s explosion resistance was %s, expected %s", block, actual, expected);
        }
    }

    private static void assertRegistered(GameTestHelper helper, String path, Item expected) {
        Identifier id = Identifier.fromNamespaceAndPath(MinecraftVPP.MOD_ID, path);
        Item registered = BuiltInRegistries.ITEM.getOptional(id).orElse(null);
        if (registered != expected) {
            throw helper.assertionException("registry item %s was %s", id, registered);
        }
    }
}
