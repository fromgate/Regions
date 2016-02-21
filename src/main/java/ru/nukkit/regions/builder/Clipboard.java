package ru.nukkit.regions.builder;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.level.Location;
import ru.nukkit.regions.Regions;
import ru.nukkit.regions.RegionsPlugin;

import java.util.LinkedList;
import java.util.List;

public class Clipboard {

    String playerName;
    Location playerLocation;
    Location minLocation;
    List<Block> blocks;

    public Clipboard (){
        this.playerName = "";
        this.playerLocation = null;
        this.minLocation =null;
        this.blocks = new LinkedList<Block>();
    }
    public Clipboard (Player player){
        this.playerName = player.getName();
        this.playerLocation = player.getLocation();
    }

    public Clipboard (Player player, List<Block> blocks,Location minLocation){
        this.playerName = player.getName();
        this.playerLocation = player.getLocation();
        this.minLocation = minLocation;
        this.blocks = new LinkedList<Block>();
        if (this.minLocation==null){
            blocks.addAll(blocks);
        } else {
            for (Block b : blocks)
                this.blocks.add(Block.get(b.getId(),b.getDamage(),b.getLocation().subtract(this.minLocation)));
        }
    }

    public void add(Block block){
        this.blocks.add(minLocation ==null? block : Block.get(block.getId(),block.getDamage(),block.getLocation().subtract(minLocation)));
    }

    public void paste(){
        Regions.getBuilder().setBlock(playerName,blocks);
    }

    public static Clipboard createUndoClipBoard (String playerName){
        if (!RegionsPlugin.getCfg().builderUseUndo) return null;
        if (playerName==null||playerName.isEmpty()) return null;
        Player player = Server.getInstance().getPlayerExact(playerName);
        if (player==null||!player.isOnline()) return null;
        if (!player.hasPermission("regions.undo")) return null;
        Clipboard clip = new Clipboard();
        clip.playerName = player.getName();
        return clip;
    }

    public void paste(Location loc, boolean asPlayer){
        Location start = asPlayer ? loc.add(this.minLocation.subtract(this.playerLocation)) : loc;
        List<Block> blocks = new LinkedList<Block>();
        for (Block b : this.blocks){
            blocks.add(Block.get(b.getId(),b.getDamage(), start.add(b)));
        }
        Regions.getBuilder().setBlock(playerName,blocks);
    }
}
