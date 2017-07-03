package ru.nukkit.regions.clipboard;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.level.Location;
import ru.nukkit.regions.Regions;
import ru.nukkit.regions.areas.Area;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ClipBlock extends Clipboard {

    String playerName;
    Location playerLocation;
    Location minLocation;
    List<Block> blocks;

    public ClipBlock() {
        this.playerName = "";
        this.playerLocation = null;
        this.minLocation = null;
        this.blocks = new LinkedList<>();
    }

    public ClipBlock(Player player) {
        this.playerName = player.getName();
        this.playerLocation = player.getLocation();
    }

    public ClipBlock(Player player, List<Block> blocks, Location minLocation) {
        this(blocks, minLocation);
        this.playerName = player.getName();
        this.playerLocation = player.getLocation();
    }

    public ClipBlock(List<Block> blocks, Location minLocation) {
        this();
        this.minLocation = minLocation;
        this.blocks = new LinkedList<>();
        if (this.minLocation == null) {
            this.blocks.addAll(blocks);
        } else {
            for (Block b : blocks) {
                this.blocks.add(Block.get(b.getId(), b.getDamage(), b.getLocation().subtract(this.minLocation)));
            }
        }
    }

    public ClipBlock(Area area) {
        this();
        this.minLocation = area.getMin();
        for (int chX = area.getChunkX1(); chX <= area.getChunkX2(); chX++)
            for (int chZ = area.getChunkZ1(); chZ <= area.getChunkZ2(); chZ++)
                for (int y = area.getY1(); y <= area.getY2(); y++) {
                    for (int x = 0; x < 16; x++)
                        for (int z = 0; z < 16; z++) {
                            int blockX = x + (chX << 4);
                            int blockZ = z + (chZ << 4);
                            if (blockX < area.getX1() || blockX > area.getX2()) continue;
                            if (blockZ < area.getZ1() || blockZ > area.getZ2()) continue;
                            this.add(new Location(blockX, y, blockZ, area.getLevel()));
                        }
                }
    }

    public ClipBlock(Location loc1, Location loc2) {
        this(new Area(loc1, loc2));
    }


    public void add(Block block) {
        this.blocks.add(minLocation == null ? block : Block.get(block.getId(), block.getDamage(), block.getLocation().subtract(minLocation)));
    }

    public void add(Block... block) {
        for (Block b : block) add(b);
    }

    public void add(Location blockLocation) {
        add(blockLocation.getLevelBlock());
    }
    public void add(Location... blockLocation) {
        Arrays.stream(blockLocation).forEach(location -> {
            add(location.getLevelBlock());
        });
    }

    public void paste() {
        paste(false);
    }

    @Override
    public void paste(boolean useUndo) {
        Regions.getBuilder().setBlock(playerName, blocks, useUndo);
    }

    public void paste(Location loc, boolean asPlayer) {
        paste(loc, asPlayer, false);
    }

    public void paste(Location loc, boolean asPlayer, boolean useUndo) {
        Location start = asPlayer && this.playerLocation != null ? loc.add(this.minLocation.subtract(this.playerLocation)) : loc;
        List<Block> blocks = new LinkedList<>();
        for (Block block : this.blocks) {
            blocks.add(Block.get(block.getId(), block.getDamage(), start.add(block)));
        }
        Regions.getBuilder().setBlock(playerName, blocks, useUndo);
    }

    @Override
    public boolean isEmpty() {
        return blocks.isEmpty();
    }

    public void remove(List<Block> blocks) {
        this.blocks.removeAll(blocks);
    }

    public void remove(Clipboard removeClip) {
        if (removeClip instanceof ClipBlock) {
            this.blocks.removeAll(((ClipBlock) removeClip).blocks);
        }
    }

    @Override
    public int getVolume() {
        return blocks.size();
    }

}