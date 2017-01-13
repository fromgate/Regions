package ru.nukkit.regions.commands.select;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.permission.Permission;
import ru.nukkit.regions.Regions;
import ru.nukkit.regions.commands.Cmd;
import ru.nukkit.regions.commands.CmdDefine;
import ru.nukkit.regions.util.Message;

@CmdDefine(command = "select", alias = "sel,rgsel,rgselect", subCommands = {}, permission = "regions.select", description = Message.SEL_DESC, defaultPerm = Permission.DEFAULT_TRUE)
public class CmdSelect extends Cmd {
    @Override
    public boolean execute(CommandSender sender, Player player, String[] args) {
        if (args.length > 0) return false;
        return (Regions.getSelector().toggleSelMode(player) ? Message.SEL_ENABLE : Message.SEL_DISABLE).print(player);
    }

}

