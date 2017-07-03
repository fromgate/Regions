package ru.nukkit.regions.saver;

import ru.nukkit.regions.manager.Region;

import java.util.Map;

public interface RegionSaver {
    boolean save(Map<String, Region> regions);

    Map<String, Region> load();
}
