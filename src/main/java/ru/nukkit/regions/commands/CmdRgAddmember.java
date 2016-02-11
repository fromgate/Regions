package ru.nukkit.regions.commands;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import ru.nukkit.regions.Regions;
import ru.nukkit.regions.util.Message;
import ru.nukkit.regions.util.StringUtil;


@CmdDefine(command = "region", alias = "rg", subCommands = {"addmember|addmem|am","\\S+"} , permission = "regions.addmember", description = Message.RG_SETOWN_DESC)
public class CmdRgAddmember extends Cmd {
    @Override
    public boolean execute(CommandSender sender, Player player, String[] args) {
        if (Regions.getManager().isRegion(args[1])) return Message.UNKNOWN_REGION.print(sender);
        if (!Regions.getManager().isMember(player,args[1])) return Message.ONLY_OWNER.print(sender);
        String owners = StringUtil.join(args,2);
        return (Regions.getManager().addMember(args[1], owners) ? Message.RG_ADDMBMR_OK :Message.RG_ADDMBMR_FAIL).print(sender,args[1], owners);
    }
}
