package ru.nukkit.regions.commands.regions;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Location;
import cn.nukkit.permission.Permission;
import ru.nukkit.regions.Regions;
import ru.nukkit.regions.commands.Cmd;
import ru.nukkit.regions.commands.CmdDefine;
import ru.nukkit.regions.util.Message;
import ru.nukkit.regions.util.StringUtil;

import java.util.List;


@CmdDefine(command = "region", alias = "rg", subCommands = {"define|create|d", "\\S+"}, permission = "regions.define", defaultPerm = Permission.DEFAULT_TRUE, description = Message.RG_DEFINE_DESC)
public class CmdRgDefine extends Cmd {
    @Override
    public boolean execute(CommandSender sender, Player player, String[] args) {
        List<Location> locs = Regions.getSelector().getPoints(player);
        if (locs == null || locs.size() != 2) return Message.DEF_SELECT.print(player);
        String id = args[1];

        if (Regions.getManager().regionIdUsed(id)) Message.RG_DEF_ID_USED.print(player, id);

        if (Regions.getManager().defineRegion(id, (args.length > 2 ? StringUtil.join(args, 2) : null), locs)) {
            Regions.getSelector().setSelMode(player, false);
            Regions.getSelector().clearSelection(player);
            return Message.RG_DEF_OK.print(sender, id);
        } else return Message.RG_DEF_FAIL.print(sender, id);
    }
}