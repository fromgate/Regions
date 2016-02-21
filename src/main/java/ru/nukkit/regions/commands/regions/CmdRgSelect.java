package ru.nukkit.regions.commands.regions;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import ru.nukkit.regions.Regions;
import ru.nukkit.regions.commands.Cmd;
import ru.nukkit.regions.commands.CmdDefine;
import ru.nukkit.regions.manager.Region;
import ru.nukkit.regions.util.Message;
import ru.nukkit.regions.util.StringUtil;

import java.util.Iterator;
import java.util.Map;


@CmdDefine(command = "region", alias = "rg", subCommands = "select|sel" , permission = "regions.select", description = Message.RG_SEL)
public class CmdRgSelect extends Cmd {
    @Override
    public boolean execute(CommandSender sender, Player player, String[] args) {
        Region region = null;
        if (args.length>1){
            region = Regions.getManager().getRegion(args[1]);
            if (region==null) return Message.UNKNOWN_REGION.print(sender,args[1]);
            if (!Regions.getManager().isOwner(player,args[1])) return Message.ONLY_OWNER.print(sender);
        } else {
            Map<String,Region> regions = Regions.getManager().getRegions(player.getLocation());
            if (regions.isEmpty()) return Message.RG_SEL_NOTFOUND.print(player);
            Iterator<Map.Entry<String,Region>> iterator  = regions.entrySet().iterator();
            while (iterator.hasNext()){
                if (!Regions.getManager().isOwner(player,iterator.next().getKey())) iterator.remove();
            }
            if (regions.isEmpty()) return Message.RG_SEL_NOTFOUND.print(player);
            if (regions.size()>0) return Message.RG_SEL_FOUNDMORE.print(player, StringUtil.listToString(regions.keySet()));
            region = regions.values().toArray(new Region[1])[0];
        }
        Regions.getSelector().selectP1(player,region.getMin());
        Regions.getSelector().selectP2(player,region.getMax());
        return Message.SEL_AREASELECTED.print(player,Regions.getSelector().getSelectionVolume(player));
    }
}
