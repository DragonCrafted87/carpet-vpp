package dragoncrafted87.vpp.bags;

import dragoncrafted87.vpp.DebugFlags;
import dragoncrafted87.vpp.MinecraftVPP;
import dragoncrafted87.vpp.core.MinecraftVPPNetworking;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketItem;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import java.util.List;
import java.util.Optional;

public class BaseBagItem extends TrinketItem {
    private int slots;
    private BagType type;

    public BaseBagItem(Settings settings, int slots, BagType type) {
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

    public Inventory getInventory(ItemStack stack) {
        return getInventory(stack, null);
    }

    public Inventory getInventory(ItemStack stack, PlayerEntity player) {
        ContainerComponent comp = stack.getOrDefault(DataComponentTypes.CONTAINER, ContainerComponent.DEFAULT);
        BagInventory inventory = new BagInventory(this.slots, stack, player);
        List<ItemStack> list = comp.stream().toList();
        for (int i = 0; i < this.slots; i++) {
            inventory.setStack(i, i < list.size() ? list.get(i) : ItemStack.EMPTY);
        }
        return inventory;
    }

    @Override
    public void onEquip(ItemStack stack, SlotReference slotRef, LivingEntity entity) {
        updateBagSlotsAndNotify(entity, stack);
    }

    @Override
    public void onUnequip(ItemStack stack, SlotReference slotRef, LivingEntity entity) {
        updateBagSlotsAndNotify(entity, stack);
    }

    private void updateBagSlotsAndNotify(LivingEntity entity, ItemStack stack) {
        if (!(entity instanceof PlayerEntity player))
            return;
        InventoryUtility.updateBagSlots(player);
        if (entity.getWorld().isClient)
            return;
        if (entity instanceof ServerPlayerEntity serverPlayer) {
            ServerPlayNetworking.send(serverPlayer, new MinecraftVPPNetworking.EnableSlotsPayload());
        }
        if (DebugFlags.DEBUG_BAG_EVENTS) {
            MinecraftVPP.LOGGER.info("Bag {}: {} by {}", entity.getWorld().isClient ? "client" : "server",
                    stack.getName().getString(),
                    entity.getName().getString());
        }
    }

    @Override
    public boolean canEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        boolean isReplace = false;
        ItemStack current = slot.inventory().getStack(slot.index());
        if (current.getItem() instanceof BaseBagItem existing && existing.getType() == this.type) {
            isReplace = true;
        }
        int currentCount = 0;
        Optional<TrinketComponent> opt = TrinketsApi.getTrinketComponent(entity);
        if (opt.isPresent()) {
            TrinketComponent comp = opt.get();
            currentCount = comp.getEquipped(s -> s.getItem() instanceof BaseBagItem b && b.getType() == this.type)
                    .size();
        }
        int maxSlots = (this.type == BagType.SATCHEL ? 1 : 2);
        return isReplace || currentCount < maxSlots;
    }

    public enum BagType {
        SATCHEL,
        POUCH
    }

    private static class BagInventory extends SimpleInventory {
        private final ItemStack bagStack;
        private final PlayerEntity player;

        public BagInventory(int size, ItemStack bagStack, PlayerEntity player) {
            super(size);
            this.bagStack = bagStack;
            this.player = player;
        }

        @Override
        public void markDirty() {
            DefaultedList<ItemStack> stacks = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
            for (int i = 0; i < this.size(); i++) {
                stacks.set(i, this.getStack(i));
            }
            if (this.player != null && !this.player.getWorld().isClient) {
                bagStack.set(DataComponentTypes.CONTAINER, ContainerComponent.fromStacks(stacks));
                if (DebugFlags.DEBUG_BAG_EVENTS) {
                    MinecraftVPP.LOGGER.info("Updated bag stack component for player {}",
                            player.getName().getString());
                }
            }
            super.markDirty();
        }
    }
}
