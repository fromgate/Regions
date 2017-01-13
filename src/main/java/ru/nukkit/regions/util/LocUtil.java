package ru.nukkit.regions.util;

import cn.nukkit.Player;
import cn.nukkit.level.Location;
import ru.nukkit.regions.RegionsPlugin;

public class LocUtil {

    private static Location getEyeLocation(Player player) {
        return new Location(player.getX(), player.getY() + player.getEyeHeight(), player.getZ(), player.getYaw(), player.getPitch(), player.getLevel());
    }

    public static int getWorldHeight () {
        return RegionsPlugin.getCfg().worldHeight == 256 ? 256 : 128;
    }

    public static boolean isHigherThanWorld (double y) {
        return  (y >= getWorldHeight());
    }

    public static boolean isHigherThanWorld(int y) {
        return (y >= getWorldHeight());
    }

    public static boolean isHigherThanWorld (Location location) {
        return isHigherThanWorld (location.getY());
    }

}