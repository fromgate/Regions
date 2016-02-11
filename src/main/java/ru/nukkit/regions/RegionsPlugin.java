package ru.nukkit.regions;

import cn.nukkit.plugin.PluginBase;
import ru.nukkit.regions.commands.Commander;
import ru.nukkit.regions.util.RegionsConfig;
import ru.nukkit.regions.flags.FlagListener;
import ru.nukkit.regions.selector.ShowParticle;
import ru.nukkit.regions.util.Message;

import java.util.ArrayList;

public class RegionsPlugin extends PluginBase{

    private static RegionsPlugin plugin;
    private RegionsConfig cfg;


    @Override
    public void onEnable(){
        plugin = this;
        this.getDataFolder().mkdirs();
        // TODO сохранение конфига из ресурсов
        this.cfg = new RegionsConfig(this);
        this.cfg.load();
        this.cfg.save();
        this.cfg.test = new ArrayList<String>();
        this.cfg.test.add("aaa");
        this.cfg.test.add("bbb");
        this.cfg.save();


        Message.init(this);

        Commander.init(this);

        Regions.init();

        ShowParticle.init();

        this.getServer().getPluginManager().registerEvents(new RegionListener(),this);
        this.getServer().getPluginManager().registerEvents(new FlagListener(),this);

    }

    public static RegionsPlugin getPlugin() {
        return plugin;
    }
    public static RegionsConfig getCfg(){
        return plugin.cfg;
    }
}
