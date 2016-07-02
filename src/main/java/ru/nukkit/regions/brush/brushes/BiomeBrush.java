package ru.nukkit.regions.brush.brushes;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.level.Location;
import cn.nukkit.level.generator.biome.Biome;
import cn.nukkit.utils.BlockColor;
import ru.nukkit.regions.builder.WeatherMan;
import ru.nukkit.regions.clipboard.Clipboard;
import ru.nukkit.regions.util.Param;

import java.util.List;

/**
 * Created by Igor on 27.06.2016.
 */
public class BiomeBrush extends Brush {


    private Biome biome;
    private BlockColor color;

    public BiomeBrush(){
        this.radius = 5;
        this.biome = null;
        this.color = null;
    }

    public boolean init (String[] args){
        Param param = Param.fromArgs(args,0);
        if (param.matchAnyParam("(?i)radius|r"))
            this.radius = param.getParam("radius",param.getParam("r",1));
        if (param.matchAnyParam("(?i)b|biome|biom")){
            String biomeStr = param.getParam("b",param.getParam("biome",param.getParam("biom", "")));
            biome = Biome.getBiome(biomeStr);
        }
        if (param.matchAnyParam("(?i)color|c|colour")){
            String colorName = param.getParam("color",param.getParam("c"));
            color = WeatherMan.getColorByName(colorName);
        }
        return biome!=null||color!=null;
    }

    @Override
    public boolean paint(Player player, Block clickedBlock) {
        Location center = clickedBlock == null ? getTargetAir(player) : clickedBlock.getLocation();
        if (center == null) return false;
        List<Block> blocks = getDisk(center, this.radius);
        Clipboard undo = Clipboard.createUndoClipBiome(player.getName());
        if (undo!=null){
            blocks.forEach(block -> undo.add(block));
            getUndoManager().add(undo);
        }
        return WeatherMan.setBiome(blocks, biome, color);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder("BIOME (");
        sb.append("radius: ").append(radius);
        if (biome!=null) sb.append(", biome: ").append(biome.getName());
        if (color!=null) sb.append(", color: ").append(color.toString());
        sb.append(")");
        return sb.toString();
    }
}
