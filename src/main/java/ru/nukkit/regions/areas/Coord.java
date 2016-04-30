package ru.nukkit.regions.areas;

import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;

public class Coord {
    private String world;
    private int x;
    private int y;
    private int z;

    public Coord(Location loc) {
        this.world = loc.getLevel().getName();
        this.x = loc.getFloorX();
        this.y = loc.getFloorY();
        this.z = loc.getFloorY();
    }

    public Coord(Level level, int x, int y, int z) {
        this.world = level.getName();
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Coord(String level, int x, int y, int z) {
        this.world = level;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Location getLocation() {
        Level level = Server.getInstance().getLevelByName(world);
        if (level == null) return null;
        Location l = new Location(x, y, z);
        l.setLevel(level);
        return l;
    }

    public Level getLevel() {
        return Server.getInstance().getLevelByName(world);
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    @Override
    public String toString() {
        return new StringBuilder(world).append(", ").append(x).append(", ").append(y).append(", ").append(z).toString();
    }


}
