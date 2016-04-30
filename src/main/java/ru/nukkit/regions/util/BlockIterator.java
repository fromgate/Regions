package ru.nukkit.regions.util;

import cn.nukkit.block.Block;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.math.Vector3;

import java.util.Iterator;

public class BlockIterator implements Iterator<Block> {

    Block currentBlock = null;
    Location current = null;
    boolean endScan = false;
    private Level level;


    Location start;
    Vector3 direction;
    int maxDistance;


    public BlockIterator(Location start, int maxDistance) {
        this.level = start.getLevel();
        this.start = cloneLoc(start);
        this.direction = pitchYawToDirection(start.getPitch(), start.getYaw());
        this.maxDistance = maxDistance;
        this.current = cloneLoc(start);
    }

    public BlockIterator(Location start, double pitch, double yaw, int maxDistance) {
        this.level = start.getLevel();
        this.start = cloneLoc(start);
        Vector3 vector = pitchYawToDirection(pitch, yaw);
        this.direction = new Vector3(vector.getX(), vector.getY(), vector.getZ()).normalize();
        this.maxDistance = maxDistance;
        this.current = cloneLoc(start);
    }


    public BlockIterator(Location start, Vector3 direction, int maxDistance) {
        this.level = start.getLevel();
        this.start = cloneLoc(start);
        this.direction = new Vector3(direction.getX(), direction.getY(), direction.getZ()).normalize();
        this.maxDistance = maxDistance;
        this.current = cloneLoc(start);
    }


    private void scanNext() {
        if (endScan) return;
        Location current = cloneLoc(this.current);
        Location newLoc = addVector(this.current, this.direction);
        this.current = newLoc;

        if (start.distance(newLoc) >= this.maxDistance) this.endScan = true;
        if (this.direction.length() == 0) endScan = true;
        if (!isSameBlock(current, newLoc) || this.currentBlock == null) {
            this.currentBlock = this.level.getBlock(new Vector3(this.current.getX(), this.current.getY(), this.current.getZ()));
        } else {
            scanNext();
        }
    }

    private Location cloneLoc(Location loc) {
        return new Location(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch(), loc.getLevel());
    }

    private Location addVector(Location loc, Vector3 vector) {
        return new Location(loc.getX() + vector.getX(), loc.getY() + vector.getY(), loc.getZ() + vector.getZ(), loc.getYaw(), loc.getPitch(), loc.getLevel());
    }

    private boolean isSameBlock(Location loc1, Location loc2) {
        if (loc1.getFloorX() != loc2.getFloorX()) return false;
        if (loc1.getFloorZ() != loc2.getFloorZ()) return false;
        return loc1.getFloorY() == loc2.getFloorY();
    }


    public boolean hasNext() {
        return !endScan;
    }

    public Block next() {
        Block b = currentBlock;
        scanNext();
        return b;
    }


    public Vector3 pitchYawToDirection(double ptc, double yw) {
        double pitch = ((ptc + 90) * Math.PI) / 180;
        double yaw = ((yw + 90) * Math.PI) / 180;
        double x = Math.sin(pitch) * Math.cos(yaw);
        double z = Math.sin(pitch) * Math.sin(yaw);
        double y = Math.cos(pitch);
        return new Vector3(x, y, z);
    }
}
