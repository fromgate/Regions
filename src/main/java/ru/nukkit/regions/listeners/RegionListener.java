package ru.nukkit.regions.listeners;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.level.Location;
import ru.nukkit.regions.Regions;
import ru.nukkit.regions.util.Message;

public class RegionListener implements Listener{


    @EventHandler (ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onSelect (PlayerInteractEvent event){
        if (event.getItem().getId()!=0) return;
        Player player = event.getPlayer();
        if (!Regions.getSelector().selMode(player)) return;
        if (event.getAction()!=PlayerInteractEvent.LEFT_CLICK_BLOCK&&event.getAction()!=PlayerInteractEvent.RIGHT_CLICK_BLOCK) return;
        boolean selP1 = event.getAction()==PlayerInteractEvent.LEFT_CLICK_BLOCK;
        Location selPoint = new Location(event.getBlock().getX(),event.getBlock().getY(),event.getBlock().getZ(),0,0,event.getBlock().getLevel());
        if (!Regions.getSelector().isSelected(player,selPoint)) {
            if (Regions.getSelector().selectPoint(player, selPoint, selP1))
                Message.SEL_OK.print(player, '6', selPoint, (selP1 ? "1" : "2"));
            else Message.SEL_FAIL.print(player, 'c');
        }
        event.setCancelled();
    }

    @EventHandler
    public void onJoin (PlayerJoinEvent event){
        Regions.getSelector().setSelMode(event.getPlayer(),false);
    }
}