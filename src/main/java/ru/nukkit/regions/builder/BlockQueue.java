package ru.nukkit.regions.builder;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import ru.nukkit.regions.RegionsPlugin;
import ru.nukkit.regions.util.Message;

import java.util.*;

public class BlockQueue {

    private String starter;
    private long startTime;
    private int ticks;
    private int count;
    private Location minLoc; //add this to block coordinates if not null

    BlockQueue(String name) {
        this.queue = new ArrayList<Block>();//LinkedList<Block>();
        this.starter = name;
        this.startTime = System.currentTimeMillis();
        this.ticks = 0;
        this.count = 0;
        this.minLoc = null;
    }

    public BlockQueue(String name, Block block) {
        this(name);
        add(block);
    }

    public BlockQueue(String name, Collection<Block> blocks) {
        this(name);
        add(blocks);
    }

    public BlockQueue(String name, Collection<Block> blocks, Location minLoc) {
        this(name);
        this.minLoc = minLoc;
        add(blocks);
    }

    private List<Block> queue;

    private boolean add(Block block) {
        if (!block.isValid()) return false;
        queue.add(block);
        return true;
    }

    private boolean add(Collection<Block> blocks) {
        queue.addAll(blocks);
        return true;
    }

    public boolean processQueue(long time) {
        if (queue.isEmpty()) return true;
        ticks++;
        for (; count < queue.size() && (System.currentTimeMillis() - time) < RegionsPlugin.getCfg().builderTicks; ) {
            Block b = queue.get(count);
            if (this.minLoc != null) b = Block.get(b.getId(), b.getDamage(), b.getLocation().add(this.minLoc));
            if (b.isValid()) b.getLevel().setBlock(b, b, false, false);
            count++;
        }
        return true;
    }

    public boolean isFinished() {
        return count >= queue.size();
    }

    public void finishInform() {
        Player player = Server.getInstance().getPlayerExact(this.starter);
        if (player != null)
            Message.BUILD_FINISHED.print(player, queue.size(), System.currentTimeMillis() - this.startTime, ticks);
        if (player == null || queue.size() > RegionsPlugin.getCfg().builderLogAmount)
            Message.BUILD_FINISHED_LOG.log(this.starter, queue.size(), System.currentTimeMillis() - this.startTime, ticks);
    }

    public void scheduleUpdateBlocks() {
        if (queue.isEmpty()) return;
        Level l = queue.get(0).getLevel();
        for (Block b : queue)
            l.scheduleUpdate(b, 1);
    }

    private void sortQueue() {
        Map<Integer, List<Block>> groupQueue = new TreeMap<Integer, List<Block>>();
        for (Block b : queue) {
            int hash = (b.getFloorX() >> 4) ^ (b.getFloorZ() >> 4);
            if (!groupQueue.containsKey(hash)) groupQueue.put(hash, new LinkedList<Block>());
            List<Block> chunkBlock = groupQueue.get(hash);
            chunkBlock.add(b);
        }
        queue = new LinkedList<Block>();
        for (Map.Entry<Integer, List<Block>> entry : groupQueue.entrySet()) {
            queue.addAll(entry.getValue());
        }
    }
}
