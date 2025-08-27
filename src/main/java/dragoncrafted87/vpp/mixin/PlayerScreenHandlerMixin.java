package dragoncrafted87.vpp.mixin;

import dragoncrafted87.vpp.MinecraftVPP;
import dragoncrafted87.vpp.bags.screen.BagSlot;
import dragoncrafted87.vpp.core.MinecraftVPPScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Higher priority to apply after trinkets
@Mixin(value = PlayerScreenHandler.class, priority = 1100)
public abstract class PlayerScreenHandlerMixin extends ScreenHandler implements MinecraftVPPScreenHandler {
    protected PlayerScreenHandlerMixin() {
        super(null, 0);
    }

    @Shadow
    @Final
    private PlayerEntity owner;
    @Unique
    public final DefaultedList<BagSlot> satchelSlots = DefaultedList.ofSize(MinecraftVPP.MAX_SATCHEL_SLOTS);
    @Unique
    public final DefaultedList<BagSlot> leftPouchSlots = DefaultedList.ofSize(MinecraftVPP.MAX_POUCH_SLOTS);
    @Unique
    public final DefaultedList<BagSlot> rightPouchSlots = DefaultedList.ofSize(MinecraftVPP.MAX_POUCH_SLOTS);

    @Inject(method = "<init>", at = @At("RETURN"))
    private void vpp$addSlots(PlayerInventory inventory, boolean onServer, PlayerEntity owner,
            CallbackInfo callbackInfo) {
        MinecraftVPP.LOGGER.info("Adding bag slots to PlayerScreenHandler, onServer: " + onServer);
        // satchel
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
        // left pouch
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
        // right pouch
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
    public final DefaultedList<BagSlot> vpp$getSatchelSlots() {
        return satchelSlots;
    }

    @Override
    public final DefaultedList<BagSlot> vpp$getLeftPouchSlots() {
        return leftPouchSlots;
    }

    @Override
    public final DefaultedList<BagSlot> vpp$getRightPouchSlots() {
        return rightPouchSlots;
    }
}
