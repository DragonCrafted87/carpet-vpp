package dragoncrafted87.vpp;

import dragoncrafted87.vpp.DebugFlags;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.StringHelper;
import net.minecraft.util.Formatting;
import java.util.ArrayList;
import java.util.List;

public class StewInfo {
    static MinecraftClient minecraft = MinecraftClient.getInstance();

    public static void onInjectTooltip(Object stackIn, List<Text> list) {
        ItemStack stack = (ItemStack) stackIn;
        list.addAll(getStewEffectTexts(stack));
        if (!list.isEmpty() && DebugFlags.DEBUG_STEW_INFO) {
            MinecraftVPP.LOGGER.info("Added stew effects to tooltip for item: {}", stack.getName().getString());
        }
    }

    public static List<Text> getStewEffectTexts(ItemStack stack) {
        List<Text> effectsText = new ArrayList<>();
        if (stack != null && stack.getItem() == Items.SUSPICIOUS_STEW) {
            NbtCompound tag = stack.getNbt();
            if (tag != null) {
                NbtList effects = tag.getList("Effects", 10);
                for (int i = 0; i < effects.size(); i++) {
                    tag = effects.getCompound(i);
                    int duration = tag.getInt("EffectDuration");
                    StatusEffect effect = StatusEffect.byRawId(tag.getByte("EffectId"));
                    if (effect != null) {  // Null-check for safety
                        String time = StringHelper.formatTicks(duration);
                        effectsText.add(Text.translatable(effect.getTranslationKey())
                                .append(" " + time)
                                .setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
                    }
                }
            }
        }
        return effectsText;
    }
}
