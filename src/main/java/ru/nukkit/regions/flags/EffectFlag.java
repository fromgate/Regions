package ru.nukkit.regions.flags;

import cn.nukkit.potion.Effect;
import ru.nukkit.regions.util.PotionEffects;
import ru.nukkit.regions.util.Relation;

import java.util.ArrayList;
import java.util.List;

public class EffectFlag extends Flag {

    List<Effect> effects;

    public EffectFlag(FlagType flagType, Relation relation) {
        super(flagType, relation);
    }

    @Override
    public boolean parseParam(String parameter) {
        effects = new ArrayList<Effect>();
        if (parameter==null||parameter.isEmpty()) return false;
        String [] ln = parameter.split("\\s+|,\\s*|;\\s*");
        for (String effStr : ln){
            String eStr = effStr;
            int power = 0;
            Effect effect=null;
            if (effStr.contains(":")){
                String[] eln=effStr.split(":");
                eStr = eln[0];
                if (eln[1].matches("\\d+")) power = Integer.parseInt(eln[1]);
            }
            try {
                effect = eStr.matches("\\d+") ? Effect.getEffect(Integer.parseInt(eStr)) : Effect.getEffectByName(eStr);
            } catch (Exception e){}
            if (effect == null) continue;
            effect.setAmplifier(power);
            effect.setAmbient(false);
            effect.setDuration(Integer.MAX_VALUE);
            effects.add (effect);
        }
        return true;
    }

    @Override
    public String getParam() {
        if (effects==null||effects.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (Effect effect : effects){
            String effStr = PotionEffects.effectToStr(effect);
            if (effStr == null) continue;
            if (sb.length()>0) sb.append(", ");
            sb.append(effStr);
        }
        return sb.toString();
    }

    @Override
    public List<Effect> getValue() {
        return this.effects;
    }
}
