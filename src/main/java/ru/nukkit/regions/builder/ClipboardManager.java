package ru.nukkit.regions.builder;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import ru.nukkit.regions.areas.Area;
import ru.nukkit.regions.clipboard.ClipBlock;
import ru.nukkit.regions.clipboard.Clipboard;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ClipboardManager {
    private Map<String, ClipBlock> clipboards;

    public ClipboardManager() {
        this.clipboards = new TreeMap<String, ClipBlock>(String.CASE_INSENSITIVE_ORDER);
    }

    public void clear(Player player) {
        if (this.clipboards.containsKey(player.getName())) clipboards.remove(player.getName());
    }

    public boolean copy(Player player, Location loc1, Location loc2) {
        Area area = new Area(loc1, loc2);
        List<Block> blocks = new LinkedList<Block>();
        int chX1 = area.getX1() >> 4;
        int chZ1 = area.getZ1() >> 4;
        int chX2 = area.getX2() >> 4;
        int chZ2 = area.getZ2() >> 4;
        for (int chX = chX1; chX <= chX2; chX++)
            for (int chZ = chZ1; chZ <= chZ2; chZ++)
                for (int x = 0; x < 16; x++)
                    for (int z = 0; z < 16; z++) {
                        int blockX = x + (chX << 4);
                        int blockZ = z + (chZ << 4);
                        if (blockX < area.getX1() || blockX > area.getX2()) continue;
                        if (blockZ < area.getZ1() || blockZ > area.getZ2()) continue;
                        for (int y = area.getY1(); y <= area.getY2(); y++)
                            blocks.add(loc1.getLevel().getBlock(new Position(blockX, y, blockZ, area.getLevel())));
                    }
        if (blocks.isEmpty()) return false;
        ClipBlock clipboard = new ClipBlock(player, blocks, area.getMin());
        clipboards.put(player.getName(), clipboard);
        return true;
    }


    public boolean paste(Player player, boolean useUndo) {
        if (!hasClipboard(player)) return false;
        Clipboard clipboard = this.clipboards.get(player.getName());
        clipboard.paste(player.getLocation(), true, useUndo);
        return true;
    }

    public boolean paste(Player player) {
        return paste(player, false);
    }

    public boolean hasClipboard(Player player) {
        return clipboards.containsKey(player.getName()) && (clipboards.get(player.getName()).getVolume() > 0);
    }

    public int getVolume(Player player) {
        return clipboards.containsKey(player.getName()) ? clipboards.get(player.getName()).getVolume() : 0;
    }

}