package ru.nukkit.regions.flags;

import ru.nukkit.regions.util.Relation;

public class BoolFlag extends Flag {

    Boolean value;

    public BoolFlag(FlagType flagType, Relation relation) {
        super(flagType, relation);
    }

    public BoolFlag(FlagType flagType, Relation relation, boolean value) {
        this (flagType,relation);
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getName());
        sb.append(" relate: ").append(relation.name());
        sb.append("value: ").append(this.getParam());
        return sb.toString();
    }

    public void set (String param){
        this.value = parseParam(param);
    }

    public void set (boolean value){
        this.value = value;
    }

    /*
       rel: - отношениие игрока к региону (т.е. OWNER, MEMBER или NOT_MEMBER)
     */
    public boolean isAllowed(Relation rel) {
        if (this.getRelation().isRelated(rel)) return value;
        else return !value;
    }
}
