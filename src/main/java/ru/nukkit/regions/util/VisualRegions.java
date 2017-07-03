package ru.nukkit.regions.util;

import cn.nukkit.Player;
import cn.nukkit.Server;
import ru.nukkit.regions.Regions;
import ru.nukkit.regions.RegionsPlugin;
import ru.nukkit.regions.areas.Area;
import ru.nukkit.regions.flags.FlagType;
import ru.nukkit.regions.manager.Region;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class VisualRegions {

    private static Map<Area, Set<Player>> visualEffect;
    private static boolean enable;

    public static void updateVisuals() {
        if (!enable) return;
        visualEffect = new HashMap<>();
        for (Region region : Regions.getManager().getRegions().values()) {
            Set<Player> players = new HashSet<>();
            for (Player player : Server.getInstance().getOnlinePlayers().values()) {
                if (Regions.getManager().cancelEvent(player, region, FlagType.VISUAL)) continue;
                players.add(player);
            }
            if (!players.isEmpty()) visualEffect.put(region.getArea(), players);
        }
    }

    public static void init() {
        enable = true; //TODO
        visualEffect = new HashMap<>();
        Server.getInstance().getScheduler().scheduleDelayedRepeatingTask(new Runnable() {
            public void run() {
                updateVisuals();
            }
        }, 30, RegionsPlugin.getCfg().selectionTick);


    }


}
