package ru.nukkit.regions.util;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.SimpleConfig;

public class RegionsConfig extends SimpleConfig {

    @Path (value = "player-move.use-player-move-event")
    public boolean usePlayerMoveEvent=true;

    @Path (value = "player-move.recheck-interval-ticks")
    public int playerMoveRechek=10;

    @Path (value = "claim.max-regions-per-player")
    public int maxRegionPerPlayer=5;

    @Path (value = "claim.max-claim-volume")
    public int maxClaimVolume=10000;

    @Path (value = "claim.claim-only-existing-regions")
    public boolean claimOnlyExisting = false;

    @Path (value = "claim.allow-to-instersect-with-other-regions")
    public boolean intersectionsAllowed = false;

    public RegionsConfig(PluginBase plugin) {
        super(plugin);
    }
}