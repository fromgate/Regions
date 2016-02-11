package ru.nukkit.regions.selector;


import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.level.particle.Particle;
import cn.nukkit.level.particle.SmokeParticle;
import ru.nukkit.regions.Regions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/*
Пока оставляем - похоже не работает
 */
public class ShowParticle {


    public static void init(){
        Server.getInstance().getScheduler().scheduleRepeatingTask(new Runnable() {
            public void run() {
                Set<String> players = Regions.getSelector().getActivePlayers();
                if (players.isEmpty()) return;
                for (String name : players){
                    Player player = Server.getInstance().getPlayer(name);
                    showSelection (player);
                }

            }
        },20);
    }

    private static void showSelection (Player player){

        if (player==null) return;
        if (!player.isOnline()) return;
        List<Location> sel = Regions.getSelector().getPoints(player);
        if (sel==null||sel.isEmpty()) return;

        List<Location> cubeLoc = new ArrayList<Location>();
        if (sel.size()>1) {
            Location min = new Location(Math.min(sel.get(0).getFloorX(),sel.get(1).getFloorX()),
                    Math.min(sel.get(0).getFloorY(),sel.get(1).getFloorY()),
                    Math.min(sel.get(0).getFloorZ(),sel.get(1).getFloorZ()),0,0,player.getLevel());
            Location max = new Location(Math.max(sel.get(0).getFloorX(),sel.get(1).getFloorX()),
                    Math.max(sel.get(0).getFloorY(),sel.get(1).getFloorY()),
                    Math.max(sel.get(0).getFloorZ(),sel.get(1).getFloorZ()),0,0,player.getLevel());
            cubeLoc = getCubePoints (min,max);
        }
        if (cubeLoc.isEmpty()) cubeLoc.add(sel.get(0));
        for (Location l : cubeLoc){
            //Particle p = new RedstoneParticle(centerLoc(l),10);
            Particle p = new SmokeParticle (centerLoc(l));
            player.getLevel().addParticle(p);
        }
    }

    private static Location centerLoc (Location loc){
        return new Location(loc.getFloorX()+0.5,loc.getFloorY()+0.5,loc.getFloorZ()+0.5,0,0,loc.getLevel());
    }


    private static List<Location> getCubePoints(Location loc1, Location loc2){
        List<Location> locs = new ArrayList<Location>();
        Level world= loc1.getLevel();
        int [] xx = {loc1.getFloorX(),loc2.getFloorX()};
        int [] zz = {loc1.getFloorZ(),loc2.getFloorZ()};


        //int [] yy = {loc1.getBlockY(),loc2.getBlockY()};
        List<Integer> yy= new ArrayList<Integer>();
        /*
        if (this.drawwall&&(loc2.getFloorY()-loc1.getFloorY())>2) {
            boolean skip = false;
            for (int y = loc2.getFloorY(); y>=loc1.getFloorY();y--){
                if (!skip) yy.add(y);
                if (this.notsolid) skip=!skip;
            }
        } else { */
            yy.add(loc1.getFloorY());
            yy.add(loc2.getFloorY());
        //}
        for (int x = 0; x<2; x++)
            for (int y = 0; y<yy.size(); y++)
                for (int z = 0; z<2; z++)
                    locs.add(new Location (xx[x],yy.get(y),zz[z],0,0,world));
        return locs;
    }

}
