package ru.nukkit.regions.commands;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import ru.nukkit.regions.Regions;
import ru.nukkit.regions.util.Message;

@CmdDefine(command = "region", alias = "rg", subCommands = {"removemember|remmember|remmem|rm","\\S+","\\S+"} , permission = "regions.removemember", description = Message.RG_REMMEM_DESC)
public class CmdRgRemoveMember extends Cmd {
    @Override
    public boolean execute(CommandSender sender, Player player, String[] args) {
        if (!Regions.getManager().isRegion(args[1])) return Message.UNKNOWN_REGION.print(sender,args[1]);
        if (!Regions.getManager().isOwner(player,args[1])) return Message.ONLY_OWNER.print(sender);
        return (Regions.getManager().removeMember (args[1], args[2]) ? Message.RG_REMMEM_OK : Message.RG_REMMEM_FAIL).print(sender,args[2],args[1]);
    }
}
