package ru.nukkit.regions.flags;

import cn.nukkit.Server;
import cn.nukkit.permission.Permission;
import cn.nukkit.utils.Config;
import ru.nukkit.regions.RegionsPlugin;
import ru.nukkit.regions.util.Message;
import ru.nukkit.regions.util.Relation;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;

public enum FlagType {
    BREAK(Relation.MEMBER, true, Permission.DEFAULT_OP),
    BUILD(Relation.MEMBER, true, Permission.DEFAULT_OP),
    CHEST(Relation.MEMBER, true, Permission.DEFAULT_OP),
    PVP(Relation.ALL, true, Permission.DEFAULT_OP),
    PVE(Relation.ALL, true, Permission.DEFAULT_OP),        // TODO Oops! There's no mobs in nukkit yet :)
    ENTRY(Relation.ALL, true, Permission.DEFAULT_OP),
    ENTRYMSG(Relation.ALL, ""),
    LEAVE(Relation.ALL, true, Permission.DEFAULT_OP),
    LEAVEMSG(Relation.ALL, ""),
    BUTTON(Relation.MEMBER, true, Permission.DEFAULT_OP),  // TODO Opps! There's no buttons in nukkit yet :)
    LEVER(Relation.MEMBER, true, Permission.DEFAULT_OP),
    PLATE(Relation.MEMBER, true, Permission.DEFAULT_OP),
    DOOR(Relation.MEMBER, true, Permission.DEFAULT_OP),
    TRAPDOOR(Relation.MEMBER, true, Permission.DEFAULT_OP),
    GATE(Relation.ALL, true, Permission.DEFAULT_OP),
    LIGHTER(Relation.MEMBER, true, Permission.DEFAULT_OP), // TODO
    CLAIM(Relation.ALL, false, Permission.DEFAULT_OP),
    EFFECT(Relation.ALL, "", EffectFlag.class),
    VISUAL(Relation.OWNER, true, Permission.DEFAULT_OP); //Названиеэффекта:Уровень, разделитель - пробел или ", "

    FlagType(Relation relation, boolean allow, String defaultPerm) {
        this(relation, allow);
        Permission permission = new Permission("regions.flag." + this.name().toLowerCase(), defaultPerm);
        Server.getInstance().getPluginManager().addPermission(permission);
        Message.debugMessage("Permission registered:", permission.getName(), defaultPerm);
    }

    FlagType(Relation relation, boolean allow) {
        this.flagClass = BoolFlag.class;
        this.defaultFlag = getDefault(new BoolFlag(this, relation, allow));
    }

    FlagType(Relation relation, String str) {
        this.flagClass = StringFlag.class;
        this.defaultFlag = getDefault(new StringFlag(this, relation, str));
    }

    FlagType(Relation relation, String valueStr, Class<? extends Flag> clazz) {
        this.flagClass = clazz;
        Flag f = createFlag(relation, valueStr);
        this.defaultFlag = getDefault(f);
    }

    private Class<? extends Flag> flagClass;
    private final Flag defaultFlag;

    private static Config cfg = null;

    public Flag getDefaultFlag() {
        return this.defaultFlag;
    }

    public Flag createNewFlag() {
        return this.createFlag(this.defaultFlag.relation, this.defaultFlag.getParam());
    }

    public Flag createNewFlag(String value) {
        return this.createFlag(this.defaultFlag.relation, value);
    }

    public Flag createNewFlag(Relation relation, String value) {
        return this.createFlag(relation == null ? this.defaultFlag.relation : relation, value);
    }

    public Flag createNewFlag(String relStr, String value) {
        return createNewFlag(Relation.getByName(relStr), value);
    }


    public static boolean isValidType(String arg) {
        for (FlagType f : values())
            if (f.name().equalsIgnoreCase(arg)) return true;
        return false;
    }

    private Flag createFlag(Relation relation, String valueStr) {
        try {
            Constructor constructor = this.flagClass.getConstructor(FlagType.class, Relation.class);
            Flag f = (Flag) constructor.newInstance(this, relation);
            f.parseParam(valueStr);
            return f;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Flag createFlag(String arg, Relation rel, String value) {
        FlagType ft = getFlagByName(arg);
        if (ft == null) return null;
        return ft.createFlag(rel, value);
    }

    public static FlagType getFlagByName(String flagTypeStr) {
        for (FlagType ft : values())
            if (ft.name().equalsIgnoreCase(flagTypeStr)) return ft;
        return null;
    }

    private static Flag getDefault(Flag defaultFlag) {
        if (cfg == null) {
            File f = new File(RegionsPlugin.getPlugin().getDataFolder(), "default-flags.yml");
            if (!f.exists()) try {
                f.createNewFile();
            } catch (IOException e) {
            }
            cfg = new Config(f, Config.YAML);
        }
        if (!cfg.exists(defaultFlag.getName())) return defaultFlag;
        return defaultFlag.getType().createNewFlag(
                cfg.getString(defaultFlag.getName() + ".relate", defaultFlag.getRelation().name()),
                cfg.getString(defaultFlag.getName() + ".value", defaultFlag.getParam()));
    }

    public static void createDefaults() {
        if (cfg == null) {
            File f = new File(RegionsPlugin.getPlugin().getDataFolder(), "default-flags.yml");
            if (!f.exists()) try {
                f.createNewFile();
            } catch (IOException e) {
            }
            cfg = new Config(f, Config.YAML);
        }
        for (FlagType ft : values()) {
            cfg.set(ft.name() + ".value", ft.getDefaultFlag().getParam());
            cfg.set(ft.name() + ".relate", ft.getDefaultFlag().getRelation().name());
        }
        cfg.save();
    }
}
