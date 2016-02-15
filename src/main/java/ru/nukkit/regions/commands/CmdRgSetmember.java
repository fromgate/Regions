package ru.nukkit.regions.commands;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import ru.nukkit.regions.Regions;
import ru.nukkit.regions.util.Message;
import ru.nukkit.regions.util.StringUtil;

@CmdDefine(command = "region", alias = "rg", subCommands = {"setmember|setmem|sm","\\S+"} , permission = "regions.setmember", description = Message.RG_SETMBMR_DESC)
public class CmdRgSetmember extends Cmd {
    @Override
    public boolean execute(CommandSender sender, Player player, String[] args) {
        if (!Regions.getManager().isRegion(args[1])) return Message.UNKNOWN_REGION.print(sender,args[1]);
        if (!Regions.getManager().isOwner(player,args[1])) return Message.ONLY_OWNER.print(sender);
        String owners = StringUtil.join(args,2);
        return (Regions.getManager().setMember(args[1], owners) ? Message.RG_SETOWN_OK :Message.RG_SETOWN_FAIL).print(sender,args[1], owners);
    }
}
