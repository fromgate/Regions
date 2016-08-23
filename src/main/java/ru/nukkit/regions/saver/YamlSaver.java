package ru.nukkit.regions.saver;

import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import ru.nukkit.regions.RegionsPlugin;
import ru.nukkit.regions.areas.Area;
import ru.nukkit.regions.flags.Flag;
import ru.nukkit.regions.flags.FlagType;
import ru.nukkit.regions.manager.Region;
import ru.nukkit.regions.util.Message;
import ru.nukkit.regions.util.StringUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class YamlSaver implements RegionSaver {
    public boolean save(Map<String, Region> regions) {
        File f = new File(RegionsPlugin.getPlugin().getDataFolder() + File.separator + "regions.yml");
        if (f.exists()) f.delete();
        Config cfg = new Config(f, Config.YAML);
        regions.entrySet().forEach(e -> {
            ConfigSection s = new ConfigSection();
            Region region = e.getValue();
            s.set("area", region.getDimension());
            s.set("owners", StringUtil.listToString(region.getOwners()));
            s.set("members", StringUtil.listToString(region.getMembers()));
            for (Flag flag : region.getFlags()) {
                String flagKey = "flags." + flag.getName() + ".";
                if (flag.getRelation() != null) s.set(flagKey + "relate", flag.getRelation().name());
                s.set(flagKey + "value", flag.getParam());
            }
            cfg.set(e.getKey(), s);
        });
        boolean saveResult = cfg.save();
        Message.debugMessage("Regions saved:", regions.size(), "Save result (ok)", saveResult);
        return saveResult;
    }

    public Map<String, Region> load() {
        Map<String, Region> regions = new HashMap<String, Region>();
        File f = new File(RegionsPlugin.getPlugin().getDataFolder() + File.separator + "regions.yml");
        if (!f.exists()) return regions;
        Config cfg = new Config(f, Config.YAML);

        cfg.getSections(null).entrySet().forEach(e -> {
            ConfigSection s = (ConfigSection) e.getValue();
            Region region = new Region(new Area(s.getString("area")));
            region.setOwner(s.getString("owners"));
            region.setMember(s.getString("members"));
            for (FlagType ft : FlagType.values()) {
                if (!s.exists("flags." + ft.name())) continue;
                if (!s.exists("flags." + ft.name() + ".value")) continue;
                String value = s.getString("flags." + ft.name() + ".value");
                String relStr = s.getString("flags." + ft.name() + ".relate");
                Flag flag = ft.createNewFlag(relStr, value);
                region.addFlag(flag);
            }
            regions.put(e.getKey(), region);
        });
        Message.debugMessage("Loaded", regions.size(), "regions");

        return regions;
    }
}