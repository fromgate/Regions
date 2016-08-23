package ru.nukkit.regions.brush.brushes;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.level.Location;
import ru.nukkit.regions.builder.WeatherMan;
import ru.nukkit.regions.util.Param;

import java.util.List;

/**
 * Created by Igor on 27.06.2016.
 */
public class SmoothBiome extends Brush {

    int size;

    public boolean init(String[] args) {
        Param param = Param.fromArgs(args, 0);
        this.radius = param.getParam("radius", param.getParam("r", 1));
        this.size = param.getParam("size", param.getParam("s", this.radius));
        return true;
    }


    @Override
    public boolean paint(Player player, Block clickedBlock) {
        Location center = clickedBlock == null ? getTarget(player) : clickedBlock.getLocation();
        if (center == null) return false;
        List<Block> blocks = getDisk(center, this.radius);
        return WeatherMan.smooth(player, blocks, size);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("SMOOTH (");
        sb.append("radius: ").append(radius);
        sb.append("size: ").append(size);
        sb.append(")");
        return sb.toString();
    }
}