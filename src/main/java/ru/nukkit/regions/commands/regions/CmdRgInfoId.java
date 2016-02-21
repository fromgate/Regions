package ru.nukkit.regions.commands.regions;


import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import ru.nukkit.regions.Regions;
import ru.nukkit.regions.commands.Cmd;
import ru.nukkit.regions.commands.CmdDefine;
import ru.nukkit.regions.manager.Region;
import ru.nukkit.regions.flags.Flag;
import ru.nukkit.regions.util.Message;
import ru.nukkit.regions.util.StringUtil;

@CmdDefine(command = "region", alias = "rg", subCommands = {"info|i","\\S+"}, permission = "regions.info", description = Message.RG_INFO_DESC, allowConsole = true)
public class CmdRgInfoId extends Cmd {
    @Override
    public boolean execute(CommandSender sender, Player player, String[] args) {
        Region region = Regions.getManager().getRegion(args[1]);
        if (region == null) return Message.UNKNOWN_REGION.print(sender, args[1]);
        Message.RG_INFO_REGNAME.print(sender,args[1]);
        Message.RG_INFO_COORD.print(sender,region.getDimension());
        if (!region.getOwners().isEmpty()) Message.RG_INFO_OWNERS.print(sender, StringUtil.listToString(region.getOwners()));
        if (!region.getMembers().isEmpty()) Message.RG_INFO_MEMBERS.print(sender,StringUtil.listToString(region.getMembers()));
        if (!region.getFlags().isEmpty())
            for (Flag f : region.getFlags())
            sender.sendMessage(TextFormat.colorize("&a"+f.toString()));
        return true;
    }
}
