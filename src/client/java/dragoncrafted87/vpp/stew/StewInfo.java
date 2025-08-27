package dragoncrafted87.vpp.stew;

import dragoncrafted87.vpp.DebugFlags;
import dragoncrafted87.vpp.MinecraftVPPClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.SuspiciousStewEffectsComponent;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.StringHelper;
import java.util.ArrayList;
import java.util.List;

public class StewInfo {
    public static void onInjectTooltip(Object stackIn, List<Text> list) {
        ItemStack stack = (ItemStack) stackIn;
        list.addAll(getStewEffectTexts(stack));
        if (!list.isEmpty() && DebugFlags.DEBUG_STEW_INFO) {
            MinecraftVPPClient.LOGGER.info("Added stew effects to tooltip for item: {}", stack.getName().getString());
        }
    }
    public static List<Text> getStewEffectTexts(ItemStack stack) {
        List<Text> effectsText = new ArrayList<>();
        if (stack.getItem() == Items.SUSPICIOUS_STEW) {
            SuspiciousStewEffectsComponent comp = stack.getOrDefault(DataComponentTypes.SUSPICIOUS_STEW_EFFECTS, SuspiciousStewEffectsComponent.DEFAULT);
            for (SuspiciousStewEffectsComponent.StewEffect entry : comp.effects()) {
                StatusEffect effect = entry.effect().value();
                int duration = entry.duration();
                if (effect != null) {
                    String time = StringHelper.formatTicks(duration, 20.0F);
                    effectsText.add(Text.translatable(effect.getTranslationKey())
                            .append(" " + time)
                            .formatted(Formatting.GRAY));
                }
            }
        }
        return effectsText;
    }
}
