package ru.nukkit.regions.flags;

import ru.nukkit.regions.util.Message;
import ru.nukkit.regions.util.Relation;

public class BoolFlag extends Flag {

    Boolean value;

    public BoolFlag(FlagType flagType, Relation relation) {
        super(flagType, relation);
    }

    public BoolFlag(FlagType flagType, Relation relation, boolean value) {
        this(flagType, relation);
        this.value = value;
    }

    @Override
    public boolean parseParam(String parameter) {
        String param = parameter.trim().toLowerCase();
        value = param.matches("(?i)allow|true|yes|on|1");
        return true;
    }

    @Override
    public String getParam() {
        return value ? "allow" : "deny";
    }

    @Override
    public Boolean getValue() {
        return value;
    }

    public void set(String param) {
        this.value = parseParam(param);
    }

    public void set(boolean value) {
        this.value = value;
    }

    /*
       rel: - отношениие игрока к региону (т.е. OWNER, MEMBER или NOT_MEMBER)
     */
    public boolean isAllowed(Relation rel) {
        boolean result = this.getRelation().isRelated(rel) ? value : !value;
        Message.FLAG_DEBUG_RESULT.debug(this.getName(), result);
        return result;
    }
}
