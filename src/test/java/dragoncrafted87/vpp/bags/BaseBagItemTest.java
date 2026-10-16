package dragoncrafted87.vpp.bags;

import dragoncrafted87.vpp.MinecraftVPP;
import dragoncrafted87.vpp.bags.BaseBagItem.BagType;
import net.minecraft.SharedConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.item.Item;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class BaseBagItemTest {
    @BeforeAll
    static void bootMinecraft() {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
    }

    @Test
    void slotCountsMatchTheBagTiers() {
        Assertions.assertEquals(MinecraftVPP.MAX_POUCH_SLOTS / 2, MinecraftVPP.POUCH.getSlotCount());
        Assertions.assertEquals(MinecraftVPP.MAX_POUCH_SLOTS, MinecraftVPP.UPGRADED_POUCH.getSlotCount());
        Assertions.assertEquals(MinecraftVPP.MAX_SATCHEL_SLOTS / 2, MinecraftVPP.SATCHEL.getSlotCount());
        Assertions.assertEquals(MinecraftVPP.MAX_SATCHEL_SLOTS, MinecraftVPP.UPGRADED_SATCHEL.getSlotCount());
        Assertions.assertEquals(BagType.POUCH, MinecraftVPP.POUCH.getType());
        Assertions.assertEquals(BagType.SATCHEL, MinecraftVPP.SATCHEL.getType());
    }

    @Test
    void rejectsABagWithTooManySlots() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new BaseBagItem(
                new Item.Properties().setId(itemKey("too_many_pouch")),
                MinecraftVPP.MAX_POUCH_SLOTS + 1,
                BagType.POUCH));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new BaseBagItem(
                new Item.Properties().setId(itemKey("too_many_satchel")),
                MinecraftVPP.MAX_SATCHEL_SLOTS + 1,
                BagType.SATCHEL));
    }

    private static ResourceKey<Item> itemKey(String path) {
        return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("vpp", path));
    }
}
