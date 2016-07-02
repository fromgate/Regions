package ru.nukkit.regions.commands.builder;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Location;
import cn.nukkit.level.generator.biome.Biome;
import cn.nukkit.utils.BlockColor;
import ru.nukkit.regions.Regions;
import ru.nukkit.regions.builder.WeatherMan;
import ru.nukkit.regions.commands.Cmd;
import ru.nukkit.regions.commands.CmdDefine;
import ru.nukkit.regions.util.Message;

import java.util.List;


@CmdDefine(command = "biome", alias = "biomeset|biom|wm", subCommands = {}, permission = "regions.builder", description = Message.BIOME_DESC)
public class CmdBiome extends Cmd {
    @Override
    public boolean execute(CommandSender sender, Player player, String[] args) {

        if (args.length==0){
            int biomeId = player.getLevel().getBiomeId(player.getFloorX(),player.getFloorZ());
            Biome biome = Biome.getBiome(biomeId);
            return Message.BUILD_BIOMESHOW.print(sender, biome.getName());
        }

        if (args[0].equalsIgnoreCase("list")) {
            String biomeList = WeatherMan.getBiomeList();
            Message m = (biomeList.isEmpty()) ? Message.BUILD_BIOMELISTFAIL :
                    Message.BUILD_BIOMELIST;
            return m.print(sender, biomeList);
        }
        List<Location> locs = Regions.getSelector().getPoints(player);
        if (locs == null || locs.size() != 2) return Message.BUILD_SELECT.print(player);

        Biome biome = null;
        BlockColor biomeColor = null;

        //biome <biome|color>
        if (args.length==1) {
            biome = Biome.getBiome(args[0]);
            if (biome == null) biomeColor = WeatherMan.getColorByName(args[0]);
        // biome <biome> <color>
        } else if (args.length == 2){
            biome = Biome.getBiome(args[0]);
            biomeColor = WeatherMan.getColorByName(args[1]);
        // biome <r> <g> <b>
        } else if (args.length == 3){
            int[] rgb = new int[3];
            for (int i = 0; i<3; i++){
                if (!args[i].matches("\\d+")) return Message.BUILD_BIOME_COLORFAIL.print(sender, args[0],args[1],args[2]);
                rgb[i] = Integer.parseInt(args[i]);
            }
            biomeColor = new BlockColor(rgb[0],rgb[1],rgb[2]);
        } else if (args.length == 4){
            biome = Biome.getBiome(args[0]);
            int[] rgb = new int[3];
            for (int i = 0; i<3; i++){
                if (!args[i].matches("\\d+")) return Message.BUILD_BIOME_COLORFAIL.print(sender, args[1],args[2],args[3]);
                rgb[i] = Integer.parseInt(args[i+1]);
            }
            biomeColor = new BlockColor(rgb[0],rgb[1],rgb[2]);
        }
        if (biome == null && biomeColor==null) return Message.BUILD_BIOME_FAIL.print(sender);
        WeatherMan.setBiome(Regions.getSelector().getSelectedArea(player), biome, biomeColor);
        return Message.BUILD_BIOMEOK.print(sender);
    }




}