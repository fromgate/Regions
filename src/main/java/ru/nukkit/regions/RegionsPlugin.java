package ru.nukkit.regions;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import ru.nukkit.regions.commands.Commander;
import ru.nukkit.regions.flags.FlagType;
import ru.nukkit.regions.listeners.FlagListener;
import ru.nukkit.regions.listeners.RegionListener;
import ru.nukkit.regions.listeners.WalkListener;
import ru.nukkit.regions.util.BlockUtil;
import ru.nukkit.regions.util.Message;
import ru.nukkit.regions.util.RegionsConfig;
import ru.nukkit.regions.util.ShowParticle;

public class RegionsPlugin extends PluginBase {

    private static RegionsPlugin plugin;
    private RegionsConfig cfg;

    @Override
    public void onEnable() {
        plugin = this;
        this.getDataFolder().mkdirs();
        this.saveDefaultConfig();
        this.cfg = new RegionsConfig(this);
        this.cfg.load();
        Message.init(this);
        this.cfg.update();
        FlagType.createDefaults();
        Commander.init(this);
        Regions.init();
        ShowParticle.init();
        BlockUtil.init();
        this.getServer().getPluginManager().registerEvents(new RegionListener(), this);
        this.getServer().getPluginManager().registerEvents(new FlagListener(), this);
        new WalkListener();
        this.getLogger().info(TextFormat.colorize("&cRegions &ecreated for Nukkit.Ru"));
        this.getLogger().info(TextFormat.colorize("&eYou can get more info about Regions at:"));
        this.getLogger().info(TextFormat.colorize("&3http://nukkit.ru/resources/regions.53/"));
    }

    public static RegionsPlugin getPlugin() {
        return plugin;
    }

    public static RegionsConfig getCfg() {
        return plugin.cfg;
    }
}
