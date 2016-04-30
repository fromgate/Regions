package ru.nukkit.regions.events;

import cn.nukkit.Player;
import cn.nukkit.event.HandlerList;
import ru.nukkit.regions.manager.Region;

public class RegionLeaveEvent extends RegionEvent {
    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }

    public RegionLeaveEvent(Player player, String regionId, Region region) {
        super(player, regionId, region);
    }
}
