package ru.nukkit.regions.clipboard;

import cn.nukkit.block.Block;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import cn.nukkit.level.generator.biome.Biome;
import cn.nukkit.utils.BlockColor;

import java.util.LinkedHashMap;
import java.util.List;

public class ClipBiome extends Clipboard {

    private LinkedHashMap<Position, BiomeColor> biomes;

    public ClipBiome() {
        this.biomes = new LinkedHashMap<>();
    }

    @Override
    public void add(Block block) {
        biomes.put(block, new BiomeColor(block));
    }

    @Override
    public void add(Block... blocks) {
        for (Block block : blocks)
            add(block);
    }

    @Override
    public void paste() {
        biomes.entrySet().forEach(e -> {
            e.getValue().set(e.getKey());
        });
    }

    @Override
    public void paste(boolean useUndo) {
        paste();
    }

    @Override
    public void paste(Location loc, boolean asPlayer) {
        throw new IllegalStateException("ClipBiome does not support relative paste");
    }

    @Override
    public void remove(List<Block> blocks) {
        throw new IllegalStateException("ClipBiome does not support remove blocks");
    }

    @Override
    public void remove(Clipboard removeClip) {
        throw new IllegalStateException("ClipBiome does not support remove blocks");
    }

    @Override
    public int getVolume() {
        return biomes.size();
    }

    @Override
    public void paste(Location location, boolean b, boolean useUndo) {
        paste(location, b);
    }

    @Override
    public boolean isEmpty() {
        return biomes.isEmpty();
    }

    class BiomeColor {
        private Biome biome;
        private BlockColor color;

        public BiomeColor(Block block) {
            this.biome = Biome.getBiome(block.getLevel().getBiomeId(block.getFloorX(), block.getFloorZ()));
            int[] rgb = block.getLevel().getBiomeColor(block.getFloorX(), block.getFloorZ());
            this.color = new BlockColor(rgb[0], rgb[1], rgb[2]);
        }

        public Biome getBiome() {
            return this.biome;
        }

        public BlockColor getColor() {
            return this.color;
        }

        public void set(Position loc) {
            if (biome != null)
                loc.getLevel().setBiomeId(loc.getFloorX(), loc.getFloorZ(), biome.getId());
            if (color != null)
                loc.getLevel().setBiomeColor(loc.getFloorX(), loc.getFloorZ(),
                        color.getRed(), color.getGreen(), color.getBlue());
        }


    }
}
