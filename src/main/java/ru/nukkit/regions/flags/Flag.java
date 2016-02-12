package ru.nukkit.regions.flags;

import ru.nukkit.regions.util.Relation;

public abstract class Flag {
    Relation relation;
    FlagType type;

    public Flag (FlagType flagType, Relation relation){
        this.type = flagType;
        this.relation = relation==null ? this.type.getDefaultFlag().relation : relation;
    }

    public abstract boolean parseParam (String parameter); // Это парсер параметров из командной строки (и возможно при загрузке)
                                                           // /rg add flag <Название> [RELATION]
    public abstract String getParam();

    public String getName(){
        return this.type.name();
    }

    public abstract Object getValue(); // возврат какого-то ;) значения флага. По идее типизированного


    public Relation getRelation(){
        return this.relation;
    }

    public FlagType getType(){
        return type;
    }

    public abstract String toString();




}
