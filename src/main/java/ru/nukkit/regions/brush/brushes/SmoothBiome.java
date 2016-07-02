package ru.nukkit.regions.brush.brushes;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.level.Location;
import cn.nukkit.utils.BlockColor;
import ru.nukkit.regions.builder.WeatherMan;
import ru.nukkit.regions.clipboard.Clipboard;
import ru.nukkit.regions.util.Param;

import java.util.*;

import static sun.audio.AudioPlayer.player;

/**
 * Created by Igor on 27.06.2016.
 */
public class SmoothBiome extends Brush {

    int size;

    public boolean init(String[] args){
        Param param = Param.fromArgs(args,0);
        this.radius = param.getParam("radius",param.getParam("r",1));
        this.size = param.getParam("size",param.getParam("s",this.radius));
        return true;
    }


    @Override
    public boolean paint(Player player, Block clickedBlock) {
        Location center = clickedBlock == null ? getTarget(player) : clickedBlock.getLocation();
        if (center == null) return false;
        List<Block> blocks = getDisk(center, this.radius);
        return smooth (blocks);
    }


    public boolean smooth (List<Block> blocks){
        Map<Block, BlockColor> futureBlocks = new HashMap<>();
        blocks.forEach(block -> {
            List<BlockColor> matrix = new ArrayList<BlockColor>();
            for (int x = block.getFloorX()-size; x<=block.getFloorX()+size; x++)
                for (int z = block.getFloorZ()-size; z<=block.getFloorZ()+size; z++){
                    int[] rgb = block.getLevel().getBiomeColor(x,z);
                    matrix.add(new BlockColor(rgb[0],rgb[1],rgb[2]));
                }
            int r = 0;
            int g = 0;
            int b = 0;
            for (BlockColor c : matrix){
                r += c.getRed();
                g += c.getGreen();
                b += c.getBlue();
            }
            r = r/matrix.size();
            g = g/matrix.size();
            b = b/matrix.size();
            futureBlocks.put(block, new BlockColor(r,g,b));
        });

        Clipboard undo = Clipboard.createUndoClipBiome(player.getName());
        if (undo!=null){
            blocks.forEach(block -> undo.add(block));
            getUndoManager().add(undo);
        }
        WeatherMan.setBiomeColors(futureBlocks);
        return true;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder("SMOOTH (");
        sb.append("radius: ").append(radius);
        sb.append("size: ").append(size);
        sb.append(")");
        return sb.toString();
    }

}
