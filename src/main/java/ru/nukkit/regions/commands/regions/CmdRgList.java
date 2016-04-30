package ru.nukkit.regions.commands.regions;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import ru.nukkit.regions.Regions;
import ru.nukkit.regions.commands.Cmd;
import ru.nukkit.regions.commands.CmdDefine;
import ru.nukkit.regions.manager.Region;
import ru.nukkit.regions.util.Message;

@CmdDefine(command = "region", alias = "rg", subCommands = {"list|lst"}, allowConsole = true, permission = "regions.list", description = Message.RG_LIST_DESC)
public class CmdRgList extends Cmd {
    @Override
    public boolean execute(CommandSender sender, Player player, String[] args) {
        StringBuilder sb = new StringBuilder();
        for (String id : Regions.getManager().getRegions().keySet()) {
            Region r = Regions.getManager().getRegion(id);
            String color = TextFormat.GREEN;
            if (player != null) {
                if (r.isOwner(player.getName())) color = TextFormat.GOLD;
                else if (r.isMember(player.getName())) color = TextFormat.YELLOW;
            }
            if (sb.length() > 0) {
                if (!color.equalsIgnoreCase(TextFormat.GREEN)) sb.append(TextFormat.GREEN);
                sb.append(", ");
            }
            sb.append(color).append(id);
        }
        return Message.RG_LIST.print(sender, sb.toString());
    }
}
