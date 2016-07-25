package ru.nukkit.regions.areas;

import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;

import java.util.regex.Pattern;

public class Area {
    private static final Pattern AREA_TEXT_PATTERN = Pattern.compile("(?i)^cuboid:\\{level:\\s?\\S+\\s+x1:\\s?-?\\d+\\s+y1:\\s?\\d+\\s+z1:\\s?-?\\d+\\s+x2:\\s?-?\\d+\\s+y2:\\s?\\d+\\s+z2:\\s?-?\\d+\\}$");

    private String world;
    private int x1;
    private int y1;
    private int z1;
    private int x2;
    private int y2;
    private int z2;

    public Area(Level level, int x1, int y1, int z1, int x2, int y2, int z2) {
        this(level.getName(), x1, y1, z1, x2, y2, z2);
    }

    public Area(String levelName, int x1, int y1, int z1, int x2, int y2, int z2) {
        this.world = levelName;
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;
        updateMinMax();
    }

    public Area(Location loc1, Location loc2) {
        this(loc1.getLevel(), loc1.getFloorX(), loc1.getFloorY(), loc1.getFloorZ()
                , loc2.getFloorX(), loc2.getFloorY(), loc2.getFloorZ());
    }

    public Area(String areaStr) {
        if (!AREA_TEXT_PATTERN.matcher(areaStr).matches())
            throw new IllegalArgumentException("Wrong area definition format: " + areaStr);
        String[] ln = areaStr.replaceAll("(?i)(^cuboid:\\{)|(\\}$)", "").split("\\s+");
        for (String s : ln) {
            if (s.startsWith("level:")) this.world = s.replace("level:", "");
            else if (s.startsWith("x1:")) this.x1 = Integer.parseInt(s.replace("x1:", ""));
            else if (s.startsWith("y1:")) this.y1 = Integer.parseInt(s.replace("y1:", ""));
            else if (s.startsWith("z1:")) this.z1 = Integer.parseInt(s.replace("z1:", ""));
            else if (s.startsWith("x2:")) this.x2 = Integer.parseInt(s.replace("x2:", ""));
            else if (s.startsWith("y2:")) this.y2 = Integer.parseInt(s.replace("y2:", ""));
            else if (s.startsWith("z2:")) this.z2 = Integer.parseInt(s.replace("z2:", ""));
        }
    }

    public Location getMin() {
        Level l = getLevel();
        return l == null ? null : new Location(Math.min(x1, x2), Math.min(y1, y2), Math.min(z1, z2), 0, 0, l);
    }

    public Location getMax() {
        Level l = getLevel();
        return l == null ? null : new Location(Math.max(x1, x2), Math.max(y1, y2), Math.max(z1, z2), 0, 0, l);
    }

    public Level getLevel() {
        return Server.getInstance().getLevelByName(world);
    }

    public int getX1() {
        return this.x1;
    }

    public int getY1() {
        return this.y1;
    }

    public int getZ1() {
        return this.z1;
    }

    public int getX2() {
        return this.x2;
    }

    public int getY2() {
        return this.y2;
    }

    public int getZ2() {
        return this.z2;
    }

    public int getSizeX() {
        return this.x2 - this.x1;
    }

    public int getSizeY() {
        return this.y2 - this.y1;
    }

    public int getSizeZ() {
        return this.z2 - this.z1;
    }

    public void setX1(int x1) {
        this.x1 = x1;
        updateMinMax();
    }

    public void setY1(int y1) {
        this.y1 = y1;
        updateMinMax();
    }

    public void setZ1(int z1) {
        this.z1 = z1;
        updateMinMax();
    }

    public void setX2(int x2) {
        this.x2 = x2;
        updateMinMax();
    }

    public void setY2(int y2) {
        this.y2 = y2;
        updateMinMax();
    }

    public void setZ2(int z2) {
        this.z2 = z2;
        updateMinMax();
    }

    private void updateMinMax() {
        int x1 = this.x1;
        int y1 = this.y1;
        int z1 = this.z1;
        int x2 = this.x2;
        int y2 = this.y2;
        int z2 = this.z2;

        this.x1 = Math.min(x1, x2);
        this.y1 = Math.min(y1, y2);
        this.z1 = Math.min(z1, z2);

        this.x2 = Math.max(x1, x2);
        this.y2 = Math.max(y1, y2);
        this.z2 = Math.max(z1, z2);
        if (y1 <= 0) this.y1 = 0;
        if (y2 <= 0) this.y2 = 0;
        if (y1 > 127) this.y1 = 127;
        if (y2 > 127) this.y2 = 127;
    }

    public Location getLoc2() {
        Level level = Server.getInstance().getLevelByName(world);
        if (level == null) return null;
        return new Location(x2, y2, z2, 0, 0, level);
    }

    public Location getLoc1() {
        Level level = Server.getInstance().getLevelByName(world);
        if (level == null) return null;
        return new Location(x1, y1, z1, 0, 0, level);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("cuboid:{");
        sb.append("level:").append(world);
        sb.append(" x1:").append(x1);
        sb.append(" y1:").append(y1);
        sb.append(" z1:").append(z1);
        sb.append(" x2:").append(x2);
        sb.append(" y2:").append(y2);
        sb.append(" z2:").append(z2);
        sb.append("}");
        return sb.toString();
    }

    public int getChunkX1() {
        return this.getX1() >> 4;
    }

    public int getChunkX2() {
        return this.getX2() >> 4;
    }

    public int getChunkZ1() {
        return this.getZ1() >> 4;
    }

    public int getChunkZ2() {
        return this.getZ2() >> 4;
    }

    public boolean isInside(Location loc) {
        Level l = this.getLevel();
        if (l == null) return false;
        if (!l.equals(loc.getLevel())) return false;

        int locX = loc.getFloorX();
        int locY = loc.getFloorY();
        int locZ = loc.getFloorZ();

        if (locX < x1 || locX > x2) return false;
        if (locZ < z1 || locZ > z2) return false;
        return locY >= y1 && locY <= y2;
    }

    public boolean intersect(Area area) {
        if (this.getX2() < area.getX1()) return false;
        if (this.getX1() > area.getX2()) return false;
        if (this.getY2() < area.getY1()) return false;
        if (this.getY1() > area.getY2()) return false;
        if (this.getZ2() < area.getZ1()) return false;
        if (this.getZ1() > area.getZ2()) return false;
        return true;
    }

    public int getVolume() {
        return (getX2() - getX1()) * (getZ2() - getZ1()) * (getY2() - getY1());
    }
}