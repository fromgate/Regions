package ru.nukkit.regions.util;

import cn.nukkit.block.Block;
import cn.nukkit.item.Item;
import cn.nukkit.level.Location;
import cn.nukkit.utils.TextFormat;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Igor on 21.04.2016.
 */
public class BlockUtil {

    private static Set<Integer> doors;

    public static void init() {
        fillDoors();

    }

    private static void fillDoors() {
        doors = new HashSet<Integer>();
        for (Field f : Block.class.getDeclaredFields()) {
            if (f.getName().contains("DOOR_BLOCK") ||
                    f.getName().contains("FENCE_GATE") ||
                    f.getName().contains("TRAPDOOR"))
                try {
                    doors.add(f.getInt(null));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
        }
    }

    public static boolean isDoor(Block block) {
        return doors.contains(block.getId());
    }

    public static boolean isDoor(int blockId) {
        return doors.contains(blockId);
    }

    public static Block getNewBlock(String typeStr, Location loc) {
        Block block = getNewBlock(typeStr);
        if (block == null) return null;
        block.setLevel(loc.getLevel());
        block.setComponents(loc.getX(), loc.getY(), loc.getZ());
        return block;
    }

    public static Block getNewBlock(String typeStr) {
        int id = -1;
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
        if (id < 0) return null;
        return Block.get(id, data);
    }


    public static Item createItem(String itemStr, String itemName) {
        int id = -1;
        Integer data = 0;
        if (itemStr.contains(":")){
            String[] ln = itemStr.split(":");
            itemStr = ln[0];
            data = Integer.parseInt(ln[1]);
        }
        if (itemStr.matches("\\d+"))
            id = Integer.parseInt(itemStr);
        else {
            try {
                Field f = Item.class.getDeclaredField(itemStr.toUpperCase());
                id = f.getInt(null);
            } catch (Exception e) {
            }
        }
        if (id < 0) return null;
        Item item = Item.get(id, data);
        if (itemName!=null&&!itemName.isEmpty()) item.setCustomName(TextFormat.colorize(itemName));
        return item;
    }

}
