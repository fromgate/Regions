package ru.nukkit.regions.builder;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import ru.nukkit.regions.RegionsPlugin;
import ru.nukkit.regions.areas.Area;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Builder {
    public Builder(){
        this.queues = new LinkedList <BlockQueue>();
        this.active = false;
        this.undoManager = new Undo();
        this.clipboard = new ClipboardManager();
    }

    private boolean active;
    private List<BlockQueue> queues;
    private Undo undoManager;
    private ClipboardManager clipboard;

    private void add (String player, Block block){
        String name = player==null||player.isEmpty() ? "CONSOLE" : player;
        this.queues.add(new BlockQueue(name));
        processQueue();
    }
    private void add (String player, Collection<Block> blocks){
        String name = player==null||player.isEmpty() ? "CONSOLE" : player;
        this.queues.add(new BlockQueue(name,blocks));
        processQueue();
    }

    public void setBlock(Player player, Block block){
        setBlock(player.getName(), block);
    }
    public void setBlock(String playerName, Block block){
        add(playerName, block);
    }

    public void replaceBlock(Player player, Block b1, Block b2, boolean compareData, Location loc1, Location loc2) {
        replaceBlock (player.getName(),b1,b2,compareData,loc1,loc2);
    }
    public void replaceBlock(String playerName, Block b1, Block b2, boolean compareData, Location loc1, Location loc2) {
        Area area = new Area (loc1,loc2);
        List<Block> blocks = new LinkedList<Block>();
        int chX1 = area.getX1()>>4;
        int chZ1 = area.getZ1()>>4;
        int chX2 = area.getX2()>>4;
        int chZ2 = area.getZ2()>>4;
        Clipboard undo = Clipboard.createUndoClipBoard(playerName);
        for (int chX=chX1;chX<=chX2;chX++)
            for (int chZ=chZ1;chZ<=chZ2;chZ++){
                for (int x = 0;x<16;x++)
                    for (int z = 0;z<16;z++){
                        int blockX = x+(chX<<4);
                        int blockZ = z+(chZ<<4);
                        if (blockX<area.getX1()||blockX>area.getX2()) continue;
                        if (blockZ<area.getZ1()||blockZ>area.getZ2()) continue;
                        for (int y = area.getY1(); y<=area.getY2();y++){
                            Position position = new Position(blockX,y,blockZ,area.getLevel());
                            Block block = position.getLevel().getBlock(position);
                            if (block.getId()!=b1.getId()) continue;
                            if (compareData&&block.getDamage()!=b1.getDamage()) continue;
                            if (undo!=null) undo.add(position.getLevel().getBlock(position));
                            blocks.add(Block.get(b2.getId(),b2.getDamage(),position));
                        }
                    }
            }
        if (undo!=null) getUndoManager().add(undo);
        setBlock(playerName,blocks);
    }

    public void setBlock(Player player, Block block, Location loc1, Location loc2){
        setBlock (player.getName(),block,loc1,loc2);
    }

    public void setBlock(String playerName, Block block, Location loc1, Location loc2){
        Area area = new Area (loc1,loc2);
        List<Block> blocks = new LinkedList<Block>();
        int chX1 = area.getX1()>>4;
        int chZ1 = area.getZ1()>>4;
        int chX2 = area.getX2()>>4;
        int chZ2 = area.getZ2()>>4;
        Clipboard undo = Clipboard.createUndoClipBoard(playerName);
        for (int chX=chX1;chX<=chX2;chX++)
            for (int chZ=chZ1;chZ<=chZ2;chZ++){
                for (int x = 0;x<16;x++)
                    for (int z = 0;z<16;z++){
                        int blockX = x+(chX<<4);
                        int blockZ = z+(chZ<<4);
                        if (blockX<area.getX1()||blockX>area.getX2()) continue;
                        if (blockZ<area.getZ1()||blockZ>area.getZ2()) continue;
                        for (int y = area.getY1(); y<=area.getY2();y++){
                            Position position =new Position(blockX, y, blockZ, area.getLevel());
                            if (undo!=null) undo.add(position.getLevel().getBlock(position));
                            blocks.add(Block.get(block.getId(), block.getDamage(), position));
                        }
                    }
            }
        if (undo!=null) getUndoManager().add(undo);
        setBlock(playerName,blocks);
    }

    public void setBlock(Player player, Collection<Block> blocks){
        add(player.getName(),blocks);
    }
    public void setBlock(String playerName, Collection<Block> blocks){
        add(playerName,blocks);
    }


    public void processQueue(){
        if (active) return;
        if (queues.isEmpty()) return;
        active = true;
        long startTime = System.currentTimeMillis();
        Iterator<BlockQueue> it = queues.iterator();
        // 1000 ms = 20 ticks / 1 tick = 50 ms /
        while (it.hasNext()&&System.currentTimeMillis() - startTime < RegionsPlugin.getCfg().builderTicks){
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
        },1);
    }


    public Block getNewBlock (String typeStr, Location loc){
        Block block = getNewBlock(typeStr);
        if (block == null) return null;
        block.setLevel(loc.getLevel());
        block.setComponents(loc.getX(),loc.getY(),loc.getZ());
        return block;
    }

    public Block getNewBlock (String typeStr){
        int id=-1;
        Integer data = 0;

        if (typeStr.contains(":")) {
            String[] ln = typeStr.split(":");
            typeStr = ln[0];
            data = Integer.parseInt(ln[1]);
        }
        if (typeStr.matches("\\d+"))
            id = Integer.parseInt(typeStr);
        else {
            try {
                Field f = Block.class.getDeclaredField(typeStr.toUpperCase());
                id = f.getInt(null);
            } catch (Exception e) {
            }
        }
        if (id<0) return null;
        return Block.get(id,data);
    }

    public Undo getUndoManager() {
        return undoManager;
    }

    public ClipboardManager getClipboard(){
        return this.clipboard;
    }
}