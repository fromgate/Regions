package ru.nukkit.regions.saver;

import ru.nukkit.regions.manager.Region;

import java.util.Map;

public interface RegionSaver {
    public abstract boolean save(Map<String,Region> regions);
    public abstract Map<String,Region> load();
}
