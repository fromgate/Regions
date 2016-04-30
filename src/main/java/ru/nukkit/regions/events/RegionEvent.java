package ru.nukkit.regions.events;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.player.PlayerEvent;
import ru.nukkit.regions.manager.Region;

public abstract class RegionEvent extends PlayerEvent implements Cancellable {
    private Region region;
    private String regionId;

    public RegionEvent(Player player, String regionId, Region region) {
        this.player = player;
        this.regionId = regionId;
        this.region = region;
    }

    public String getRegionId() {
        return this.regionId;
    }

    public Region getRegion() {
        return this.region;
    }

}
