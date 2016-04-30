package ru.nukkit.regions.commands.select;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.permission.Permission;
import ru.nukkit.regions.Regions;
import ru.nukkit.regions.commands.Cmd;
import ru.nukkit.regions.commands.CmdDefine;
import ru.nukkit.regions.util.Message;


@CmdDefine(command = "select", alias = "sel", subCommands = {"(?i)pos1|p1|pos2|p2"}, permission = "regions.select", defaultPerm = Permission.DEFAULT_TRUE, description = Message.SEL_POS_DESC)
public class CmdSelectPos extends Cmd {

    @Override
    public boolean execute(CommandSender sender, Player player, String[] args) {
        boolean selP1;
        if (args[0].equalsIgnoreCase("pos1") || args[0].equalsIgnoreCase("p1")) selP1 = true;
        else if (args[0].equalsIgnoreCase("pos2") || args[0].equalsIgnoreCase("p2")) selP1 = false;
        else return false;
        Regions.getSelector().selectPoint(player, player.getLocation(), selP1);
        return Message.SEL_OK.print(player, '6', player.getLocation(), (selP1 ? "1" : "2"));
    }
}
