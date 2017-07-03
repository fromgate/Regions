package ru.nukkit.regions.brush;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.item.Item;
import cn.nukkit.utils.TextFormat;
import ru.nukkit.regions.RegionsPlugin;
import ru.nukkit.regions.brush.brushes.Brush;
import ru.nukkit.regions.util.BlockUtil;

import java.util.Map;
import java.util.TreeMap;

public class BrushManager {
    private static Map<String, Brush> artists = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);


    // Command brush <brushName> r <radius> <brushParams...>
    public static boolean takeBrush(Player player, String[] args) {
        if (player == null) return false;
        artists.remove(player.getName());
        if (args == null || args.length == 0) return false;
        Brush brush = getBrush(args[0], removeArg(args));
        if (brush == null) return false;
        artists.put(player.getName(), brush);
        return true;
    }

    private static Brush getBrush(String arg, String[] strings) {
        BrushType bt = BrushType.getByName(arg);
        if (bt == null) return null;
        return bt.createBrush(strings);
    }

    private static String[] removeArg(String[] args) {
        if (args.length <= 1) return new String[]{};
        String[] newArgs = new String[args.length - 1];
        System.arraycopy(args, 1, newArgs, 0, args.length - 1);
        return newArgs;
    }

    public static Item createBrushItem() {
        return BlockUtil.createItem(RegionsPlugin.getCfg().brushItem,
                TextFormat.colorize(RegionsPlugin.getCfg().brushName));
    }

    public static boolean isBrush(Item item) {
        if (item == null) return false;
        Item brush = createBrushItem();
        if (brush == null) return false;
        if (item.getId() != brush.getId()) return false;
        if (item.getDamage() != brush.getDamage()) return false;
        if (RegionsPlugin.getCfg().brushName.isEmpty()) return true;
        return (item.getCustomName().equals(brush.getCustomName()));
    }

    public static boolean hasBrush(Player player) {
        if (!artists.containsKey(player.getName())) return false;
        return (artists.get(player.getName()) != null);
    }

    public static boolean paint(Player player, Block clickedBlock) {
        if (!artists.containsKey(player.getName())) return false;
        Brush brush = artists.get(player.getName());
        if (brush == null) {
            artists.remove(player.getName());
            return false;
        }
        brush.paint(player, clickedBlock);
        return true;
    }

    public static boolean modifyBrush(Player player, String[] args) {
        if (!artists.containsKey(player.getName())) return false;
        return artists.get(player.getName()).init(removeArg(args));
    }

    public static Brush getBrush(Player player) {
        return artists.getOrDefault(player.getName(), null);
    }
}
