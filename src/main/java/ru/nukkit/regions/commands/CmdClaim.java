package ru.nukkit.regions.commands;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Location;
import ru.nukkit.regions.Regions;
import ru.nukkit.regions.RegionsPlugin;
import ru.nukkit.regions.areas.Area;
import ru.nukkit.regions.flags.BoolFlag;
import ru.nukkit.regions.flags.Flag;
import ru.nukkit.regions.flags.FlagType;
import ru.nukkit.regions.manager.Region;
import ru.nukkit.regions.util.Message;
import ru.nukkit.regions.util.StringUtil;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@CmdDefine(command = "claim", alias = "rgclaim", subCommands ={} , permission = "regions.claim", description = Message.RG_CLAIM_DESC)
public class CmdClaim extends Cmd {
    @Override
    public boolean execute(CommandSender sender, Player player, String[] args) {
        if (Regions.getManager().canClaimMore(player.getName()))
            return Message.RG_CLAIM_MAX_COUNT_REACHED.print(sender,RegionsPlugin.getPlugin().getCfg().maxRegionPerPlayer);

        if (RegionsPlugin.getPlugin().getCfg().claimOnlyExisting){
            /*
            TODO move this code to claim-manager, add economy support (buy/sell regions)
             */
            if (args.length==1) return Message.RG_CLAIM_PREDEFINED_RENAME.print(player);

            Map<String,Region> regions = Regions.getManager().getRegions(player);
            Iterator <Map.Entry<String,Region>> it = regions.entrySet().iterator();
            while (it.hasNext()){
                Map.Entry<String,Region> r = it.next();
                Flag f = r.getValue().getFlag(FlagType.CLAIM);
                BoolFlag bf = (BoolFlag) f;
                if (!bf.isAllowed(r.getValue().getRelation(player.getName()))) it.remove();
            }

            if (regions.isEmpty()) return Message.RG_CLAIM_INSIDE_ONLY.print(player);
            if (regions.size()>1) return Message.RG_CLAIM_SINGLE_ONLY.print(player);
            String id = regions.keySet().toArray(new String[1])[0];
            Region region = regions.get(id);
            if (!region.clear(FlagType.CLAIM)) return Message.RG_CLAIM_PREDEFINED_FAIL.print(sender);
            //if (region.hasOwners()) return Message.RG_CLAIM_ALREADY.print(player);
            return (Regions.getManager().setOwner(id,player.getName())?Message.RG_CLAIM_PREDEFINED_OK:Message.RG_CLAIM_PREDEFINED_FAIL).print(player,id);
        } else {
            List<Location> locs = Regions.getSelector().getPoints(player);
            if (locs == null||locs.size()!=2) return Message.DEF_SELECT.print(player);
            String id = args[0];
            if (Regions.getManager().regionIdUsed(id)) Message.RG_DEF_ID_USED.print(player,id);

            Area area = new Area (locs.get(0),locs.get(1));
            if (Regions.getManager().canClaimVolume(area))
                return Message.RG_CLAIM_AREA_VOLUME_REACHED.print(sender,RegionsPlugin.getPlugin().getCfg().maxClaimVolume);

            if (!RegionsPlugin.getPlugin().getCfg().intersectionsAllowed
                    && Regions.getManager().getIntersections(area).size()>0) return Message.RG_CLAIM_AREA_INTERSECTED.print(sender);
            if (Regions.getManager().defineRegion(id,(args.length>1 ? StringUtil.join(args,1): null),locs)) {
                Regions.getSelector().setSelMode(player,false);
                Regions.getSelector().clearSelection(player);
                return Message.RG_CLAIM_OK.print(sender,id);
            } else return Message.RG_CLAIM_FAIL.print(sender,id);
        }
    }
}
