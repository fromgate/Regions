package ru.nukkit.regions.commands.builder;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Location;
import ru.nukkit.regions.commands.Cmd;
import ru.nukkit.regions.commands.CmdDefine;
import ru.nukkit.regions.util.Message;


@CmdDefine(command = "up", alias = "jumpup", subCommands = {"\\d+"} , permission = "regions.builder.up", description = Message.UP_DESC)
public class CmdUp extends Cmd {
    @Override
    public boolean execute(CommandSender sender, Player player, String[] args) {
        int height = Integer.parseInt(args[0]);
        if (height<=3) return Message.UP_HEIGHT_FAIL.print(player);
        Location loc = player.getLocation().add(0,height,0);
        if (loc.getY()>= 125) loc.setComponents(loc.getX(),124,loc.getZ());

        if (loc.getLevelBlock().getId()!=0&&
                loc.add(0,1,0).getLevelBlock().getId()!=0&&
                loc.add(0,2,0).getLevelBlock().getId()!=0) return Message.UP_EMPTY_FAIL.print(player);

        loc.getLevel().setBlock(loc, Block.get(Block.GLASS));
        player.teleport(loc.add(0,1,0));
        return true;
    }
}
