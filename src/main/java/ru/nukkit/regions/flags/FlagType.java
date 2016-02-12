package ru.nukkit.regions.flags;

import ru.nukkit.regions.util.Relation;

import java.lang.reflect.Constructor;

public enum FlagType {
    BREAK (Relation.MEMBER,true),
    BUILD (Relation.MEMBER,true),
    CHEST (Relation.MEMBER,true),
    PVP (Relation.ALL,true),
    PVE (Relation.ALL,true),        // TODO Oops! There's no mobs in nukkit yet :)
    ENTRY (Relation.ALL,true),       // TODO
    BUTTON (Relation.MEMBER,true),  // TODO Opps! There's no buttons in nukkit yet :)
    LEVER (Relation.MEMBER,true),
    PLATE (Relation.MEMBER,true),
    DOOR (Relation.MEMBER,true),
    TRAPDOOR (Relation.MEMBER,true),
    GATE (Relation.ALL,true),
    LIGHTER (Relation.MEMBER,true), // TODO
    CLAIM (Relation.ALL,false);

    FlagType(Relation relation, boolean allow) {
        this.defaultFlag = new BoolFlag(this,relation,allow);
        this.flagClass = BoolFlag.class;
    }


    // private FlagType parent; // Заморочиться, что ли...
    private Class<? extends Flag> flagClass;
    private final Flag defaultFlag;

    public Flag getDefaultFlag(){
        return this.defaultFlag;
    }

    public Flag createNewFlag(){
        return this.createFlag(this.defaultFlag.relation,this.defaultFlag.getParam());
    }

    public Flag createNewFlag(String value){
        return this.createFlag(this.defaultFlag.relation,value);
    }

    public Flag createNewFlag(Relation relation, String value){
        return this.createFlag(relation == null ? this.defaultFlag.relation : relation,value);
    }

    public Flag createNewFlag(String relStr, String value){
        return createNewFlag(Relation.getByName(relStr),value);
    }


    public static boolean isValidType(String arg) {
        for (FlagType f : values())
            if (f.name().equalsIgnoreCase(arg)) return true;
        return false;
    }

    private Flag createFlag (Relation relation, String valueStr){
        try {
            Constructor constructor = this.flagClass.getConstructor(FlagType.class,Relation.class);
            Flag f = (Flag) constructor.newInstance(this,relation);
            if (f.parseParam(valueStr)) return f;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Flag createFlag(String arg, Relation rel, String value) {
        FlagType ft = getFlagByName(arg);
        if (ft == null) return null;
        return ft.createFlag(rel,value);
    }

    public static FlagType getFlagByName(String flagTypeStr) {
        for (FlagType ft : values())
            if (ft.name().equalsIgnoreCase(flagTypeStr)) return ft;
        return null;
    }

}
