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
import ru.nukkit.regions.flags.EffectFlag;
import ru.nukkit.regions.flags.FlagType;
import ru.nukkit.regions.flags.StringFlag;
import ru.nukkit.regions.manager.Region;
import ru.nukkit.regions.util.Message;
import ru.nukkit.regions.util.PotionEffects;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class WalkListener implements Listener {

    private Map<String, Location> prevLoc;
    private Map<String, Map<String, Region>> currentRegions;

    public WalkListener() {
        if (!RegionsPlugin.getCfg().enablePlayerMove) return;
        this.currentRegions = new HashMap<>();
        if (!RegionsPlugin.getCfg().usePlayerMoveEvent) {
            this.prevLoc = new HashMap<>();
            startRechek();
        } else Server.getInstance().getPluginManager().registerEvents(this, RegionsPlugin.getPlugin());
    }

    private void startRechek() {
        Server.getInstance().getScheduler().scheduleRepeatingTask(new Runnable() {
            public void run() {
                for (Player player : Server.getInstance().getOnlinePlayers().values()) {
                    Location from = prevLoc.getOrDefault(player.getName(), null);
                    Location to = player.getLocation();
                    if (from == null || (from.getFloorX() == to.getFloorX() &&
                            from.getFloorY() == to.getFloorY() &&
                            from.getFloorZ() == to.getFloorZ()) ||
                            !cancelPlayerMove(player, to)) prevLoc.put(player.getName(), to);
                    else player.teleport(from);
                }
            }
        }, RegionsPlugin.getCfg().playerMoveRechek);
    }

    private boolean cancelPlayerMove(Player player, Location to) {
        Map<String, Region> fromReg = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        fromReg.putAll(this.currentRegions.containsKey(player.getName()) ? this.currentRegions.get(player.getName()) : new HashMap<>());

        Map<String, Region> toReg = Regions.getManager().getRegions(to);

        Map<String, Region> toCheck = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        Map<String, Region> fromCheck = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        if (RegionsPlugin.getCfg().enableCustomEvents) {
            for (Map.Entry<String, Region> tor : toReg.entrySet()) {
                if (fromReg.containsKey(tor.getKey())) continue;
                RegionEnterEvent e = new RegionEnterEvent(player, tor.getKey(), tor.getValue());
                Server.getInstance().getPluginManager().callEvent(e);
                if (e.isCancelled()) return true;
                toCheck.put(tor.getKey(), tor.getValue());
            }

            for (Map.Entry<String, Region> fr : fromReg.entrySet()) {
                if (toReg.containsKey(fr.getKey())) continue;
                RegionLeaveEvent e = new RegionLeaveEvent(player, fr.getKey(), fr.getValue());
                Server.getInstance().getPluginManager().callEvent(e);
                if (e.isCancelled()) return true;
                fromCheck.put(fr.getKey(), fr.getValue());
            }
        }


        for (Map.Entry<String, Region> entry : toCheck.entrySet()) {
            Region region = entry.getValue();
            if (Regions.getManager().cancelEvent(player, region, FlagType.ENTRY))
                return Message.FMSG_LEAVE.print(player);
            StringFlag sf = (StringFlag) region.getFlag(FlagType.ENTRYMSG);
            sf.print(player, entry.getKey());
            EffectFlag ef = (EffectFlag) region.getFlag(FlagType.EFFECT);
            PotionEffects.setEffects(player, ef.getValue());
        }

        for (Map.Entry<String, Region> entry : fromCheck.entrySet()) {
            Region region = entry.getValue();
            if (Regions.getManager().cancelEvent(player, region, FlagType.LEAVE))
                return Message.FMSG_LEAVE.print(player);
            StringFlag sf = (StringFlag) region.getFlag(FlagType.LEAVEMSG);
            sf.print(player, entry.getKey());
            EffectFlag ef = (EffectFlag) region.getFlag(FlagType.EFFECT);
            PotionEffects.removeEffects(player, ef.getValue());
        }
        this.currentRegions.put(player.getName(), toReg);
        return false;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onPlayerMove(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();
        if (from.getFloorX() == to.getFloorX() &&
                from.getFloorY() == to.getFloorY() &&
                from.getFloorZ() == to.getFloorZ()) return;
        if (cancelPlayerMove(event.getPlayer(), to)) event.setCancelled();

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        this.currentRegions.put(player.getName(), Regions.getManager().getRegions(player.getLocation()));
        if (!RegionsPlugin.getCfg().usePlayerMoveEvent) prevLoc.put(player.getName(), player.getLocation());
        cancelPlayerMove(event.getPlayer(), event.getPlayer().getLocation());
    }
}
