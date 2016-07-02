package ru.nukkit.regions.builder;

import cn.nukkit.block.Block;
import cn.nukkit.level.Level;
import cn.nukkit.level.generator.biome.Biome;
import cn.nukkit.utils.BlockColor;
import ru.nukkit.regions.areas.Area;
import ru.nukkit.regions.util.Message;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Igor on 31.05.2016.
 */
public class WeatherMan {
    private static final String[] colors = new String[]{"White", "Orange", "Magenta", "Light Blue", "Yellow", "Lime", "Pink", "Gray", "Light Gray", "Cyan", "Purple", "Blue", "Brown", "Green", "Red", "Black"};

    public static boolean setBiome(Area area, Biome biome) {
        if (area == null) return false;
        if (biome == null) return false;
        Level level = area.getLevel();
        Set<BuilderChunk> toUpdate = new HashSet<>();
        for (int x = area.getX1(); x<= area.getX2(); x++)
            for (int z = area.getZ1(); z<= area.getZ2(); z++){
                int color = biome.getColor();
                int[] rgb = new int[]{color >> 16, (color >> 8) & 0xFF, color & 0xFF};
                level.setBiomeId(x,z,biome.getId());
                level.setBiomeColor(x, z, rgb[0],rgb[1],rgb[2]);
                toUpdate.add(new BuilderChunk(level, x >> 4, z >> 4));
            }

        toUpdate.forEach(BuilderChunk::updateChunk);

        return true;
    }

    public static String getBiomeList(){
        Map<Integer, Biome> biomes = null;
        try {
            Field field = Biome.class.getDeclaredField("biomes");
            field.setAccessible(true);
            biomes = (Map<Integer, Biome>) field.get(null);
        } catch (Exception e) {
            if (Message.isDebug()) e.printStackTrace();
        }
        if (biomes== null) return "";
        StringBuilder sb = new StringBuilder();
        biomes.values().forEach(b->{
            if (sb.length()>0) sb.append(", ");
            sb.append(b.getName());
        });
        return sb.toString();
    }



    public static BlockColor getColorByName (String name){
        BlockColor color = null;
        if (name.matches("\\d+,\\d+,\\d+")){
            String[] rgbS = name.split(",");
            int[]rgb = new int[3];
            for (int i = 0; i<3; i++) rgb[i] = Integer.parseInt(rgbS[i]);
            color = new BlockColor(rgb[0],rgb[1],rgb[2]);
        } else {
            int colorNum = -1;
            for (int i = 0; i < colors.length; i++)
                if (colors[i].equalsIgnoreCase(name.replace("_", " "))) {
                    colorNum = i;
                    break;
                }
            if (colorNum >= 0) color = BlockColor.getDyeColor(colorNum);
        }
        return color;
    }

    public static boolean setBiome(Collection<Block> blocks, Biome biome, BlockColor biomeColor) {
        if (blocks==null||blocks.isEmpty()) return false;
        if (biome == null&&biomeColor == null) return false;
        BlockColor color = biomeColor == null ? new BlockColor(biome.getColor()) : biomeColor;
        Set<BuilderChunk> toUpdate = new HashSet<>();
        blocks.forEach(block -> {
            Level level = block.getLevel();
            int x = block.getFloorX();
            int z = block.getFloorZ();
            if (biome !=null) level.setBiomeId(x,z,biome.getId());
            level.setBiomeColor(x, z, color.getRed(),color.getGreen(),color.getBlue());
            toUpdate.add(new BuilderChunk(level, x >> 4, z >> 4));
        });
        toUpdate.forEach(BuilderChunk::updateChunk);
        return true;
    }

    public static boolean setBiome(Area area, Biome biome, BlockColor biomeColor) {
        if (area == null) return false;
        BlockColor color = biomeColor == null ? new BlockColor(biome.getColor()) : biomeColor;
        Level level = area.getLevel();
        Set<BuilderChunk> toUpdate = new HashSet<>();
        for (int x = area.getX1(); x<= area.getX2(); x++)
            for (int z = area.getZ1(); z<= area.getZ2(); z++){
                if (biome !=null) level.setBiomeId(x,z,biome.getId());
                level.setBiomeColor(x, z, color.getRed(),color.getGreen(),color.getBlue());
                toUpdate.add(new BuilderChunk(level, x >> 4, z >> 4));
            }
        toUpdate.forEach(BuilderChunk::updateChunk);
        return true;
    }

    public static boolean setBiomeColors(Map<Block,BlockColor> blocks) {
        if (blocks==null||blocks.isEmpty()) return false;
        Set<BuilderChunk> toUpdate = new HashSet<>();
        blocks.entrySet().forEach(e->{
            Block b = e.getKey();
            BlockColor color = e.getValue();
            b.getLevel().setBiomeColor(b.getFloorX(),b.getFloorZ(),color.getRed(),color.getGreen(),color.getBlue());
            toUpdate.add(new BuilderChunk(b.level, b.getFloorX() >> 4, b.getFloorZ() >> 4));
        });
        toUpdate.forEach(BuilderChunk::updateChunk);
        return true;
    }

}
