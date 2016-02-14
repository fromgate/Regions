package ru.nukkit.regions.selector;


import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.level.particle.Particle;
import cn.nukkit.level.particle.RedstoneParticle;
import ru.nukkit.regions.Regions;
import ru.nukkit.regions.RegionsPlugin;
import ru.nukkit.regions.areas.Area;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ShowParticle {

    private static Set<String> selShow;

    public static void toggleSelShow (Player player){
        if (selShow.contains(player.getName())) selShow.remove(player.getName());
        else selShow.add(player.getName());
    }

    public static boolean isSelShow(Player player){
        return selShow.contains(player.getName());
    }

    public static void init(){
        selShow = new HashSet<String>();
        if (RegionsPlugin.getCfg().selectionShow)
        Server.getInstance().getScheduler().scheduleRepeatingTask(new Runnable() {
            public void run() {
                Set<String> players = Regions.getSelector().getActivePlayers();
                for (String name : players){
                    Player player = Server.getInstance().getPlayer(name);
                    if (!selShow.contains(name)) continue;
                    showSelection (player);
                }
            }
        }, RegionsPlugin.getCfg().selectionTick);
    }

    private static void showSelection (Player player){
        if (player==null) return;
        if (!player.isOnline()) return;
        List<Location> sel = Regions.getSelector().getPoints(player);
        if (sel==null||sel.isEmpty()) return;
        List<Location> cubeLoc = new ArrayList<Location>();
        if (sel.size()==2) {
            Area area = new Area(sel.get(0), sel.get(2));
            cubeLoc = getCubePoints (area.getMin(),area.getMax(),RegionsPlugin.getCfg().selectionDrawWall,RegionsPlugin.getCfg().selectionSolidWall);
            // TODO add show of intersected regions
        } else cubeLoc.add(sel.get(0));

        for (Location l : cubeLoc){
            Particle p = new RedstoneParticle(centerLoc(l));
            player.getLevel().addParticle(p,player);
        }
    }

    private static Location centerLoc (Location loc){
        return new Location(loc.getFloorX()+0.5,loc.getFloorY()+0.5,loc.getFloorZ()+0.5,0,0,loc.getLevel());
    }

    private static List<Location> getCubePoints(Location loc1, Location loc2, boolean drawWall, boolean solid){
        List<Location> locs = new ArrayList<Location>();
        Level world= loc1.getLevel();
        int [] xx = {loc1.getFloorX(),loc2.getFloorX()};
        int [] zz = {loc1.getFloorZ(),loc2.getFloorZ()};
        List<Integer> yy= new ArrayList<Integer>();
        if (drawWall&&(loc2.getFloorY()-loc1.getFloorY())>2) {
            boolean skip = false;
            for (int y = loc2.getFloorY(); y>=loc1.getFloorY();y--){
                if (!skip) yy.add(y);
                if (!solid) skip=!skip||y==loc1.getFloorY()||y==loc2.getFloorY();

            }
        } else {
            yy.add(loc1.getFloorY());
            yy.add(loc2.getFloorY());
        }

        for (int x = xx[0]; x<xx[1]; x++)
                    for (int y = 0; y<yy.size(); y++) {
                        locs.add(new Location(x+0.5, yy.get(y)+0.5, zz[0]+0.5,0,0,world));
                        locs.add(new Location(x+0.5, yy.get(y)+0.5, zz[1]+0.5,0,0,world));
                    }
        for (int z = zz[0]; z<zz[1]; z++)
            for (int y = 0; y<yy.size(); y++) {
                locs.add(new Location(xx[0]+0.5, yy.get(y)+0.5, z+0.5,0,0,world));
                locs.add(new Location(xx[1]+0.5, yy.get(y)+0.5, z+0.5,0,0,world));
            }
        return locs;
    }
}
