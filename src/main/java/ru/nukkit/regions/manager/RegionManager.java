package ru.nukkit.regions.manager;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import ru.nukkit.regions.Regions;
import ru.nukkit.regions.RegionsPlugin;
import ru.nukkit.regions.areas.Area;
import ru.nukkit.regions.flags.BoolFlag;
import ru.nukkit.regions.flags.Flag;
import ru.nukkit.regions.flags.FlagType;
import ru.nukkit.regions.saver.RegionSaver;
import ru.nukkit.regions.saver.YamlSaver;
import ru.nukkit.regions.util.Message;
import ru.nukkit.regions.util.Relation;

import java.util.*;
import java.util.regex.Pattern;

public class RegionManager {

    private RegionSaver saver = new YamlSaver();

    private final Pattern VALID_ID_PATTERN = Pattern.compile("^[A-Za-z0-9_,\'\\-\\+/]{1,}$");

    private Map<String, Region> regions = new TreeMap<String, Region>(String.CASE_INSENSITIVE_ORDER);

    public RegionManager() {
        load();
    }

    public void load() {
        regions = saver.load();
    }

    public void save() {
        saver.save(regions);
    }

    public boolean regionIdUsed(String id) {
        return regions.containsKey(id);
    }

    public boolean reDefineRegion(String id, List<Location> locs) {
        if (locs.size() != 2) return false;
        return reDefineRegion(id, locs.get(0), locs.get(1));
    }

    public boolean reDefineRegion(String id, Location loc1, Location loc2) {
        if (loc1 == null || loc2 == null) return false;
        Region region = this.getRegion(id);
        if (region == null) return false;
        region.updateArea(loc1, loc2);
        save();
        return true;
    }

    public boolean defineRegion(String id, String owner, List<Location> locs) {
        if (locs.size() != 2) return false;
        return defineRegion(id, owner, locs.get(0), locs.get(1));
    }

    public boolean claimRegion(String id, Player player, List<Location> locs) {
        if (locs.size() != 2) return false;
        return defineRegion(id, player, locs.get(0), locs.get(1));
    }

    public boolean defineRegion(String id, Player player, Location loc1, Location loc2) {
        if (loc1 == null || loc2 == null) return false;
        if (id == null || id.isEmpty()) return false;
        if (player == null || !player.isOnline()) return false;
        if (!VALID_ID_PATTERN.matcher(id).matches()) return false;
        Region region = new Region(loc1, loc2);
        region.addOwner(player.getName());
        regions.put(id, region);
        save();
        return true;
    }

    public boolean defineRegion(String id, String owner, Location loc1, Location loc2) {
        if (loc1 == null || loc2 == null) return false;
        if (id == null || id.isEmpty()) return false;
        if (!VALID_ID_PATTERN.matcher(id).matches()) return false;
        Region region = new Region(loc1, loc2);
        if (owner != null && !owner.isEmpty()) region.setOwner(owner);
        regions.put(id, region);
        save();
        return true;
    }

    public Region getRegion(String id) {
        return (regions.containsKey(id)) ? regions.get(id) : null;
    }

    public boolean removeRegion(String id) {
        if (!regions.containsKey(id)) return false;
        regions.remove(id);
        save();
        return true;
    }

    public Map<String, Region> getRegions(Block block) {
        return getRegions(block.getLocation());
    }

    public Map<String, Region> getRegions(Location loc) {
        Map<String, Region> locReg = new TreeMap<String, Region>(String.CASE_INSENSITIVE_ORDER);
        for (String id : regions.keySet()) {
            Region r = regions.get(id);
            if (r.isInRegion(loc)) locReg.put(id, r);
        }
        return locReg;
    }

    public Map<String, Region> getRegions() {
        return regions;
    }

    public Map<String, Region> getRegions(Level level) {
        Map<String, Region> locReg = new TreeMap<String, Region>(String.CASE_INSENSITIVE_ORDER);
        for (String id : regions.keySet()) {
            Region r = regions.get(id);
            if (r.getLevel().equals(level)) locReg.put(id, r);
        }
        return locReg;
    }


    public List<Flag> getDefaultFlags() {
        return new ArrayList<Flag>();
    }

    public boolean addOwner(String id, String players) {
        Region region = this.getRegion(id);
        if (region == null) return false;
        region.addOwner(players);
        save();
        return true;
    }

    public boolean setOwner(String id, String players) {
        Region region = this.getRegion(id);
        if (region == null) return false;
        region.setOwner(players);
        save();
        return true;
    }

    public boolean addFlag(String id, Flag flag) {
        Region region = this.getRegion(id);
        if (region == null) return false;
        region.addFlag(flag);
        save();
        return true;
    }

    public boolean setMember(String id, String players) {
        Region region = this.getRegion(id);
        if (region == null) return false;
        region.setMember(players);
        save();
        return true;

    }

    public boolean addMember(String id, String players) {
        Region region = this.getRegion(id);
        if (region == null) return false;
        region.addMember(players);
        save();
        return true;
    }

    public boolean removeMember(String id, String player) {
        Region region = this.getRegion(id);
        if (region == null) return false;
        if (!region.removeMember(player)) return false;
        save();
        return true;
    }

    public boolean removeOwner(String id, String player) {
        Region region = this.getRegion(id);
        if (region == null) return false;
        if (!region.removeOwner(player)) return false;
        save();
        return true;
    }

    public boolean isOwner(Player player, String id) {
        return isOwner(player, id, true);
    }

    public boolean isOwner(Player player, String id, boolean usePerm) {
        Region region = this.getRegion(id);
        if (region == null) return false;
        if (usePerm && player.hasPermission("regions.unlimited")) return true;
        return region.isOwner(player.getName());
    }

    public boolean isMember(Player player, String id) {
        return isMember(player, id, true);
    }

    public boolean isMember(Player player, String id, boolean usePerm) {
        Region region = this.getRegion(id);
        if (region == null) return false;
        if (usePerm && player.hasPermission("regions.unlimited")) return true;
        return region.isMember(player.getName());
    }

    public int countClaimRegions(String playerName) {
        int count = 0;
        for (Region region : this.getRegions().values())
            if (region.isOwner(playerName)) count++;
        return count;
    }

    public boolean canClaimMore(String playerName) {
        int count = countClaimRegions(playerName);
        return count >= RegionsPlugin.getPlugin().getCfg().maxRegionPerPlayer;
    }

    public boolean canClaimVolume(Area area) {
        return RegionsPlugin.getPlugin().getCfg().maxClaimVolume < area.getVolume();
    }

    public Map<String, Region> getIntersections(String id) {
        Map<String, Region> intersections = new TreeMap<String, Region>(String.CASE_INSENSITIVE_ORDER);
        Region r = this.getRegion(id);
        if (r == null) return intersections;
        for (String rid : this.regions.keySet()) {
            if (id.equalsIgnoreCase(rid)) continue;
            Region rr = this.regions.get(rid);
            if (r.intersect(r.getArea())) intersections.put(id, rr);
        }
        return intersections;
    }

    public Map<String, Region> getIntersections(Area area) {
        Map<String, Region> intersections = new TreeMap<String, Region>(String.CASE_INSENSITIVE_ORDER);
        for (Map.Entry<String, Region> entry : regions.entrySet()) {
            if (entry.getValue().intersect(area))
                intersections.put(entry.getKey(), entry.getValue());
        }
        return intersections;
    }

    public boolean isRegion(String id) {
        return this.regions.containsKey(id);
    }


    public boolean cancelEvent(Player player, Region region, FlagType flagType) {
        if (player.hasPermission("regions.flag." + flagType.name().toLowerCase()))
            return !Message.FLAG_DEBUG_PERMISSION.debug(flagType.name());
        Flag f = region.getFlag(flagType);
        Relation rel = region.getRelation(player.getName());
        BoolFlag bf = (BoolFlag) f;
        return !bf.isAllowed(rel);
    }

    public boolean cancelEvent(Player player, Location loc, FlagType flagType) {
        Map<String, Region> regions = Regions.getManager().getRegions(loc);
        if (regions == null || regions.isEmpty()) return false;
        for (Map.Entry<String, Region> entry : regions.entrySet()) {
            Region region = entry.getValue();
            if (cancelEvent(player, region, flagType)) return true;
        }
        return false;
    }
}
