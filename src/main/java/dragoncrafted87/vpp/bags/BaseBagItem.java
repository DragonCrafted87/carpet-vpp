package dragoncrafted87.vpp.bags;

import dragoncrafted87.vpp.DebugFlags;
import dragoncrafted87.vpp.MinecraftVPP;
import dragoncrafted87.vpp.core.MinecraftVPPNetworking;

import eu.pb4.trinkets.api.TrinketSlotAccess;
import eu.pb4.trinkets.api.TrinketsApi;
import eu.pb4.trinkets.api.callback.TrinketCallback;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

import java.util.List;

public class BaseBagItem extends Item implements TrinketCallback {
    private final int slots;
    private final BagType type;

    public BaseBagItem(Properties settings, int slots, BagType type) {
        super(settings);
        if (type == BagType.SATCHEL && slots > MinecraftVPP.MAX_SATCHEL_SLOTS) {
            throw new IllegalArgumentException("Satchel has too many slots.");
        }
        if (type == BagType.POUCH && slots > MinecraftVPP.MAX_POUCH_SLOTS) {
            throw new IllegalArgumentException("Pouch has too many slots.");
        }
        this.slots = slots;
        this.type = type;
    }

    public int getSlotCount() {
        return this.slots;
    }

    public BagType getType() {
        return this.type;
    }

    public Container getInventory(ItemStack stack) {
        return getInventory(stack, null);
    }

    public Container getInventory(ItemStack stack, Player player) {
        ItemContainerContents comp =
                stack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
        BagInventory inventory = new BagInventory(this.slots, stack, player);
        List<ItemStack> list = comp.itemCopies().toList();
        for (int i = 0; i < this.slots; i++) {
            inventory.setItem(i, i < list.size() ? list.get(i) : ItemStack.EMPTY);
        }
        return inventory;
    }

    @Override
    public void onEquip(ItemStack stack, TrinketSlotAccess slot, LivingEntity entity) {
        updateBagSlotsAndNotify(entity, stack);
    }

    @Override
    public void onUnequip(ItemStack stack, TrinketSlotAccess slot, LivingEntity entity) {
        updateBagSlotsAndNotify(entity, stack);
    }

    private void updateBagSlotsAndNotify(LivingEntity entity, ItemStack stack) {
        if (!(entity instanceof Player player)) return;
        InventoryUtility.updateBagSlots(player);
        if (entity.level().isClientSide()) return;
        if (entity instanceof ServerPlayer serverPlayer) {
            ServerPlayNetworking.send(
                    serverPlayer, new MinecraftVPPNetworking.EnableSlotsPayload());
        }
        if (DebugFlags.DEBUG_BAG_EVENTS) {
            MinecraftVPP.LOGGER.info(
                    "Bag {}: {} by {}",
                    entity.level().isClientSide() ? "client" : "server",
                    stack.getHoverName().getString(),
                    entity.getName().getString());
        }
    }

    @Override
    public boolean canEquip(ItemStack stack, TrinketSlotAccess slot, LivingEntity entity) {
        boolean isReplace = false;
        ItemStack current = slot.get();
        if (current.getItem() instanceof BaseBagItem existing && existing.getType() == this.type) {
            isReplace = true;
        }
        int currentCount =
                TrinketsApi.getAttachment(entity)
                        .equipped(
                                s ->
                                        s.getItem() instanceof BaseBagItem bag
                                                && bag.getType() == this.type,
                                false)
                        .size();
        int maxSlots = (this.type == BagType.SATCHEL ? 1 : 2);
        return isReplace || currentCount < maxSlots;
    }

    public enum BagType {
        SATCHEL,
        POUCH
    }

    private static class BagInventory extends SimpleContainer {
        private final ItemStack bagStack;
        private final Player player;

        public BagInventory(int size, ItemStack bagStack, Player player) {
            super(size);
            this.bagStack = bagStack;
            this.player = player;
        }

        @Override
        public void setChanged() {
            NonNullList<ItemStack> stacks =
                    NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
            for (int i = 0; i < this.getContainerSize(); i++) {
                stacks.set(i, this.getItem(i));
            }
            if (this.player != null && !this.player.level().isClientSide()) {
                bagStack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(stacks));
                if (DebugFlags.DEBUG_BAG_EVENTS) {
                    MinecraftVPP.LOGGER.info(
                            "Updated bag stack component for player {}",
                            player.getName().getString());
                }
            }
            super.setChanged();
        }
    }
}
