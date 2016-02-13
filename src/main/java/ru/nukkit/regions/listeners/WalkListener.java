package ru.nukkit.regions.listeners;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.level.Location;
import ru.nukkit.regions.Regions;
import ru.nukkit.regions.RegionsPlugin;
import ru.nukkit.regions.events.RegionEnterEvent;
import ru.nukkit.regions.events.RegionLeaveEvent;
import ru.nukkit.regions.flags.FlagType;
import ru.nukkit.regions.flags.StringFlag;
import ru.nukkit.regions.manager.Region;
import ru.nukkit.regions.util.Message;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class WalkListener implements Listener {

    private Map<String,Location> prevLoc;
    private Map<String,Map<String,Region>> currentRegions;

    public WalkListener(){
        this.currentRegions = new HashMap<String, Map<String,Region>>();
        if (!RegionsPlugin.getCfg().usePlayerMoveEvent) {
            this.prevLoc = new HashMap<String,Location>();
            startRechek();
        }
    }

    private void startRechek() {
        Server.getInstance().getScheduler().scheduleRepeatingTask(new Runnable() {
            public void run() {
                for (Player player : Server.getInstance().getOnlinePlayers().values()){
                    Location from = prevLoc.containsKey(player.getName()) ? prevLoc.get(player.getName()) : null;
                    Location to = player.getLocation();
                    if (from==null||(from.getFloorX()==to.getFloorX()&&
                            from.getFloorY()==to.getFloorY()&&
                            from.getFloorZ()==to.getFloorZ())||
                            !cancelPlayerMove(player,to)) prevLoc.put(player.getName(),to);
                    else player.teleport(from);
                }
            }
        },RegionsPlugin.getCfg().playerMoveRechek);
    }

    private boolean cancelPlayerMove (Player player, Location to){
        Map<String,Region> fromReg = new TreeMap<String, Region>(String.CASE_INSENSITIVE_ORDER);
        fromReg.putAll(this.currentRegions.containsKey(player.getName()) ? this.currentRegions.get(player.getName()) : new HashMap<String, Region>());
        Map<String,Region> toReg =Regions.getManager().getRegions(to);
        Map<String,Region> toCheck = new TreeMap<String, Region>(String.CASE_INSENSITIVE_ORDER);
        Map<String,Region> fromCheck = new TreeMap<String, Region>(String.CASE_INSENSITIVE_ORDER);
        for (Map.Entry<String,Region> tor : toReg.entrySet()){
            if (fromReg.containsKey(tor.getKey())) continue;
            RegionEnterEvent e = new RegionEnterEvent(player,tor.getKey(),tor.getValue());
            Server.getInstance().getPluginManager().callEvent(e);
            if (e.isCancelled()) return true;
            toCheck.put(tor.getKey(),tor.getValue());
        }

        for (Map.Entry<String,Region> fr : fromReg.entrySet()){
            if (toReg.containsKey(fr.getKey())) continue;
            RegionLeaveEvent e = new RegionLeaveEvent(player,fr.getKey(),fr.getValue());
            Server.getInstance().getPluginManager().callEvent(e);
            if (e.isCancelled()) return true;
            fromCheck.put(fr.getKey(),fr.getValue());
        }

        for (Region region : toCheck.values()) {
            if (Regions.getManager().cancelEvent(player, region, FlagType.ENTRY))
                return Message.FMSG_ENTRY.print(player);
        }

        for (Region region : fromCheck.values())
            if (Regions.getManager().cancelEvent(player,region,FlagType.LEAVE)) return Message.FMSG_LEAVE.print(player);

        for (Map.Entry<String,Region> entry : toCheck.entrySet()){
            StringFlag sf = (StringFlag) entry.getValue().getFlag(FlagType.ENTRYMSG);
            sf.print(player,entry.getKey());
        }

        for (Map.Entry<String,Region> entry : fromCheck.entrySet()){
            StringFlag sf = (StringFlag) entry.getValue().getFlag(FlagType.LEAVEMSG);
            sf.print(player,entry.getKey());
        }
        this.currentRegions.put(player.getName(),toReg);
        return false;
    }

    @EventHandler (ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onPlayerMove (PlayerMoveEvent event){
        if (!RegionsPlugin.getCfg().usePlayerMoveEvent) return;
        Location from = event.getFrom();
        Location to = event.getTo();
        if (from.getFloorX()==to.getFloorX()&&
                from.getFloorY()==to.getFloorY()&&
                from.getFloorZ()==to.getFloorZ()) return;
        if (cancelPlayerMove(event.getPlayer(), to)) event.setCancelled();
    }

    @EventHandler (ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onPlayerJoin (PlayerJoinEvent event){
        Player player = event.getPlayer();
        this.currentRegions.put(player.getName(),Regions.getManager().getRegions(player.getLocation()));
        if (!RegionsPlugin.getCfg().usePlayerMoveEvent) prevLoc.put(player.getName(), player.getLocation());
    }
}
