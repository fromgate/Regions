package ru.nukkit.regions.brush;

import ru.nukkit.regions.brush.brushes.*;

/**
 * Created by Igor on 27.06.2016.
 */
public enum BrushType {
    BUILD(BuildBrush.class, "build"),
    BREAK(BreakBrush.class, "destroy"),
    BIOME(BiomeBrush.class, "biom"),
    SMOOTH(SmoothBiome.class, "sbiome"),
    FILL(FillBrush.class, "floodfill");

    private Class<? extends Brush> brushClass;
    private String alias;

    BrushType(Class<? extends Brush> clazz, String alias) {
        this.brushClass = clazz;
        this.alias = alias;
    }

    public Brush createBrush(String[] args) {
        try {
            Brush brush = this.brushClass.newInstance(); //c.newInstance();
            if (brush.init(args)) return brush;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BrushType getByName(String name) {
        for (BrushType bt : values()) {
            if (bt.name().equalsIgnoreCase(name) || name.matches("(?i)" + bt.alias)) return bt;
        }
        return null;
    }
}
