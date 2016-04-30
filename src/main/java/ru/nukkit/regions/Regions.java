package ru.nukkit.regions;

import ru.nukkit.regions.builder.Builder;
import ru.nukkit.regions.builder.ClipboardManager;
import ru.nukkit.regions.builder.Undo;
import ru.nukkit.regions.manager.RegionManager;
import ru.nukkit.regions.selector.Selector;

public class Regions {

    static void init() {
        regionManager = new RegionManager();
        selector = new Selector();
        builder = RegionsPlugin.getCfg().getConfiguredBuilder();
        undoManager = new Undo();
        clipboardManager = new ClipboardManager();
    }

    private static Builder builder;
    private static Undo undoManager;
    private static RegionManager regionManager;
    private static Selector selector;
    private static ClipboardManager clipboardManager;

    public static RegionManager getManager() {
        return regionManager;
    }

    public static Selector getSelector() {
        return selector;
    }

    public static Builder getBuilder() {
        return builder;
    }

    public static Undo getUndoManager() {
        return undoManager;
    }

    public static ClipboardManager getClipboard() {
        return clipboardManager;
    }

}
