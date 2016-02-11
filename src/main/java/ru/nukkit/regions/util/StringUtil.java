package ru.nukkit.regions.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StringUtil {
    public static List<String> stringToList (String str){
        List<String> list = new ArrayList<String>();
        String[] ln = str.split(",\\s*");
        for (String s : ln)
            list.add(s);
        return list;
    }

    public static String listToString(Collection<String> list){
        StringBuilder sb = new StringBuilder();
        for (String s : list){
            if (sb.length()>0) sb.append(", ");
            sb.append(s);
        }
        return sb.toString();
    }

    public static String join(String[] args, int i) {
        if (i>=args.length) return "";
        StringBuilder sb = new StringBuilder();
        for (int j = i; j<args.length; j++) {
            if (sb.length()>0) sb.append(" ");
            sb.append(args[j]);
        }
        return sb.toString();
    }

    public static String join(String... str) {
        StringBuilder sb = new StringBuilder();
        for (String s : str) {
            if (sb.length()>0) sb.append(" ");
            sb.append(s);
        }
        return sb.toString();
    }



}
