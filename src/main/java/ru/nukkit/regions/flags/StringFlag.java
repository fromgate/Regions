package ru.nukkit.regions.flags;

import cn.nukkit.Player;
import cn.nukkit.utils.TextFormat;
import ru.nukkit.regions.util.Relation;

public class StringFlag extends Flag {

    private String value;

    public StringFlag(FlagType flagType, Relation relation) {
        super(flagType, relation);
        this.value="";
    }

    public StringFlag(FlagType flagType, Relation relation, String param) {
        super(flagType, relation);
        this.parseParam(param);
    }

    @Override
    public boolean parseParam(String parameter) {
        this.value = parameter== null ? "" : new String(parameter);
        return true;
    }

    @Override
    public String getParam() {
        return this.value;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    public void print(Player player, String... str){
        if (player == null) return;
        if (this.value.isEmpty()) return;
        String msg = this.value.replace("%player%",player.getName());
        if (str.length>0)
            for (int i = 0;i<str.length;i++)
                msg = msg.replace("%"+Integer.toString(i+1)+"%",str[i]);
        player.sendMessage(TextFormat.colorize(msg));
    }
}
