package ru.nukkit.regions.events;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;
import ru.nukkit.regions.manager.Region;

public abstract class RegionEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }

    private Region region;
    private String regionId;

    private PlayerEvent parent;

    public RegionEvent (Player player, String regionId, Region region, PlayerEvent parent){
        this.player = player;
        this.regionId= regionId;
        this.region = region;
        this.parent = parent;
    }

    public String getRegionId(){
        return this.regionId;
    }

    public Region getRegion(){
        return this.region;
    }

}
