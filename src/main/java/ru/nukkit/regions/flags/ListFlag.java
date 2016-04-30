package ru.nukkit.regions.flags;

import ru.nukkit.regions.util.Relation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class ListFlag extends Flag {
    Set<String> names;

    public ListFlag(FlagType flagType, Relation relation) {
        super(flagType, relation);
        this.names = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
    }

    @Override
    public boolean parseParam(String parameter) {
        if (parameter == null || parameter.isEmpty()) return false;
        String[] ln = parameter.split(",\\s*");
        for (String s : ln) names.add(s);
        return true;
    }

    @Override
    public String getParam() {
        StringBuilder sb = new StringBuilder();
        for (String s : names) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(s);
        }
        return sb.toString();
    }

    @Override
    public Set<String> getValue() {
        return names;
    }

    public boolean isOwner(String playerName) {
        return this.names.contains(playerName);
    }

    public void add(String playerName) {
        if (this.names == null) this.names = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
        this.names.add(playerName);
    }

    public void remove(String playerName) {
        if (this.names.contains(playerName)) this.names.remove(playerName);
    }

    public void clear(String playerName) {
        this.names.clear();
    }


    public List<String> getList() {
        return new ArrayList<String>(this.names);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getName());
        sb.append(" relate: ").append(relation.name());
        sb.append("value: [");

        if (this.names.isEmpty()) sb.append(" - ]");
        else {
            int count = sb.length();
            for (String s : this.names) {
                if (sb.length() > count) sb.append(", ");
                sb.append(s);
            }
            sb.append("]");
        }
        return sb.toString();
    }

}
