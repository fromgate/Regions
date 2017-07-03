package ru.nukkit.regions.builder;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import ru.nukkit.regions.Regions;
import ru.nukkit.regions.RegionsPlugin;
import ru.nukkit.regions.areas.Area;
import ru.nukkit.regions.clipboard.Clipboard;
import ru.nukkit.regions.util.Message;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static ru.nukkit.regions.Regions.getUndoManager;

public class BuilderQueed implements Builder {
    public BuilderQueed() {
        this.queues = new LinkedList<>();
        this.active = false;
    }

    private boolean active;
    private List<BlockQueue> queues;
    private ClipboardManager clipboard;

    private void add(String player, Block block) {
        String name = player == null || player.isEmpty() ? "CONSOLE" : player;
        this.queues.add(new BlockQueue(name));
        processQueue();
    }

    private void add(String player, Collection<Block> blocks) {
        String name = player == null || player.isEmpty() ? "CONSOLE" : player;
        this.queues.add(new BlockQueue(name, blocks));
        processQueue();
    }

    private void add(String playerName, Collection<Block> blocks, Clipboard undo) {
        if (undo == null) {
            add(playerName, blocks);
        } else {
            String name = playerName == null || playerName.isEmpty() ? "CONSOLE" : playerName;
            this.queues.add(new BlockQueue(name, blocks, undo));
            if (!undo.isEmpty()) getUndoManager().add(undo);
            processQueue();
        }
    }

    public void setBlock(Player player, Block block) {
        setBlock(player.getName(), block);
    }

    public void setBlock(String playerName, Block block) {
        add(playerName, block);
    }

    public void replaceBlock(Player player, Block b1, Block b2, boolean compareData, Location loc1, Location loc2) {
        replaceBlock(player.getName(), b1, b2, compareData, loc1, loc2);
    }

    public void replaceBlock(String playerName, Block b1, Block b2, boolean compareData, Location loc1, Location loc2) {
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
                            Block block = position.getLevel().getBlock(position);
                            if (b1 == null) {
                                if (block.getId() != 0) continue;
                            } else {
                                if (block.getId() != b1.getId()) continue;
                                if (compareData && block.getDamage() != b1.getDamage()) continue;
                            }
                            if (undo != null) undo.add(position.getLevel().getBlock(position));
                            blocks.add(Block.get(b2.getId(), b2.getDamage(), position));
                        }
                    }
            }
        if (undo != null) getUndoManager().add(undo);
        setBlock(playerName, blocks);
    }

    public void setBlock(Player player, Block block, Location loc1, Location loc2) {
        setBlock(player.getName(), block, loc1, loc2);
    }

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

    public void setBlock(Player player, Collection<Block> blocks) {
        add(player.getName(), blocks);
    }

    public void setBlock(String playerName, Collection<Block> blocks) {
        add(playerName, blocks);
    }


    @Override
    public void setBlock(String playerName, Collection<Block> blocks, boolean useUndo) {
        Clipboard undo = useUndo ? Clipboard.createUndoClipBoard(playerName) : null;
        if (undo == null) {
            add(playerName, blocks);
        } else {
            add(playerName, blocks, undo);
            getUndoManager().add(undo);
        }
    }

    public void processQueue() {
        if (active) return;
        if (queues.isEmpty()) return;
        active = true;
        long startTime = System.currentTimeMillis();
        Iterator<BlockQueue> it = queues.iterator();
        // 1000 ms = 20 ticks / 1 tick = 50 ms /
        while (it.hasNext() && System.currentTimeMillis() - startTime < RegionsPlugin.getCfg().builderTicks) {
            BlockQueue bq = it.next();
            bq.processQueue(startTime);
            if (bq.isFinished()) {
                bq.scheduleUpdateBlocks();
                bq.finishInform();
                it.remove();
            }
        }
        Server.getInstance().getScheduler().scheduleDelayedTask(new Runnable() {
            public void run() {
                active = false;
                processQueue();
            }
        }, 1);
    }

    public void setBlockBox(Player player, Block block, Location l1, Location l2) {
        setBlockBox(player.getName(), block, l1, l2);
    }

    public void setBlockBox(String playerName, Block block, Location l1, Location l2) {
        Area area = new Area(l1, l2);
        List<Block> blocks = new LinkedList<>();
        Clipboard undo = Clipboard.createUndoClipBoard(playerName);
        for (int x = area.getX1(); x <= area.getX2(); x++)
            for (int z = area.getZ1(); z <= area.getZ2(); z++)
                for (int y = area.getY1(); y <= area.getY2(); x++) {
                    if (x != l1.getFloorX() && x != l2.getFloorX() &&
                            z != l1.getFloorZ() && z != l2.getFloorZ() &&
                            y != l1.getFloorY() && y != l2.getFloorY()) continue;
                    Position position = new Position(x, y, z, area.getLevel());
                    if (undo != null) undo.add(position.getLevel().getBlock(position));
                    blocks.add(Block.get(block.getId(), block.getDamage(), position));
                }
        if (undo != null) Regions.getUndoManager().add(undo);
        setBlock(playerName, blocks);
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