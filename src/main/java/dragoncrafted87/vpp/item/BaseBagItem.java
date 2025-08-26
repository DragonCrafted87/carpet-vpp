package dragoncrafted87.vpp.item;

import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.Nullable;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketItem;
import dev.emi.trinkets.api.TrinketsApi;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import dragoncrafted87.vpp.DebugFlags;
import dragoncrafted87.vpp.MinecraftVPP;
import dragoncrafted87.vpp.MinecraftVPPNetworking;
import dragoncrafted87.vpp.InventoryUtility;

public class BaseBagItem extends TrinketItem {
    private static final String ITEMS_KEY = "Items";
    private final int slots;
    private final BagType type;

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

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(Text
                .translatable("tooltip.vpp.slots", Text.literal(String.valueOf(this.slots)).formatted(Formatting.BLUE))
                .formatted(Formatting.GRAY));
    }

    public Inventory getInventory(ItemStack stack) {
        SimpleInventory inventory = new SimpleInventory(this.slots) {
            @Override
            public void markDirty() {
                stack.getOrCreateNbt().put(ITEMS_KEY, InventoryUtility.inventoryToTag(this));
                super.markDirty();
            }
        };
        NbtCompound compound = stack.getOrCreateNbt();
        if (!compound.contains(ITEMS_KEY)) {
            compound.put(ITEMS_KEY, new NbtList());
        }
        NbtList items = compound.getList(ITEMS_KEY, 10);
        InventoryUtility.inventoryFromTag(items, inventory);
        return inventory;
    }

    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        DefaultedList<ItemStack> stacks = DefaultedList.of();
        Inventory inventory = getInventory(stack);
        for (int i = 0; i < slots; i++) {
            stacks.add(inventory.getStack(i));
        }
        if (stacks.stream().allMatch(ItemStack::isEmpty))
            return Optional.empty();
        return Optional.of(new BagTooltipData(stacks, slots));
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
        PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());
        if (entity instanceof ServerPlayerEntity serverPlayer) {
            ServerPlayNetworking.send(serverPlayer, MinecraftVPPNetworking.ENABLE_SLOTS, packet);
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
}
