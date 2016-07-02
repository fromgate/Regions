package ru.nukkit.regions;

import ru.nukkit.regions.builder.Builder;
import ru.nukkit.regions.builder.ClipboardManager;
import ru.nukkit.regions.builder.UndoManager;
import ru.nukkit.regions.manager.RegionManager;
import ru.nukkit.regions.selector.Selector;

public class Regions {

    static void init() {
        regionManager = new RegionManager();
        selector = new Selector();
        builder = RegionsPlugin.getCfg().getConfiguredBuilder();
        undoManagerManager = new UndoManager();
        clipboardManager = new ClipboardManager();
    }

    private static Builder builder;
    private static UndoManager undoManagerManager;
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

    public static UndoManager getUndoManager() {
        return undoManagerManager;
    }

    public static ClipboardManager getClipboard() {
        return clipboardManager;
    }

}
