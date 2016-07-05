package ru.nukkit.regions.commands.builder;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Location;
import ru.nukkit.regions.Regions;
import ru.nukkit.regions.commands.Cmd;
import ru.nukkit.regions.commands.CmdDefine;
import ru.nukkit.regions.util.BlockUtil;
import ru.nukkit.regions.util.Message;

import java.util.List;

@CmdDefine(command = "box", alias = "buildbox", subCommands = {"\\S+"}, permission = "regions.builder", description = Message.SET_DESC)
public class CmdBox extends Cmd {
    @Override
    public boolean execute(CommandSender sender, Player player, String[] args) {
        List<Location> locs = Regions.getSelector().getPoints(player);
        if (locs == null || locs.size() != 2) return Message.BUILD_SELECT.print(player);
        Block block = BlockUtil.getNewBlock(args[0]);
        if (block == null) return Message.BUILD_WRONG_ID.print(player, args[0]);
        Regions.getBuilder().setBlockBox(player, block, locs.get(0), locs.get(1));
        return true;
    }
}
