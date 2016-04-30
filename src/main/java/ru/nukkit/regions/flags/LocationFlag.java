package ru.nukkit.regions.flags;

import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import ru.nukkit.regions.util.Relation;

import java.util.regex.Pattern;

public class LocationFlag extends Flag {
    private static final Pattern LOC_PATTERN = Pattern.compile("\\S+,\\s*(\\d+\\.\\d+|\\d+),\\s*(\\d+\\.\\d+|\\d+),\\s*(\\d+\\.\\d+|\\d+)(,\\s*(\\d+\\.\\d+|\\d+),\\s*(\\d+\\.\\d+|\\d+))?");
    private Location loc;

    public LocationFlag(FlagType flagType, Relation relation) {
        super(flagType, relation);
        this.loc = null;
    }

    @Override
    public boolean parseParam(String parameter) {
        if (!LOC_PATTERN.matcher(parameter).matches()) return false;
        String[] ln = parameter.split(",\\s*");
        Level level = Server.getInstance().getLevelByName(ln[0]);
        double x = Double.parseDouble(ln[1]);
        double y = Double.parseDouble(ln[2]);
        double z = Double.parseDouble(ln[3]);
        double yaw = ln.length == 6 ? Double.parseDouble(ln[4]) : 0;
        double pitch = ln.length == 6 ? Double.parseDouble(ln[5]) : 0;
        this.loc = new Location(x, y, z, yaw, pitch, level);
        return true;
    }

    @Override
    public String getParam() {
        StringBuilder sb = new StringBuilder(loc.getLevel().getName());
        sb.append(", ").append(loc.getX());
        sb.append(", ").append(loc.getY());
        sb.append(", ").append(loc.getZ());
        if (loc.getYaw() != 0 || loc.getPitch() != 0) {
            sb.append(", ").append(loc.getYaw());
            sb.append(", ").append(loc.getPitch());
        }
        return sb.toString();
    }

    @Override
    public Object getValue() {
        return null;
    }
}
