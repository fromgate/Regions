package ru.nukkit.regions.commands.regions;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import ru.nukkit.regions.Regions;
import ru.nukkit.regions.commands.Cmd;
import ru.nukkit.regions.commands.CmdDefine;
import ru.nukkit.regions.util.Message;

@CmdDefine(command = "region", alias = "rg", subCommands ={"remove|rem|delete|del","\\S+"}, permission = "regions.remove", description = Message.RG_REMOVE_DESC)
public class CmdRgRemove extends Cmd {
    @Override
    public boolean execute(CommandSender sender, Player player, String[] args) {
        if (!Regions.getManager().isRegion(args[1])) return Message.UNKNOWN_REGION.print(sender,args[1]);
        if (!Regions.getManager().isOwner(player,args[1])) return Message.ONLY_OWNER.print(sender);
        return (Regions.getManager().removeRegion(args[1]) ? Message.RG_REMOVE_OK : Message.RG_REMOVE_FAIL).print(sender,args[1]);
    }
}
