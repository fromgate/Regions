package ru.nukkit.regions.builder;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import ru.nukkit.regions.Regions;
import ru.nukkit.regions.areas.Area;
import ru.nukkit.regions.clipboard.Clipboard;
import ru.nukkit.regions.util.Message;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static ru.nukkit.regions.Regions.getUndoManager;

/**
 * Created by Igor on 23.04.2016.
 */
public class SimpleBuilder implements Builder {
    @Override
    public void setBlock(Player player, Block block) {
        block.getLevel().setBlock(block, block);
    }

    @Override
    public void setBlock(String playerName, Block block) {
        setBlock(playerName, block, true);

    }

    public void setBlock(String playerName, Block block, boolean direct) {
        block.getLevel().setBlock(block, block, direct);
    }

    @Override
    public void replaceBlock(Player player, Block b1, Block b2, boolean compareData, Location loc1, Location loc2) {
        replaceBlock(player.getName(), b1, b2, compareData, loc1, loc2);
    }

    @Override
    public void replaceBlock(String playerName, Block b1, Block b2, boolean compareData, Location loc1, Location loc2) {
        Area area = new Area(loc1, loc2);
        List<Block> blocks = new LinkedList<>();
        ChunkCoord ch1 = new ChunkCoord(area.getLoc1());
        ChunkCoord ch2 = new ChunkCoord(area.getLoc1());
        int chX1 = area.getX1() >> 4;
        int chZ1 = area.getZ1() >> 4;
        int chX2 = area.getX2() >> 4;
        int chZ2 = area.getZ2() >> 4;
        Clipboard undo = Clipboard.createUndoClipBoard(playerName);
        for (int chX = chX1; chX <= chX2; chX++)
            for (int chZ = chZ1; chZ <= chZ2; chZ++)
                for (int x = 0; x < 16; x++)
                    for (int z = 0; z < 16; z++) {
                        int blockX = x + (chX << 4);
                        int blockZ = z + (chZ << 4);
                        if (blockX < area.getX1() || blockX > area.getX2()) continue;
                        if (blockZ < area.getZ1() || blockZ > area.getZ2()) continue;
                        for (int y = area.getY1(); y <= area.getY2(); y++) {
                            Position position = new Position(blockX, y, blockZ, area.getLevel());
                            Block block = position.getLevel().getBlock(position);
                            if (b1 == null) if (block.getId() != 0) continue;
                            else {
                                if (block.getId() != b1.getId()) continue;
                                if (compareData && block.getDamage() != b1.getDamage()) continue;
                            }
                            if (undo != null) undo.add(position.getLevel().getBlock(position));
                            setBlock(playerName, Block.get(b2.getId(), b2.getDamage(), position));
                        }
                    }
        if (undo != null) getUndoManager().add(undo);
    }

    @Override
    public void setBlock(Player player, Block block, Location loc1, Location loc2) {
        setBlock(player.getName(), block, loc1, loc2);
    }

    @Override
    public void setBlock(String playerName, Block block, Location loc1, Location loc2) {
        Area area = new Area(loc1, loc2);
        List<Block> blocks = new LinkedList<>();
        int chX1 = area.getX1() >> 4;
        int chZ1 = area.getZ1() >> 4;
        int chX2 = area.getX2() >> 4;
        int chZ2 = area.getZ2() >> 4;
        Clipboard undo = Clipboard.createUndoClipBoard(playerName);
        for (int chX = chX1; chX <= chX2; chX++)
            for (int chZ = chZ1; chZ <= chZ2; chZ++) {
                for (int x = 0; x < 16; x++)
                    for (int z = 0; z < 16; z++) {
                        int blockX = x + (chX << 4);
                        int blockZ = z + (chZ << 4);
                        if (blockX < area.getX1() || blockX > area.getX2()) continue;
                        if (blockZ < area.getZ1() || blockZ > area.getZ2()) continue;
                        for (int y = area.getY1(); y <= area.getY2(); y++) {
                            Position position = new Position(blockX, y, blockZ, area.getLevel());
                            if (undo != null) undo.add(position.getLevel().getBlock(position));
                            blocks.add(Block.get(block.getId(), block.getDamage(), position));
                        }
                    }
            }
        if (undo != null) getUndoManager().add(undo);
        Message.debugMessage("setBlock:", "blocks:", blocks.size(), "undo:", undo == null ? "null" : undo.getVolume());
        setBlock(playerName, blocks);
    }

    @Override
    public void setBlock(Player player, Collection<Block> blocks) {
        setBlock(player.getName(), blocks);
    }

    @Override
    public void setBlock(String playerName, Collection<Block> blocks) {
        setBlock(playerName, blocks, false);
    }

    @Override
    public void setBlock(String playerName, Collection<Block> blocks, boolean useUndo) {
        boolean direct = blocks.size() < 100;
        Message.debugMessage("Set Blocks:", blocks.size(), "direct:", direct);
        Clipboard undo = useUndo ? Clipboard.createUndoClipBoard(playerName) : null;
        blocks.forEach(block -> {
            if (undo != null) undo.add(block.getLevelBlock());
            setBlock(playerName, block, direct);
        });
        if (undo != null) getUndoManager().add(undo);
    }

    @Override
    public void setBlockBox(Player player, Block block, Location l1, Location l2) {
        setBlockBox(player.getName(), block, l1, l2);
    }

    @Override
    public void setBlockBox(String playerName, Block block, Location l1, Location l2) {
        Area area = new Area(l1, l2);
        List<Block> blocks = new LinkedList<>();
        Clipboard undo = Clipboard.createUndoClipBoard(playerName);
        for (int x = area.getX1(); x <= area.getX2(); x++)
            for (int z = area.getZ1(); z <= area.getZ2(); z++)
                for (int y = area.getY1(); y <= area.getY2(); y++) {
                    if (x != l1.getFloorX() && x != l2.getFloorX() &&
                            z != l1.getFloorZ() && z != l2.getFloorZ() &&
                            y != l1.getFloorY() && y != l2.getFloorY()) continue;
                    Position position = new Position(x, y, z, area.getLevel());
                    if (undo != null) undo.add(position.getLevel().getBlock(position));
                    setBlock(playerName, Block.get(block.getId(), block.getDamage(), position));
                }
        if (undo != null) Regions.getUndoManager().add(undo);
    }

    @Override
    public void setBlockWall(Player player, Block block, Location l1, Location l2) {
        setBlockBox(player.getName(), block, l1, l2);
    }

    @Override
    public void setBlockWall(String playerName, Block block, Location l1, Location l2) {
        Area area = new Area(l1, l2);
        Clipboard undo = Clipboard.createUndoClipBoard(playerName);
        for (int y = area.getY1(); y <= area.getY2(); y++) {
            for (int x = area.getX1(); x <= area.getX2(); x++) {
                Position p1 = new Position(x, y, area.getZ1(), area.getLevel());
                Position p2 = new Position(x, y, area.getZ1(), area.getLevel());
                if (undo != null) undo.add(p1.getLevel().getBlock(p1), p1.getLevel().getBlock(p2));
                setBlock(playerName, Block.get(block.getId(), block.getDamage(), p1));
                setBlock(playerName, Block.get(block.getId(), block.getDamage(), p2));
            }
            for (int z = area.getZ1(); z <= area.getZ2(); z++) {
                Position p1 = new Position(area.getX1(), area.getY1(), z, area.getLevel());
                Position p2 = new Position(area.getX2(), area.getY2(), z, area.getLevel());
                if (undo != null) undo.add(p1.getLevel().getBlock(p1), p1.getLevel().getBlock(p2));
                setBlock(playerName, Block.get(block.getId(), block.getDamage(), p1));
                setBlock(playerName, Block.get(block.getId(), block.getDamage(), p2));
            }
        }
        if (undo != null) Regions.getUndoManager().add(undo);
    }
}
