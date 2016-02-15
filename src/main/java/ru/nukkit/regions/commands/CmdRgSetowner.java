package ru.nukkit.regions.commands;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import ru.nukkit.regions.Regions;
import ru.nukkit.regions.util.Message;
import ru.nukkit.regions.util.StringUtil;

@CmdDefine(command = "region", alias = "rg", subCommands = {"setowner|setown|so","\\S+"} , permission = "regions.setowner", description = Message.RG_SETOWN_DESC)
public class CmdRgSetowner extends Cmd {
    @Override
    public boolean execute(CommandSender sender, Player player, String[] args) {
        //       0          1       2
        // rg setowner <Region> <Player>[, <player>][, <player>]...
        if (!Regions.getManager().isRegion(args[1])) return Message.UNKNOWN_REGION.print(sender,args[1]);
        if (!Regions.getManager().isOwner(player,args[1])) return Message.ONLY_OWNER.print(sender);
        String members = StringUtil.join(args,2);
        return (Regions.getManager().setOwner(args[1], members) ? Message.RG_SETOWN_OK :Message.RG_SETOWN_FAIL).print(sender,args[1], members);
    }
}
