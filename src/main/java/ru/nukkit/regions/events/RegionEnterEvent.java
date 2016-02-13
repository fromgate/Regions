package ru.nukkit.regions.events;

import cn.nukkit.Player;
import cn.nukkit.event.HandlerList;
import ru.nukkit.regions.manager.Region;

public class RegionEnterEvent extends RegionEvent {
    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }
    public RegionEnterEvent(Player player, String regionId, Region region) {
        super(player, regionId, region);
    }
}
