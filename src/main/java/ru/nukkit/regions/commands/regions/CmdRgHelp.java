package ru.nukkit.regions.commands.regions;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import ru.nukkit.regions.commands.Cmd;
import ru.nukkit.regions.commands.CmdDefine;
import ru.nukkit.regions.commands.Commander;
import ru.nukkit.regions.util.Message;

@CmdDefine(command = "region", alias = "rg", subCommands = "help|hlp|\\?", permission = "regions.help", description = Message.RG_HELP_DESC, allowConsole = true)
public class CmdRgHelp extends Cmd {
    @Override
    public boolean execute(CommandSender sender, Player player, String[] args) {
        int page = args.length>1 &&args[1].matches("\\d+")? Integer.parseInt(args[1]) :1;
        Commander.printHelp(sender,page);
        return true;
    }
}
