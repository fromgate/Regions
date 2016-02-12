package ru.nukkit.regions.flags;

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
import cn.nukkit.level.Location;
import ru.nukkit.regions.Regions;
import ru.nukkit.regions.manager.Region;
import ru.nukkit.regions.util.Message;
import ru.nukkit.regions.util.Relation;

import java.util.Map;

public class FlagListener implements Listener {

    private boolean cancelEvent(Player player, Location loc, FlagType flagType){
        Map<String,Region> regions = Regions.getManager().getRegions(loc);
        if (regions == null||regions.isEmpty()) return false;
        for (Region region : regions.values()) {
            Flag f = region.getFlag(flagType);
            Relation rel = region.getRelation(player.getName());
            BoolFlag bf = (BoolFlag) f;
            Message.BC(f.getType().name()+" : "+rel.name()+" | flag: "+f.getRelation().name()+" : "+f.getParam()+" | "+bf.isAllowed (rel));
            if (!bf.isAllowed (rel)) return true;
        }
        return false;
    }

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

    //@EventHandler (ignoreCancelled =  true, priority = EventPriority.NORMAL)
    @EventHandler
    public void onInteract (PlayerInteractEvent event){
        //if (event.getAction()!=PlayerInteractEvent.RIGHT_CLICK_BLOCK) return;
        Player player = event.getPlayer();
        switch (event.getBlock().getId()){
            case Block.CHEST:
            case Block.TRAPPED_CHEST:
                if (cancelEvent(player,event.getBlock().getLocation(),FlagType.CHEST)) event.setCancelled();
                break;
            case Block.LEVER:
                if (cancelEvent(player,event.getBlock().getLocation(),FlagType.LEVER)) event.setCancelled();
                break;
            //BUTTON???
            case Block.STONE_PRESSURE_PLATE:
            case Block.WOODEN_PRESSURE_PLATE:
                if (cancelEvent(player,event.getBlock().getLocation(),FlagType.LEVER)) event.setCancelled();
                break;
            case Block.DOOR_BLOCK:
            case Block.ACACIA_DOOR_BLOCK:
            case Block.DARK_OAK_DOOR_BLOCK:
            case Block.BIRCH_DOOR_BLOCK:
            case Block.JUNGLE_DOOR_BLOCK:
            case Block.SPRUCE_DOOR_BLOCK:
                if (cancelEvent(player,event.getBlock().getLocation(),FlagType.DOOR)) event.setCancelled();
                break;
            case Block.TRAPDOOR:
                if (cancelEvent(player,event.getBlock().getLocation(),FlagType.DOOR)) event.setCancelled();
                break;
            case Block.FENCE_GATE:
            case Block.FENCE_GATE_ACACIA:
            case Block.FENCE_GATE_BIRCH:
            case Block.FENCE_GATE_DARK_OAK:
            case Block.FENCE_GATE_JUNGLE:
            case Block.FENCE_GATE_SPRUCE:
                if (cancelEvent(player,event.getBlock().getLocation(),FlagType.DOOR)) event.setCancelled();
                break;
        }
    }

    @EventHandler (ignoreCancelled =  true, priority = EventPriority.NORMAL)
    public void onPVP (EntityDamageEvent event){
        Player player =  (event.getEntity() instanceof Player) ? (Player) event.getEntity() : null;
        if (player == null) return;
        if (event instanceof EntityDamageByEntityEvent){
            EntityDamageByEntityEvent ev = (EntityDamageByEntityEvent) event;
            if (!(ev.getDamager() instanceof Player)) return;
            if (cancelEvent(player, player.getLocation(),FlagType.PVP)) event.setCancelled();
        } else if (event instanceof EntityDamageByChildEntityEvent) {
            EntityDamageByChildEntityEvent ev = (EntityDamageByChildEntityEvent) event;
            if (!(ev.getDamager() instanceof Player)) return;
            if (cancelEvent(player, player.getLocation(),FlagType.PVP)) event.setCancelled();
        }
    }

}
