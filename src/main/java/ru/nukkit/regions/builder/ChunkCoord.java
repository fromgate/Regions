package ru.nukkit.regions.builder;

import cn.nukkit.level.Location;

/**
 * Created by Igor on 30.04.2016.
 */
public class ChunkCoord {
    int x;
    int z;

    public ChunkCoord(Location location) {
        this.x = location.getFloorX() >> 4;
        this.z = location.getFloorZ() >> 4;
    }

    public ChunkCoord(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }


}
