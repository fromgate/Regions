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
import ru.nukkit.regions.flags.FlagType;
import ru.nukkit.regions.util.Message;

import java.util.HashMap;
import java.util.Map;

public class WalkListener implements Listener {

    private Map<String,Location> locations;

    public WalkListener(){
        if (!RegionsPlugin.getCfg().usePlayerMoveEvent) {
            this.locations = new HashMap<String,Location>();
            startRechek();
        }
    }

    private void startRechek() {
        Server.getInstance().getScheduler().scheduleRepeatingTask(new Runnable() {
            public void run() {
                for (Player player : Server.getInstance().getOnlinePlayers().values()){
                    Location from = locations.containsKey(player.getName()) ? locations.get(player.getName()) : null;
                    if (cancelPlayerMove(player,player.getLocation())&&from!=null) player.teleport(from);
                    else locations.put(player.getName(),player.getLocation());
                }
            }
        },RegionsPlugin.getCfg().playerMoveRechek);
    }

    private boolean cancelPlayerMove (Player player, Location to){
        if (Regions.getManager().cancelEvent(player,to, FlagType.ENTRY)) return Message.FMSG_ENTRY.print(player);
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
        if (RegionsPlugin.getCfg().usePlayerMoveEvent) return;
        locations.put(event.getPlayer().getName(), event.getPlayer().getLocation());
    }

}
