package ru.nukkit.regions.events;

import cn.nukkit.Player;
import cn.nukkit.event.player.PlayerEvent;
import ru.nukkit.regions.manager.Region;

public class RegionEnterEvent extends RegionEvent {
    public RegionEnterEvent(Player player, String regionId, Region region, PlayerEvent parentEvent) {
        super(player, regionId, region,parentEvent);
    }
}
