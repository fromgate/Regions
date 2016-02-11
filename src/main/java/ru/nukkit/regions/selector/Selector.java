package ru.nukkit.regions.selector;

import cn.nukkit.Player;
import cn.nukkit.level.Location;

import java.util.*;

public class Selector {
    public Selector(){
        this.mode = new HashSet<String>();
        this.p1 = new HashMap<String,Location>();
        this.p2 = new HashMap<String,Location>();
    }
    private Set<String> mode;

    private Map<String,Location> p1;
    private Map<String,Location> p2;

    public boolean selectPoint (Player player, Location loc, boolean p1){
        return p1 ? selectP1 (player,loc) : selectP2 (player,loc);
    }

    public boolean selectP1 (Player player, Location loc){
        if (loc == null||player == null) return false;
        Location l = new Location(loc.getFloorX(),loc.getFloorY(),loc.getFloorZ(),0,0,loc.getLevel());
        String playerName = player.getName();
        p1.put(playerName,loc);
        return true;
    }

    public boolean selectP2 (Player player, Location loc){
        if (loc == null||player == null) return false;
        Location l = new Location(loc.getFloorX(),loc.getFloorY(),loc.getFloorZ(),0,0,loc.getLevel());
        String playerName = player.getName();
        p2.put(playerName,loc);
        return true;
    }

    public boolean isSelected (Player player, Location loc){
        String name = player.getName();
        List<Location> sel = getPoints(player);
        for (Location l : sel){
            if (!l.getLevel().equals(loc.getLevel())) continue;
            if (l.getFloorX()!=loc.getFloorX()) continue;
            if (l.getFloorZ()!=loc.getFloorZ()) continue;
            if (l.getFloorY()!=loc.getFloorY()) continue;
            return true;
        }
        return false;
    }

    public void clearSelection (Player player){
        String playerName = player.getName();
        if (p1.containsKey(playerName)) p1.remove(playerName);
        if (p2.containsKey(playerName)) p2.remove(playerName);
    }

    public List<Location> getPoints (Player player){
        List<Location> locs = new ArrayList<Location>();
        String playerName = player.getName();
        if (p1.containsKey(playerName)) locs.add(p1.get(playerName));
        if (p2.containsKey(playerName)) locs.add(p2.get(playerName));
        return locs;
    }

    public boolean toggleSelMode (Player player){
        return setSelMode(player,!selMode(player));
    }
    public boolean setSelMode(Player player, boolean sel){
        String name = player.getName();
        if (sel) mode.add(name);
        else if (mode.contains(name)) mode.remove(name);
        return sel;
    }

    public boolean selMode (Player player){
        return mode.contains(player.getName());
    }


    public Set<String> getActivePlayers() {
        return mode;
    }

    public void selectPoints(Player player, Location p1, Location p2) {


    }
}