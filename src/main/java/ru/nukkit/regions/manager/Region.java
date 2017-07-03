package ru.nukkit.regions.manager;

import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import ru.nukkit.regions.areas.Area;
import ru.nukkit.regions.flags.Flag;
import ru.nukkit.regions.flags.FlagType;
import ru.nukkit.regions.util.Relation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Region {


    private Area area;
    List<Flag> flags;
    Set<String> owners;
    Set<String> members;

    public Region(Area area) {
        this.flags = new ArrayList<Flag>();
        this.area = area;
        this.owners = new HashSet<String>();
        this.members = new HashSet<String>();
    }

    public Region(Location loc1, Location loc2) {
        this(new Area(loc1, loc2));
    }

    public boolean isInRegion(Location loc) {
        return area.isInside(loc);
    }

    public Location getMin() {
        return area.getMin();
    }

    public Location getMax() {
        return area.getMax();
    }


    public List<Location> getLocations() {
        List<Location> locs = new ArrayList<Location>();
        locs.add(area.getLoc1());
        locs.add(area.getLoc2());
        return locs;
    }

    public void redefine(Area area) {
        this.area = area;
    }

    public void redefine(Location loc1, Location loc2) {
        redefine(new Area(loc1, loc2));
    }


    public Level getLevel() {
        return area.getLevel();
    }

    public List<Flag> getFlags() {
        return this.flags;
    }

    public void addFlag(Flag flag) {
        removeFlag(flag.getType());
        flags.add(flag);
    }

    public void removeFlag(FlagType flagType) {
        Iterator<Flag> it = this.flags.iterator();
        while (it.hasNext()) {
            if (it.next().getType() == flagType) it.remove();
        }
    }

    public Flag getFlag(FlagType flagType) {
        for (Flag f : flags)
            if (f.getType() == flagType) return f;
        return flagType.getDefaultFlag();
    }

    public boolean hasFlag(FlagType flagType) {
        for (Flag f : flags)
            if (f.getType() == flagType) return true;
        return false;
    }


    public void setMember(String player) {
        this.members = new HashSet<String>();
        addMember(player);
    }

    public void setOwner(String players) {
        this.owners = new HashSet<String>();
        addOwner(players);
    }

    public void addMember(String players) {
        if (players == null || players.isEmpty()) return;
        String[] ln = players.split(",\\s*|\\s+");
        for (String s : ln)
            if (!owners.contains(s)) this.members.add(s);
    }

    public void addOwner(String player) {
        if (player == null || player.isEmpty()) return;
        String[] ln = player.split(",\\s*|\\s+");
        for (String s : ln) {
            if (this.members.contains(s)) this.members.remove(s);
            owners.add(s);
        }
    }

    public Set<String> getMembers() {
        return this.members;
    }

    public Set<String> getOwners() {
        return this.owners;
    }

    public boolean isMember(String player) {
        return this.owners.contains(player) || this.members.contains(player);
    }

    public boolean isOwner(String player) {
        return this.owners.contains(player);
    }

    public String getDimension() {
        return area.toString();
    }

    public Relation getRelation(String player) {
        if (this.owners.contains(player)) return Relation.OWNER;
        if (this.members.contains(player)) return Relation.MEMBER;
        return Relation.NOT_MEMBER;
    }


    public void updateArea(Location loc1, Location loc2) {
        this.area = new Area(loc1, loc2);
    }

    public boolean clear(FlagType flagType) {
        Iterator<Flag> it = flags.iterator();
        while (it.hasNext()) {
            if (it.next().getType() == flagType) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    public boolean removeMember(String member) {
        if (!this.members.contains(member)) return false;
        members.remove(member);
        return true;
    }

    public boolean removeOwner(String owner) {
        if (!this.owners.contains(owner)) return false;
        owners.remove(owner);
        return true;
    }

    public boolean hasOwners() {
        return !owners.isEmpty();
    }

    public boolean hasMembers() {
        return !members.isEmpty();
    }

    public boolean intersect(Region region) {
        return this.area.intersect(region.getArea());
    }

    public boolean intersect(Area area) {
        return this.area.intersect(area);
    }

    public Area getArea() {
        return area;
    }
}
