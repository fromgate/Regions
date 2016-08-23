package ru.nukkit.regions.builder;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.level.Location;

import java.util.Collection;

/**
 * Created by Igor on 20.04.2016.
 */
public interface Builder {


    void setBlock(Player player, Block block);

    void setBlock(String playerName, Block block);

    void replaceBlock(Player player, Block b1, Block b2, boolean compareData, Location loc1, Location loc2);

    void replaceBlock(String playerName, Block b1, Block b2, boolean compareData, Location loc1, Location loc2);

    void setBlock(Player player, Block block, Location loc1, Location loc2);

    void setBlock(String playerName, Block block, Location loc1, Location loc2);

    void setBlock(Player player, Collection<Block> blocks);

    void setBlock(String playerName, Collection<Block> blocks);

    void setBlockBox(Player player, Block block, Location l1, Location l2);

    void setBlockBox(String playerName, Block block, Location l1, Location l2);

    void setBlockWall(Player player, Block block, Location location, Location location1);

    void setBlockWall(String playerName, Block block, Location location, Location location1);

    void setBlock(String playerName, Collection<Block> blocks, boolean useUndo);
}