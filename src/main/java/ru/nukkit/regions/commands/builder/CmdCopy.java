package ru.nukkit.regions.commands.builder;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Location;
import ru.nukkit.regions.Regions;
import ru.nukkit.regions.commands.Cmd;
import ru.nukkit.regions.commands.CmdDefine;
import ru.nukkit.regions.util.Message;

import java.util.List;

import static ru.nukkit.regions.Regions.getClipboard;

@CmdDefine(command = "copy", subCommands = {}, permission = "regions.clipboard", description = Message.CBD_COPY_DESK)
public class CmdCopy extends Cmd {
    @Override
    public boolean execute(CommandSender sender, Player player, String[] args) {
        List<Location> locs = Regions.getSelector().getPoints(player);
        if (locs == null || locs.size() != 2) return Message.BUILD_SELECT.print(player);
        getClipboard().copy(player, locs.get(0), locs.get(1));
        int volume = Regions.getClipboard().getVolume(player);
        if (volume == 0) return Message.CBD_COPY_FAIL.print(player);
        return Message.CBD_COPY_OK.print(player, volume);
    }
}
