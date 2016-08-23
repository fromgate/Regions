package ru.nukkit.regions.builder;

import ru.nukkit.regions.RegionsPlugin;
import ru.nukkit.regions.clipboard.Clipboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class UndoManager {

    private Map<String, List<Clipboard>> undos;

    public UndoManager() {
        this.undos = new TreeMap<String, List<Clipboard>>(String.CASE_INSENSITIVE_ORDER);
    }

    public void add(Clipboard clipboard) {
        if (clipboard == null) return;
        if (!RegionsPlugin.getCfg().builderUseUndo) return;
        String userName = clipboard.getPlayerName();
        if (!undos.containsKey(userName)) undos.put(userName, new ArrayList<>());
        undos.get(userName).add(clipboard);
        if (undos.size() > RegionsPlugin.getCfg().undoLevels) undos.get(userName).remove(0);
    }

    public boolean playerUndoExist(String name) {
        return undos.containsKey(name) && !undos.get(name).isEmpty();
    }

    public boolean performUndo(String name) {
        if (!playerUndoExist(name)) return false;
        List<Clipboard> clipboards = undos.get(name);
        Clipboard clipboard = clipboards.get(clipboards.size() - 1);
        clipboards.remove(clipboards.size() - 1);
        if (clipboards.isEmpty()) undos.remove(name);
        clipboard.paste();
        return true;
    }


    public void clear(String name) {
        if (undos.containsKey(name)) {
            undos.get(name).clear();
            undos.remove(name);
        }
    }
}
