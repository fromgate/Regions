package ru.nukkit.regions.listeners;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.block.DoorToggleEvent;
import cn.nukkit.event.entity.EntityDamageByChildEntityEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import ru.nukkit.regions.Regions;
import ru.nukkit.regions.flags.FlagType;
import ru.nukkit.regions.util.BlockUtil;
import ru.nukkit.regions.util.Message;

import static ru.nukkit.regions.flags.FlagType.PVP;

public class FlagListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onBreak(BlockBreakEvent event) {
        if (Regions.getManager().cancelEvent(event.getPlayer(), event.getBlock().getLocation(), FlagType.BREAK)) {
            event.setCancelled();
            Message.FMSG_BREAK.print(event.getPlayer());
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onBuild(BlockPlaceEvent event) {
        if (Regions.getManager().cancelEvent(event.getPlayer(), event.getBlock().getLocation(), FlagType.BUILD)) {
            event.setCancelled();
            Message.FMSG_BUILD.print(event.getPlayer());
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onDoor(DoorToggleEvent event) {
        if (!BlockUtil.isDoor(event.getBlock())) return;
        Player player = event.getPlayer();
        if(player != null) {
            if (Regions.getManager().cancelEvent(player, event.getBlock().getLocation(), FlagType.DOOR))
                event.setCancelled();
            if (event.isCancelled()) Message.FMSG_INTERACT.print(player);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        switch (event.getBlock().getId()) {
            case Block.CHEST:
            case Block.TRAPPED_CHEST:
                if (Regions.getManager().cancelEvent(player, event.getBlock().getLocation(), FlagType.CHEST))
                    event.setCancelled();
                break;
            case Block.LEVER:
                if (Regions.getManager().cancelEvent(player, event.getBlock().getLocation(), FlagType.LEVER))
                    event.setCancelled();
                break;
            //BUTTON???
            case Block.STONE_PRESSURE_PLATE:
            case Block.WOODEN_PRESSURE_PLATE:
                if (Regions.getManager().cancelEvent(player, event.getBlock().getLocation(), FlagType.LEVER))
                    event.setCancelled();
                break;
        }
        if (event.isCancelled()) Message.FMSG_INTERACT.print(player);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onPVP(EntityDamageEvent event) {
        Player player = (event.getEntity() instanceof Player) ? (Player) event.getEntity() : null;
        if (player == null) return;
        Player damager = null;
        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent ev = (EntityDamageByEntityEvent) event;
            if (ev instanceof EntityDamageByChildEntityEvent) {
                EntityDamageByChildEntityEvent evc = (EntityDamageByChildEntityEvent) ev;
                if (evc.getDamager() instanceof Player) damager = (Player) evc.getDamager();
            } else if (ev.getDamager() instanceof Player) damager = (Player) ev.getDamager();
        }
        if (damager == null) return;
        if (Regions.getManager().cancelEvent(player, player.getLocation(), PVP) ||
                Regions.getManager().cancelEvent(damager, damager.getLocation(), PVP)) event.setCancelled();
    }
}