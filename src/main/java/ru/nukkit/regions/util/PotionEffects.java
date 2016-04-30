package ru.nukkit.regions.util;

import cn.nukkit.Player;
import cn.nukkit.potion.Effect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;


public class PotionEffects {

    public static void setEffects(Player player, List<Effect> effects) {
        for (Effect eff : effects) {
            if (player.hasEffect(eff.getId())) player.removeEffect(eff.getId());
            player.addEffect(eff);
        }
    }

    public static void removeEffects(Player player, List<Effect> effects) {
        for (Effect eff : effects)
            if (player.hasEffect(eff.getId())) player.removeEffect(eff.getId());
    }


    public static String effectToStr(Effect effect) {
        for (Field field : Effect.class.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers())) continue;
            try {
                if (field.getInt(null) != effect.getId()) continue;
            } catch (Exception e) {
                continue;
            }
            return field.getName() + (effect.getAmplifier() > 0 ? ":" + effect.getAmplifier() : "");
        }
        return null;
    }
}
