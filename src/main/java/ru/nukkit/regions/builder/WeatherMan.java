package ru.nukkit.regions.builder;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.level.Level;
import cn.nukkit.level.generator.biome.Biome;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.DyeColor;
import ru.nukkit.regions.Regions;
import ru.nukkit.regions.areas.Area;
import ru.nukkit.regions.clipboard.Clipboard;
import ru.nukkit.regions.util.Message;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Igor on 31.05.2016.
 */
public class WeatherMan {
    public static boolean setBiome(Area area, Biome biome) {
        if (area == null) return false;
        if (biome == null) return false;
        Level level = area.getLevel();
        Set<BuilderChunk> toUpdate = new HashSet<>();
        for (int x = area.getX1(); x <= area.getX2(); x++)
            for (int z = area.getZ1(); z <= area.getZ2(); z++) {
                int color = biome.getColor();
                int[] rgb = new int[]{color >> 16, (color >> 8) & 0xFF, color & 0xFF};
                level.setBiomeId(x, z, biome.getId());
                level.setBiomeColor(x, z, rgb[0], rgb[1], rgb[2]);
                toUpdate.add(new BuilderChunk(level, x >> 4, z >> 4));
            }

        toUpdate.forEach(BuilderChunk::updateChunk);

        return true;
    }

    public static String getBiomeList() {
        Map<Integer, Biome> biomes = null;
        try {
            Field field = Biome.class.getDeclaredField("biomes");
            field.setAccessible(true);
            biomes = (Map<Integer, Biome>) field.get(null);
        } catch (Exception e) {
            if (Message.isDebug()) e.printStackTrace();
        }
        if (biomes == null) return "";
        StringBuilder sb = new StringBuilder();
        biomes.values().forEach(b -> {
            if (sb.length() > 0) sb.append(", ");
            sb.append(b.getName());
        });
        return sb.toString();
    }


    public static BlockColor getColorByName(String name) {
        BlockColor color = null;
        if (name.matches("\\d+,\\d+,\\d+")) {
            String[] rgbS = name.split(",");
            int[] rgb = new int[3];
            for (int i = 0; i < 3; i++) rgb[i] = Integer.parseInt(rgbS[i]);
            color = new BlockColor(rgb[0], rgb[1], rgb[2]);
        } else {
            for (DyeColor dye : DyeColor.values()) {
                if (dye.name().equalsIgnoreCase(name) || dye.getName().equalsIgnoreCase(name)) {
                    color = dye.getColor();
                    break;
                }
            }
        }
        return color;
    }

    public static boolean setBiome(Player player, Collection<Block> blocks, Biome biome, BlockColor biomeColor) {
        if (blocks == null || blocks.isEmpty()) return false;
        if (biome == null && biomeColor == null) return false;
        BlockColor color = biomeColor == null ? new BlockColor(biome.getColor()) : biomeColor;
        Set<BuilderChunk> toUpdate = new HashSet<>();
        Clipboard undo = Clipboard.createUndoClipBiome(player.getName());
        blocks.forEach(block -> {
            Level level = block.getLevel();
            if (undo != null) undo.add(block);
            int x = block.getFloorX();
            int z = block.getFloorZ();
            if (biome != null) level.setBiomeId(x, z, biome.getId());
            level.setBiomeColor(x, z, color.getRed(), color.getGreen(), color.getBlue());
            toUpdate.add(new BuilderChunk(level, x >> 4, z >> 4));
        });
        if (undo != null) Regions.getUndoManager().add(undo);
        toUpdate.forEach(BuilderChunk::updateChunk);
        return true;
    }

    public static boolean setBiome(Player player, Area area, Biome biome, BlockColor biomeColor) {
        if (area == null) return false;
        BlockColor color = biomeColor == null ? new BlockColor(biome.getColor()) : biomeColor;
        Level level = area.getLevel();
        Set<BuilderChunk> toUpdate = new HashSet<>();
        Clipboard undo = Clipboard.createUndoClipBiome(player.getName());
        for (int x = area.getX1(); x <= area.getX2(); x++)
            for (int z = area.getZ1(); z <= area.getZ2(); z++) {
                if (undo != null) undo.add(area.getLevel().getBlock(new Vector3(x, 64, z)));
                if (biome != null) level.setBiomeId(x, z, biome.getId());
                level.setBiomeColor(x, z, color.getRed(), color.getGreen(), color.getBlue());
                toUpdate.add(new BuilderChunk(level, x >> 4, z >> 4));
            }
        Regions.getUndoManager().add(undo);
        toUpdate.forEach(BuilderChunk::updateChunk);
        return true;
    }

    public static boolean setBiomeColors(Player player, Map<Block, BlockColor> blocks) {
        if (blocks == null || blocks.isEmpty()) return false;
        Set<BuilderChunk> toUpdate = new HashSet<>();
        Clipboard undo = Clipboard.createUndoClipBiome(player.getName());
        blocks.forEach((b, color) -> {
            if (undo != null) undo.add(b);
            b.getLevel().setBiomeColor(b.getFloorX(), b.getFloorZ(), color.getRed(), color.getGreen(), color.getBlue());
            toUpdate.add(new BuilderChunk(b.level, b.getFloorX() >> 4, b.getFloorZ() >> 4));
        });
        Regions.getUndoManager().add(undo);
        toUpdate.forEach(BuilderChunk::updateChunk);
        return true;
    }

    public static boolean smooth(Player player, Area area, int radius) {
        if (area == null) return false;
        Map<Block, BlockColor> futureBlocks = new HashMap<>();
        for (int x = area.getX1(); x <= area.getX2(); x++) {
            for (int z = area.getZ1(); z <= area.getZ2(); z++) {
                Block block = area.getLevel().getBlock(new Vector3(x, area.getY1(), z));
                List<BlockColor> matrix = new ArrayList<>();
                for (int cX = block.getFloorX() - radius; cX <= block.getFloorX() + radius; cX++)
                    for (int cZ = block.getFloorZ() - radius; cZ <= block.getFloorZ() + radius; cZ++) {
                        int[] rgb = block.getLevel().getBiomeColor(cX, cZ);
                        matrix.add(new BlockColor(rgb[0], rgb[1], rgb[2]));
                    }
                int r = 0;
                int g = 0;
                int b = 0;
                for (BlockColor c : matrix) {
                    r += c.getRed();
                    g += c.getGreen();
                    b += c.getBlue();
                }
                r = r / matrix.size();
                g = g / matrix.size();
                b = b / matrix.size();
                futureBlocks.put(block, new BlockColor(r, g, b));
            }
        }
        setBiomeColors(player, futureBlocks);
        return true;

    }

    public static boolean smooth(Player player, List<Block> blocks, int radius) {
        Map<Block, BlockColor> futureBlocks = new HashMap<>();
        blocks.forEach(block -> {
            List<BlockColor> matrix = new ArrayList<>();
            for (int x = block.getFloorX() - radius; x <= block.getFloorX() + radius; x++)
                for (int z = block.getFloorZ() - radius; z <= block.getFloorZ() + radius; z++) {
                    int[] rgb = block.getLevel().getBiomeColor(x, z);
                    matrix.add(new BlockColor(rgb[0], rgb[1], rgb[2]));
                }
            int r = 0;
            int g = 0;
            int b = 0;
            for (BlockColor c : matrix) {
                r += c.getRed();
                g += c.getGreen();
                b += c.getBlue();
            }
            r = r / matrix.size();
            g = g / matrix.size();
            b = b / matrix.size();
            futureBlocks.put(block, new BlockColor(r, g, b));
        });
        setBiomeColors(player, futureBlocks);
        return true;
    }

}
