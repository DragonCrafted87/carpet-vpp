package dragoncrafted87.vpp.mixin;

import com.google.common.collect.Table;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractContainerMenu.class)
public class ScreenHandlerMixin {
    @Redirect(method = "transferState", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/Table;put(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", remap = false))
    private Object vpp$skipNullInventories1(Table<Container, Integer, Integer> self, Object inventory, Object index,
            Object size) {
        if (inventory == null) {
            return null;
        }
        return self.put((Container) inventory, (int) index, (int) size);
    }

    @Redirect(method = "transferState", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/Table;get(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", remap = false))
    private Object vpp$skipNullInventories2(Table<Container, Integer, Integer> self, Object inventory, Object index) {
        if (inventory == null) {
            return null;
        }
        return self.get(inventory, (int) index);
    }
}
