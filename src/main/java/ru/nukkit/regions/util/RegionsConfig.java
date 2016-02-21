package ru.nukkit.regions.util;

import cn.nukkit.plugin.Plugin;
import cn.nukkit.utils.SimpleConfig;

public class RegionsConfig extends SimpleConfig {

    // Selection particles
    @Path (value = "selection.particles.enable")
    public boolean selectionShow;

    @Path (value = "selection.particles.draw-wall")
    public boolean selectionDrawWall = true;

    @Path (value = "selection.particles.solid")
    public boolean selectionSolidWall = false;

    @Path (value = "selection.particles.replay-interval-ticks")
    public int selectionTick = 10;

    @Path (value = "selection.particles.limit-amount")
    public int selectionLimitAmount = 100;

    @Path (value = "selection.particles.limit-distance")
    public int particleDistance = 64;

    @Path (value = "selection.particles.show-intersections")
    public boolean selectionShowIntersections=true;

    // Player Move detections
    @Path (value = "player-move.use-player-move-event")
    public boolean usePlayerMoveEvent=true;

    @Path (value = "player-move.recheck-interval-ticks")
    public int playerMoveRechek=10;

    // Claim command configuration
    @Path (value = "claim.max-regions-per-player")
    public int maxRegionPerPlayer=5;

    @Path (value = "claim.max-claim-volume")
    public int maxClaimVolume=10000;

    @Path (value = "claim.claim-only-existing-regions")
    public boolean claimOnlyExisting = false;

    @Path (value = "claim.claim-only-when-player-inside-region")
    public boolean claimOnlyInside = false;

    @Path (value = "claim.allow-to-instersect-with-other-regions")
    public boolean intersectionsAllowed = false;


    /*
     * Builder configuration
     *
     *
     **/

    @Path (value = "builder.log.volume-to-inform")
    public int builderLogAmount = 5000;

    @Path (value = "builder.queue.max-time-per-tick-ms")
    public long builderTicks=30;

    @Path (value = "enable-undo-operations")
    public boolean builderUseUndo=true;


    public RegionsConfig(Plugin plugin) {
        super(plugin);
    }
}