package ru.nukkit.regions.clipboard;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.level.Location;
import ru.nukkit.regions.RegionsPlugin;

import java.util.List;

public abstract class Clipboard {

    String playerName;
    Location playerLocation;
    Location minLocation;


    public Clipboard() {
        this.playerName = "";
        this.playerLocation = null;
        this.minLocation = null;
    }

    public Clipboard(Player player) {
        this.playerName = player.getName();
        this.playerLocation = player.getLocation();
    }

    public abstract void add(Block block);

    public abstract void add(Block... block);

    public abstract void paste();

    public static Clipboard createUndoClipBoard(String playerName) {
        if (!RegionsPlugin.getCfg().builderUseUndo) return null;
        if (playerName == null || playerName.isEmpty()) return null;
        Player player = Server.getInstance().getPlayerExact(playerName);
        if (player == null || !player.isOnline()) return null;
        if (!player.hasPermission("regions.undo")) return null;
        Clipboard clip = new ClipBlock();
        clip.playerName = player.getName();
        return clip;
    }

    public static Clipboard createUndoClipBiome(String playerName) {
        if (!RegionsPlugin.getCfg().builderUseUndo) return null;
        if (playerName == null || playerName.isEmpty()) return null;
        Player player = Server.getInstance().getPlayerExact(playerName);
        if (player == null || !player.isOnline()) return null;
        if (!player.hasPermission("regions.undo")) return null;
        Clipboard clip = new ClipBiome();
        clip.playerName = player.getName();
        return clip;
    }

    public abstract void paste(Location loc, boolean asPlayer);

    public abstract void remove(List<Block> blocks);

    public abstract void remove(Clipboard removeClip);

    public String getPlayerName() {
        return playerName;
    }

    public abstract int getVolume();
}
