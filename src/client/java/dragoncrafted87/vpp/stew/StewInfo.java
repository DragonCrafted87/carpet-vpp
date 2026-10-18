package dragoncrafted87.vpp.stew;

import dragoncrafted87.vpp.DebugFlags;
import dragoncrafted87.vpp.MinecraftVPPClient;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringUtil;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.SuspiciousStewEffects;

import java.util.ArrayList;
import java.util.List;

public class StewInfo {
    public static void onInjectTooltip(ItemStack stack, List<Component> list) {
        list.addAll(getStewEffectTexts(stack));
        if (!list.isEmpty() && DebugFlags.DEBUG_STEW_INFO) {
            MinecraftVPPClient.LOGGER.info(
                    "Added stew effects to tooltip for item: {}", stack.getHoverName().getString());
        }
    }

    public static List<Component> getStewEffectTexts(ItemStack stack) {
        List<Component> effectsText = new ArrayList<>();
        if (stack.is(Items.SUSPICIOUS_STEW)) {
            SuspiciousStewEffects comp =
                    stack.getOrDefault(
                            DataComponents.SUSPICIOUS_STEW_EFFECTS, SuspiciousStewEffects.EMPTY);
            for (SuspiciousStewEffects.Entry entry : comp.effects()) {
                MobEffect effect = entry.effect().value();
                int duration = entry.duration();
                if (effect != null) {
                    String time = StringUtil.formatTickDuration(duration, 20.0F);
                    effectsText.add(
                            Component.translatable(effect.getDescriptionId())
                                    .append(" " + time)
                                    .withStyle(ChatFormatting.GRAY));
                }
            }
        }
        return effectsText;
    }
}
