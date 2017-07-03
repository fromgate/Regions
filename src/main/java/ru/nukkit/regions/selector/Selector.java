package ru.nukkit.regions.selector;

import cn.nukkit.Player;
import cn.nukkit.level.Location;
import ru.nukkit.regions.areas.Area;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Selector {
    public Selector() {
        this.mode = new HashSet<>();
        this.p1 = new HashMap<>();
        this.p2 = new HashMap<>();
    }

    private Set<String> mode;

    private Map<String, Location> p1;
    private Map<String, Location> p2;

    public boolean selectPoint(Player player, Location loc, boolean p1) {
        return p1 ? selectP1(player, loc) : selectP2(player, loc);
    }

    public boolean isSelectionFinished(Player player) {
        String playerName = player.getName();
        if (!p1.containsKey(playerName)) return false;
        if (!p2.containsKey(playerName)) return false;
        return p1.get(playerName).getLevel().equals(p2.get(playerName).getLevel());
    }


    public boolean selectPoints(Player player, Location loc1, Location loc2) {
        return selectP1(player, loc1) && selectP2(player, loc2);
    }

    public boolean selectP1(Player player, Location loc) {
        if (loc == null || player == null) return false;
        Location l = new Location(loc.getFloorX(), loc.getFloorY(), loc.getFloorZ(), 0, 0, loc.getLevel());
        String playerName = player.getName();
        p1.put(playerName, loc);
        return true;
    }

    public boolean selectP2(Player player, Location loc) {
        if (loc == null || player == null) return false;
        Location l = new Location(loc.getFloorX(), loc.getFloorY(), loc.getFloorZ(), 0, 0, loc.getLevel());
        String playerName = player.getName();
        p2.put(playerName, loc);
        return true;
    }

    public boolean isSelected(Player player, Location loc) {
        String name = player.getName();
        List<Location> sel = getPoints(player);
        for (Location l : sel) {
            if (!l.getLevel().equals(loc.getLevel())) continue;
            if (l.getFloorX() != loc.getFloorX()) continue;
            if (l.getFloorZ() != loc.getFloorZ()) continue;
            if (l.getFloorY() != loc.getFloorY()) continue;
            return true;
        }
        return false;
    }

    public void clearSelection(Player player) {
        String playerName = player.getName();
        if (p1.containsKey(playerName)) p1.remove(playerName);
        if (p2.containsKey(playerName)) p2.remove(playerName);
    }

    public List<Location> getPoints(Player player) {
        List<Location> locs = new ArrayList<>();
        String playerName = player.getName();
        if (p1.containsKey(playerName) && p1.get(playerName) != null) locs.add(p1.get(playerName));
        if (p2.containsKey(playerName) && p2.get(playerName) != null) locs.add(p2.get(playerName));
        return locs;
    }

    public boolean toggleSelMode(Player player) {
        return setSelMode(player, !selMode(player));
    }

    public boolean setSelMode(Player player, boolean sel) {
        String name = player.getName();
        if (sel) mode.add(name);
        else if (mode.contains(name)) mode.remove(name);
        return sel;
    }

    public boolean selMode(Player player) {
        return mode.contains(player.getName());
    }


    public Set<String> getActivePlayers() {
        return mode;
    }

    public int getSelectionVolume(Player player) {
        List<Location> locs = getPoints(player);
        if (locs.size() != 2) return -1;
        int dx = locs.get(1).getFloorX() - locs.get(0).getFloorX();
        int dy = locs.get(1).getFloorY() - locs.get(0).getFloorY();
        int dz = locs.get(1).getFloorZ() - locs.get(0).getFloorZ();
        return dx * dy * dz;
    }

    public Area getSelectedArea(Player player) {
        if (!isSelectionFinished(player)) return null;
        return new Area(getPoints(player).get(0), getPoints(player).get(1));
    }

    public boolean selectArea(Player player, Area area) {
        return selectPoints(player, area.getLoc1(), area.getLoc2());
    }
}