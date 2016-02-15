package ru.nukkit.regions.commands;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import ru.nukkit.regions.Regions;
import ru.nukkit.regions.manager.Region;
import ru.nukkit.regions.flags.Flag;
import ru.nukkit.regions.flags.FlagType;
import ru.nukkit.regions.util.Message;
import ru.nukkit.regions.util.Relation;
import ru.nukkit.regions.util.StringUtil;


@CmdDefine(command = "region", alias = "rg", subCommands = {"flag|f", "\\S+", "\\S+","\\S+"}, permission = "region.flag", description = Message.RG_FLAG_DESC)
public class CmdRgFlag extends Cmd{
    @Override
    public boolean execute(CommandSender sender, Player player, String[] args) {
        //       0    1    2     3       4
        // /rg flag <id> break allow
        // /rg flag <id> break rel:ALL allow
        // /rg flag <id> break clear
        Region region  = Regions.getManager().getRegion(args[1]);
        if (region==null) return Message.UNKNOWN_REGION.print(sender,args[1]);
        if (!Regions.getManager().isOwner(player,args[1])) return Message.ONLY_OWNER.print(sender);
        //if (FlagType.isValidType(args[2]))

        FlagType flagType = FlagType.getFlagByName(args[2]);

        if (flagType==null) return Message.UNKNOWN_FLAG.print(sender,args[2]);

        Relation rel = null;
        String value;
        if (args[3].matches("(?i)clear|remove|rmv|delete|del")) {
            return (region.clear(flagType) ? Message.RG_FLAG_CLEAR_OK : Message.RG_FLAG_CLEAR_FAIL).print(sender, flagType.name(), args[1]);
        } else if (args[3].matches("(?i)default|standart")){
            Flag f = flagType.getDefaultFlag();
            if (f==null||!Regions.getManager().addFlag (args[2],f)) Message.RG_FLAG_FAIL.print(sender,args[2]);
            return Message.RG_FLAG_OK.print(sender,args[2]);
        } else if (args[3].matches("(?i)^(rel|relation|r):.*")) {
            rel =  Relation.getByName(args[3].replaceAll("(?i)^(rel|relation|r|group|grp|g):",""));
            value = StringUtil.join (args,4);
        } else value = StringUtil.join (args,3);
        Flag flag = FlagType.createFlag (args[2],rel,value);
        if (flag==null||!Regions.getManager().addFlag (args[1],flag)) return Message.RG_FLAG_FAIL.print(sender,args[2].toUpperCase());
        else return Message.RG_FLAG_OK.print(sender,args[2].toUpperCase());
    }
}
