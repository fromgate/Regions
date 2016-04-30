package ru.nukkit.regions.builder;

import ru.nukkit.regions.RegionsPlugin;

import java.util.Map;
import java.util.TreeMap;

public class Undo {

    private Map<String, Clipboard> undos;

    public Undo() {
        this.undos = new TreeMap<String, Clipboard>(String.CASE_INSENSITIVE_ORDER);
    }

    public void add(Clipboard clipboard) {
        if (!RegionsPlugin.getCfg().builderUseUndo) return;
        undos.put(clipboard.playerName, clipboard);
    }


    public boolean playerUndoExist(String name) {
        return (undos.containsKey(name));
    }

    public boolean performUndo(String name) {
        if (!undos.containsKey(name)) return false;
        Clipboard undo = undos.get(name);
        undo.paste();
        undos.remove(name);
        return true;
    }
}
