package ru.nukkit.regions.commands;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Location;
import ru.nukkit.regions.Regions;
import ru.nukkit.regions.util.Message;

import java.util.List;


@CmdDefine(command = "region", alias = "rg", subCommands = {"redefine|update|move","\\S+"} , permission = "regions.redefine", description = Message.RG_REDEFINE_DESC)
public class CmdRgRedefine extends Cmd {
    @Override
    public boolean execute(CommandSender sender, Player player, String[] args) {
        if (!Regions.getManager().isRegion(args[1])) return Message.UNKNOWN_REGION.print(sender,args[1]);
        if (!Regions.getManager().isOwner(player,args[1])) return Message.ONLY_OWNER.print(sender);
        List<Location> locs = Regions.getSelector().getPoints(player);
        if (locs == null||locs.size()!=2) return Message.DEF_SELECT.print(player);
        String id = args[1];
        if (Regions.getManager().defineRegion(id,player.getName(),locs)) {
            Regions.getSelector().setSelMode(player,false);
            Regions.getSelector().clearSelection(player);
            return Message.RG_REDEF_OK.print(sender,id);
        } else return Message.RG_REDEF_FAIL.print(sender,id);
    }
}