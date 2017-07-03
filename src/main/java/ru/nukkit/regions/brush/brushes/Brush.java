package ru.nukkit.regions.brush.brushes;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.level.Location;
import cn.nukkit.utils.BlockIterator;
import ru.nukkit.regions.Regions;
import ru.nukkit.regions.builder.UndoManager;
import ru.nukkit.regions.util.LocUtil;

import java.util.ArrayList;
import java.util.List;

public abstract class Brush {

    boolean valid;
    int radius; // Brush size

    public Brush() {
        radius = 0;
    }

    public abstract boolean init(String[] args);

    public abstract boolean paint(Player player, Block clickedBlock);

    public Location getTarget(Player player) {
        Block targetBlock = player.getTargetBlock(75);
        if (targetBlock == null) return null;
        return targetBlock.getLocation();
    }

    public Location getTargetAir(Player player) {
        int maxDistance = 75;
        BlockIterator itr;
        try {
            itr = new BlockIterator(player.level, player.getPosition(), player.getDirectionVector(), (double) player.getEyeHeight(), maxDistance);
        } catch (Exception e) {
            return null;
        }
        Block block = null;
        while (itr.hasNext()) {
            Block temp = itr.next();
            if (temp.getId() != 0) break;
            block = temp;
        }
        return block == null ? null : block.getLocation();
    }

    public List<Block> getBall(Location loc, int radius) {
        List<Block> blocks = new ArrayList<>();
        if (radius <= 0) {
            blocks.add(loc.getLevelBlock());
        } else for (int x = loc.getFloorX() - radius; x <= loc.getFloorX() + radius; x++) {
            for (int z = loc.getFloorZ() - radius; z <= loc.getFloorZ() + radius; z++) {
                for (int y = Math.max(0, loc.getFloorY() - radius); y <= Math.min(loc.getFloorY() + radius, LocUtil.getWorldHeight() - 1); y++) {
                    Location l = new Location(x, y, z, 0, 0, loc.getLevel());
                    if (l.distance(loc) < radius) blocks.add(l.getLevelBlock());
                }
            }
        }
        return blocks;
    }

    public List<Block> getDisk(Location loc, int radius) {
        List<Block> blocks = new ArrayList<>();
        if (radius <= 0) {
            blocks.add(loc.getLevelBlock());
        } else {
            for (int x = loc.getFloorX() - radius; x <= loc.getFloorX() + radius; x++) {
                for (int z = loc.getFloorZ() - radius; z <= loc.getFloorZ() + radius; z++) {
                    Location l = new Location(x, loc.getY(), z, 0, 0, loc.getLevel());
                    if (l.distance(loc) <= radius) blocks.add(l.getLevelBlock());
                }
            }
        }
        return blocks;
    }


    protected UndoManager getUndoManager() {
        return Regions.getUndoManager();
    }
}
