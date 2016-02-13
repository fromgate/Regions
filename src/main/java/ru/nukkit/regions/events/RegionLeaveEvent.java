package ru.nukkit.regions.events;

import cn.nukkit.Player;
import cn.nukkit.event.player.PlayerEvent;
import ru.nukkit.regions.manager.Region;

public class RegionLeaveEvent extends RegionEvent{
    public RegionLeaveEvent(Player player, String regionId, Region region, PlayerEvent parentEvent) {
        super(player, regionId, region,parentEvent);
    }
}
