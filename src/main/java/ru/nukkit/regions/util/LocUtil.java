package ru.nukkit.regions.util;

import cn.nukkit.Player;
import cn.nukkit.level.Location;

public class LocUtil {

    private static Location getEyeLocation (Player player){
        return new Location(player.getX(),player.getY()+player.getEyeHeight(),player.getZ(),player.getYaw(),player.getPitch(),player.getLevel());
    }



}