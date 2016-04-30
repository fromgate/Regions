package ru.nukkit.regions.util;

public enum Relation {
    OWNER("own", 4),
    MEMBER("mem", 3),
    NOT_OWNER("notowner", 2),
    NOT_MEMBER("notmember", 1),
    ALL("everyone", 0);


    private String alias;
    private int priority;

    Relation(String alias, int priority) {
        this.alias = alias;
        this.priority = priority;
    }
    /*
    Это список возможных отношений региона и игрока:
    Игрок может быть собственником региона OWNER - это описывается наличием его в перечне флага OWNER
    Игрок может быть участником региона MEMBER - это описывается в перечение флагов OWNER и MEMBER
    NOT_OWNER - все кроме владельцев (NOT_MEMBER+MEMBER)
    NOT_MEMBER - все посторонние
    ALL - вообще все
     */

    public static Relation getByName(String str) {
        if (str != null)
            for (Relation r : values())
                if (r.name().equalsIgnoreCase(str)) return r;
        return null;
    }

    public boolean isRelated(Relation to) {
        if (to == null) return false;
        switch (this) {
            case OWNER:
                return (to == OWNER);
            case MEMBER:
                return (to == OWNER || to == MEMBER);
            case NOT_OWNER:
                return (to != OWNER);
            case NOT_MEMBER:
                return (to == NOT_MEMBER);
            case ALL:
                return true;
        }
        return false;
    }

}
