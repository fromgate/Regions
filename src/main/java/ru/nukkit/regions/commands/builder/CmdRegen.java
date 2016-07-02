package ru.nukkit.regions.commands.builder;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.level.generator.Generator;
import cn.nukkit.level.generator.task.GeneratorPool;
import ru.nukkit.regions.Regions;
import ru.nukkit.regions.areas.Area;
import ru.nukkit.regions.builder.ChunkCoord;
import ru.nukkit.regions.clipboard.ClipBlock;
import ru.nukkit.regions.clipboard.Clipboard;
import ru.nukkit.regions.commands.Cmd;
import ru.nukkit.regions.commands.CmdDefine;
import ru.nukkit.regions.util.Message;

import java.util.ArrayList;
import java.util.List;

@CmdDefine(command = "regen", subCommands = {}, permission = "regions.regen", description = Message.CMD_REGEN_DESC)
public class CmdRegen extends Cmd {


    @Override
    public boolean execute(CommandSender sender, Player player, String[] args) {
        List<Location> selection = Regions.getSelector().getPoints(player);
        if (selection.size() != 2) return Message.REGEN_NEED_SEL.print(player);
        selection.get(0).getLevel();
        Area area = new Area(selection.get(0), selection.get(1));

        List<ChunkCoord> chunList = new ArrayList<>();
        for (int x = area.getChunkX1(); x <= area.getChunkX2(); x++)
            for (int z = area.getChunkZ1(); z <= area.getChunkZ2(); z++)
                chunList.add(new ChunkCoord(x, z));

        List<Location> chLocs = getChunkedLocation(area);
        Clipboard chunks = new ClipBlock(chLocs.get(0), chLocs.get(1));
        chunks.remove(new ClipBlock(area));
        Level level = area.getLevel();
        Generator generator = GeneratorPool.get(player.getLevel().getId());

        chunList.forEach(ch -> {

            generator.generateChunk(ch.getX(), ch.getZ());
        });
        chunks.paste();
        return Message.REGEN_OK.print(player, chunList.size());
    }

    public List<Location> getChunkedLocation(Area area) {
        List<Location> locs = new ArrayList<>();
        locs.add(new Location(area.getChunkX1() << 4, 0, area.getChunkZ1() << 4, 0, 0, area.getLevel()));
        locs.add(new Location((area.getChunkX2() << 4) + 15, 0, (area.getChunkZ2() << 4) + 15, 0, 0, area.getLevel()));
        return locs;
    }

}
