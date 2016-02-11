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

    /*
     Базовый тип. У флага есть параметры: название, кому он назначен, и собственно параметр.
     Т.е. есть полная аналогия с флагами в RA

     /rg define <aaa>
     /rg set flag <flagName> [for:ALL/OWNER/MEMBER] <значение в соотстветсвии с типом> - задает
     /rg add flag <flagName> [for:ALL/OWNER/MEMBER] <значение в соотстветсвии с типом> - дополняет

     /rg add flag TP for:all world,x,y,z
     /rg add flag DAMAGE for:all 10

      */
/*
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("relate:").append(this.relation.name());
        String value = this.getParam();
        sb.append("value:");
        if (value.contains(" ")) sb.append("{").append(value).append("}");
        else sb.append(value);


    }
*/


}
