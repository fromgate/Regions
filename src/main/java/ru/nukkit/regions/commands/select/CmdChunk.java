package ru.nukkit.regions.commands.select;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Location;
import ru.nukkit.regions.Regions;
import ru.nukkit.regions.commands.Cmd;
import ru.nukkit.regions.commands.CmdDefine;
import ru.nukkit.regions.util.LocUtil;
import ru.nukkit.regions.util.Message;

@CmdDefine(command = "chunk", alias = "selchunk", subCommands = {}, permission = "regions.select.chunk", description = Message.CMD_CHNK_DESC)
public class CmdChunk extends Cmd {
    @Override
    public boolean execute(CommandSender sender, Player player, String[] args) {
        int chX = player.getLocation().getFloorX() >> 4;
        int chZ = player.getLocation().getFloorZ() >> 4;
        Location loc1 = new Location(chX << 4, 0, chZ << 4, 0, 0, player.getLevel());
        Location loc2 = new Location((chX << 4) + 15, LocUtil.getWorldHeight()-1, (chZ << 4) + 15, 0, 0, player.getLevel());
        Message.debugMessage("Select chunk. chX:", chX, "(" + (chX << 4), ((chX << 4) + 15) + ")", "chZ:", chZ, "(" + (chZ << 4), ((chZ << 4) + 15) + ")");
        if (Regions.getSelector().selectPoints(player, loc1, loc2)) return Message.SEL_CHUNK_OK.print(player, chX, chZ);
        else return Message.SEL_CHUNK_FAIL.print(player);
    }
}
