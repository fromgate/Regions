package ru.nukkit.regions;

import ru.nukkit.regions.builder.Builder;
import ru.nukkit.regions.manager.RegionManager;
import ru.nukkit.regions.selector.Selector;

public class Regions {

    static void init(){
        regionManager = new RegionManager();
        selector = new Selector();
        builder = new Builder();
    }

    private static Builder builder;
    private static RegionManager regionManager;
    private static Selector selector;

    public static RegionManager getManager() {
        return regionManager;
    }
    public static Selector getSelector(){
        return selector;
    }
    public static Builder getBuilder(){
        return builder;
    }

}
