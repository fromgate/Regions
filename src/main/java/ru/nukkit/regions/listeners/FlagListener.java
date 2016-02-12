package ru.nukkit.regions.listeners;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.entity.EntityDamageByChildEntityEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import ru.nukkit.regions.Regions;
import ru.nukkit.regions.flags.FlagType;
import ru.nukkit.regions.util.Message;

public class FlagListener implements Listener {

    @EventHandler (ignoreCancelled =  true, priority = EventPriority.NORMAL)
    public void onBreak (BlockBreakEvent event){
        if (Regions.getManager().cancelEvent(event.getPlayer(), event.getBlock().getLocation(),FlagType.BREAK)){
            event.setCancelled();
            Message.FMSG_BREAK.print(event.getPlayer());
        }
    }

    @EventHandler (ignoreCancelled =  true, priority = EventPriority.NORMAL)
    public void onBuild (BlockPlaceEvent event){
        if (Regions.getManager().cancelEvent(event.getPlayer(), event.getBlock().getLocation(),FlagType.BUILD)){
            event.setCancelled();
            Message.FMSG_BUILD.print(event.getPlayer());
        }
    }

    @EventHandler (ignoreCancelled =  true, priority = EventPriority.NORMAL)
    public void onInteractDoor (PlayerInteractEvent event) {
        switch (event.getBlock().getId()){
            case Block.DOOR_BLOCK:
            case Block.ACACIA_DOOR_BLOCK:
            case Block.DARK_OAK_DOOR_BLOCK:
            case Block.BIRCH_DOOR_BLOCK:
            case Block.JUNGLE_DOOR_BLOCK:
            case Block.SPRUCE_DOOR_BLOCK:
                event.setCancelled();
        }
    }

    @EventHandler (ignoreCancelled =  true, priority = EventPriority.NORMAL)
    public void onInteract (PlayerInteractEvent event){
        //if (event.getAction()!=PlayerInteractEvent.RIGHT_CLICK_BLOCK) return;
        Player player = event.getPlayer();
        switch (event.getBlock().getId()){
            case Block.CHEST:
            case Block.TRAPPED_CHEST:
                if (Regions.getManager().cancelEvent(player,event.getBlock().getLocation(),FlagType.CHEST)) event.setCancelled();
                break;
            case Block.LEVER:
                if (Regions.getManager().cancelEvent(player,event.getBlock().getLocation(),FlagType.LEVER)) event.setCancelled();
                break;
            //BUTTON???
            case Block.STONE_PRESSURE_PLATE:
            case Block.WOODEN_PRESSURE_PLATE:
                if (Regions.getManager().cancelEvent(player,event.getBlock().getLocation(),FlagType.LEVER)) event.setCancelled();
                break;
            case Block.DOOR_BLOCK:
            case Block.ACACIA_DOOR_BLOCK:
            case Block.DARK_OAK_DOOR_BLOCK:
            case Block.BIRCH_DOOR_BLOCK:
            case Block.JUNGLE_DOOR_BLOCK:
            case Block.SPRUCE_DOOR_BLOCK:
                if (Regions.getManager().cancelEvent(player,event.getBlock().getLocation(),FlagType.DOOR)) event.setCancelled();
                break;
            case Block.TRAPDOOR:
                if (Regions.getManager().cancelEvent(player,event.getBlock().getLocation(),FlagType.DOOR)) event.setCancelled();
                break;
            case Block.FENCE_GATE:
            case Block.FENCE_GATE_ACACIA:
            case Block.FENCE_GATE_BIRCH:
            case Block.FENCE_GATE_DARK_OAK:
            case Block.FENCE_GATE_JUNGLE:
            case Block.FENCE_GATE_SPRUCE:
                if (Regions.getManager().cancelEvent(player,event.getBlock().getLocation(),FlagType.DOOR)) event.setCancelled();
                break;
        }
        if (event.isCancelled()) Message.FMSG_INTERACT.print(player);
    }

    @EventHandler (ignoreCancelled =  true, priority = EventPriority.NORMAL)
    public void onPVP (EntityDamageEvent event){
        Player player =  (event.getEntity() instanceof Player) ? (Player) event.getEntity() : null;
        if (player == null) return;
        if (event instanceof EntityDamageByEntityEvent){
            EntityDamageByEntityEvent ev = (EntityDamageByEntityEvent) event;
            if (!(ev.getDamager() instanceof Player)) return;
            if (Regions.getManager().cancelEvent(player, player.getLocation(),FlagType.PVP)) event.setCancelled();
        } else if (event instanceof EntityDamageByChildEntityEvent) {
            EntityDamageByChildEntityEvent ev = (EntityDamageByChildEntityEvent) event;
            if (!(ev.getDamager() instanceof Player)) return;
            if (Regions.getManager().cancelEvent(player, player.getLocation(),FlagType.PVP)) event.setCancelled();
        }
    }
}