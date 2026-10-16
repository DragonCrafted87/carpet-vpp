package dragoncrafted87.vpp.mixin;

import dragoncrafted87.vpp.MinecraftVPP;
import dragoncrafted87.vpp.bags.screen.BagSlot;
import dragoncrafted87.vpp.core.MinecraftVPPScreenHandler;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Higher priority to apply after trinkets
@Mixin(value = InventoryMenu.class, priority = 1100)
public abstract class PlayerScreenHandlerMixin extends AbstractContainerMenu
        implements MinecraftVPPScreenHandler {
    protected PlayerScreenHandlerMixin() {
        super(null, 0);
    }

    @Unique public final NonNullList<BagSlot> satchelSlots = NonNullList.create();
    @Unique public final NonNullList<BagSlot> leftPouchSlots = NonNullList.create();
    @Unique public final NonNullList<BagSlot> rightPouchSlots = NonNullList.create();

    @Inject(method = "<init>", at = @At("RETURN"))
    private void vpp$addSlots(
            Inventory inventory, boolean onServer, Player owner, CallbackInfo callbackInfo) {
        MinecraftVPP.LOGGER.info("Adding bag slots to InventoryMenu, onServer: " + onServer);
        int x = 8;
        int y = 168;
        for (int i = 0; i < MinecraftVPP.MAX_SATCHEL_SLOTS; i++) {
            if (i % 9 == 0) {
                x = 8;
            }
            BagSlot slot = new BagSlot(i, x, y);
            satchelSlots.add(slot);
            this.addSlot(slot);
            x += 18;
            if ((i + 1) % 9 == 0) {
                y += 18;
            }
        }
        x = 8;
        y = 66;
        for (int i = 0; i < MinecraftVPP.MAX_POUCH_SLOTS; i++) {
            if (i % 3 == 0) {
                x -= 18;
                y += 54;
            }
            BagSlot slot = new BagSlot(i, x, y);
            leftPouchSlots.add(slot);
            this.addSlot(slot);
            y -= 18;
        }
        x = 152;
        y = 66;
        for (int i = 0; i < MinecraftVPP.MAX_POUCH_SLOTS; i++) {
            if (i % 3 == 0) {
                x += 18;
                y += 54;
            }
            BagSlot slot = new BagSlot(i, x, y);
            rightPouchSlots.add(slot);
            this.addSlot(slot);
            y -= 18;
        }
    }

    @Override
    public final NonNullList<BagSlot> vpp$getSatchelSlots() {
        return satchelSlots;
    }

    @Override
    public final NonNullList<BagSlot> vpp$getLeftPouchSlots() {
        return leftPouchSlots;
    }

    @Override
    public final NonNullList<BagSlot> vpp$getRightPouchSlots() {
        return rightPouchSlots;
    }
}
