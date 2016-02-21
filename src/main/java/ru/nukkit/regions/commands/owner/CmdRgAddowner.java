package ru.nukkit.regions.commands.owner;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import ru.nukkit.regions.Regions;
import ru.nukkit.regions.commands.Cmd;
import ru.nukkit.regions.commands.CmdDefine;
import ru.nukkit.regions.util.Message;
import ru.nukkit.regions.util.StringUtil;

@CmdDefine(command = "region", alias = "rg", subCommands = {"addowner|addown|ao","\\S+"} , permission = "regions.addowner", description = Message.RG_SETOWN_DESC)
public class CmdRgAddowner extends Cmd {
    @Override
    public boolean execute(CommandSender sender, Player player, String[] args) {
        if (!Regions.getManager().isRegion(args[1])) return Message.UNKNOWN_REGION.print(sender,args[1]);
        if (!Regions.getManager().isOwner(player,args[1])) return Message.ONLY_OWNER.print(sender);
        String owners = StringUtil.join(args,2);
        return (Regions.getManager().addOwner(args[1], owners) ? Message.RG_ADDOWN_OK :Message.RG_ADDOWN_FAIL).print(sender,args[1], owners);

    }
}
