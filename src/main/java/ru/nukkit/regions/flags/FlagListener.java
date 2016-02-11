package ru.nukkit.regions.flags;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.level.Location;
import ru.nukkit.regions.Regions;
import ru.nukkit.regions.manager.Region;
import ru.nukkit.regions.util.Message;
import ru.nukkit.regions.util.Relation;

import java.util.Map;

public class FlagListener implements Listener {

    @EventHandler (ignoreCancelled =  true, priority = EventPriority.NORMAL)
    public void onBreak (BlockBreakEvent event){
        if (cancelEvent(event.getPlayer(), event.getBlock().getLocation(),FlagType.BREAK)){
            event.setCancelled();
            Message.FMSG_BREAK.print(event.getPlayer());
        }
    }

    @EventHandler (ignoreCancelled =  true, priority = EventPriority.NORMAL)
    public void onBuild (BlockPlaceEvent event){
        if (cancelEvent(event.getPlayer(), event.getBlock().getLocation(),FlagType.BUILD)){
            event.setCancelled();
            Message.FMSG_BUILD.print(event.getPlayer());
        }
    }

    private boolean cancelEvent(Player player, Location loc, FlagType flagType){
        Map<String,Region> regions = Regions.getManager().getRegions(loc);
        if (regions == null||regions.isEmpty()) return false;
        for (Region region : regions.values()) {
            Flag f = region.getFlag(flagType);
            Relation rel = region.getRelation(player.getName());
            BoolFlag bf = (BoolFlag) f;
            if (!bf.isAllowed (rel)) return true;
        }
        return false;
    }

}
