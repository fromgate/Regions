package ru.nukkit.regions.saver;

import cn.nukkit.utils.Config;
import ru.nukkit.regions.RegionsPlugin;
import ru.nukkit.regions.areas.Area;
import ru.nukkit.regions.flags.Flag;
import ru.nukkit.regions.flags.FlagType;
import ru.nukkit.regions.manager.Region;
import ru.nukkit.regions.util.StringUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


/*
    <НазваниеРегиона>:
        area: <ОписаниеРегиона>
        min: {x: -195.0, y: 63.0, z: 355.0}
        max: {x: -152.0, y: 76.0, z: 370.0}
        members: {}
        flags: {}
        owners:
            players: [rain]
        type: cuboid
        priority: 0

 */
public class YamlSaver implements RegionSaver {

    public boolean save(Map<String, Region> regions) {
        File f = new File(RegionsPlugin.getPlugin().getDataFolder()+File.separator+"regions.yml");
        if (f.exists()) f.delete();
        Config cfg = new Config(f,Config.YAML);
        for (String id : regions.keySet()){
            Region region = regions.get(id);
            cfg.set(id+".area",region.getDimension());
            cfg.set(id+".owners", StringUtil.listToString(region.getOwners()));
            cfg.set(id+".members", StringUtil.listToString(region.getMembers()));
            for (Flag flag : region.getFlags()){
                String flagKey = id+".flags."+flag.getName()+".";
                //break
                //   relate: ALL
                //   value:
                if (flag.getRelation()!=null)
                    cfg.set(flagKey+"relate",flag.getRelation().name());
                cfg.set(flagKey+"value",flag.getParam());
            }
        }
        return cfg.save();
    }

    public Map<String, Region> load() {
        Map<String, Region> regions = new HashMap<String, Region>();
        File f = new File(RegionsPlugin.getPlugin().getDataFolder()+File.separator+"regions.yml");
        if (!f.exists()) return regions;
        Config cfg = new Config(f,Config.YAML);

        //Map<String, List<String>> flags = new HashMap<String, List<String>>();
        for (String key : cfg.getAll().keySet()){
            if (key.contains(".")) continue;
            Region region = new Region(new Area(cfg.getString(key+".area")));
            region.setOwner(cfg.getString(key+".owners"));
            region.setMember(cfg.getString(key+".members"));
            for (FlagType ft : FlagType.values()){
                String value = cfg.getString (key+".flags."+ft.name()+".value");
                if (value == null) continue;
                String relStr = cfg.getString(key+".flags."+ft.name()+".relate");
                Flag flag = ft.createNewFlag(relStr,value);
                region.addFlag(flag);
            }
            regions.put(key,region);

          /*  if (!key.contains(".")) flags.put(key, new ArrayList<String>());
            else {
                String[] ln = key.split(".");
                if (key.matches("\\S+\\.flags\\.\\S+$"))
                    flags.get(ln[0]).add(key);
            } */
        }

        /*

        for (String key : flags.keySet()){
            Region region = new Region(new Area(cfg.getString(key+".area")));
            region.setOwner(cfg.getString(key+".owners"));
            region.setMember(cfg.getString(key+".members"));
            for (String s : flags.get(key)){
                String id = s.split("\\.")[2];
                String value = cfg.getString(s+".value");
                Relation rel = Relation.getByName(cfg.getString(s+".relate"));
                Flag flag = FlagType.createFlag(id,rel,value);
                RegionsPlugin.getPlugin().getLogger().info(TextFormat.LIGHT_PURPLE+id+" : "+rel.name()+" : "+value+" : "+(flag==null));
                if (flag != null) region.addFlag(flag);
            }
            regions.put(key,region);
        } */
        return regions;
    }




}
