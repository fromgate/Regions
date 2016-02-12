package ru.nukkit.regions;

import cn.nukkit.plugin.PluginBase;
import ru.nukkit.regions.commands.Commander;
import ru.nukkit.regions.listeners.FlagListener;
import ru.nukkit.regions.listeners.RegionListener;
import ru.nukkit.regions.listeners.WalkListener;
import ru.nukkit.regions.selector.ShowParticle;
import ru.nukkit.regions.util.Message;
import ru.nukkit.regions.util.RegionsConfig;

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


        Message.init(this,true);

        Commander.init(this);

        Regions.init();

        ShowParticle.init();

        this.getServer().getPluginManager().registerEvents(new RegionListener(),this);
        this.getServer().getPluginManager().registerEvents(new FlagListener(),this);
        this.getServer().getPluginManager().registerEvents(new WalkListener(),this);

    }

    public static RegionsPlugin getPlugin() {
        return plugin;
    }
    public static RegionsConfig getCfg(){
        return plugin.cfg;
    }
}
