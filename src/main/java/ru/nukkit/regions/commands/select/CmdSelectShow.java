package ru.nukkit.regions.commands.select;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.permission.Permission;
import ru.nukkit.regions.RegionsPlugin;
import ru.nukkit.regions.commands.Cmd;
import ru.nukkit.regions.commands.CmdDefine;
import ru.nukkit.regions.util.Message;
import ru.nukkit.regions.util.ShowParticle;

@CmdDefine(command = "select", alias = "sel", subCommands = {"(?i)show"}, permission = "regions.select.particles", defaultPerm = Permission.DEFAULT_TRUE, description = Message.SEL_SHOW_DESC)
public class CmdSelectShow extends Cmd {
    @Override
    public boolean execute(CommandSender sender, Player player, String[] args) {
        if (!RegionsPlugin.getCfg().selectionShow) return Message.SEL_SHOW_DISABLED.print(player);
        ShowParticle.toggleSelShow(player);
        return (ShowParticle.isSelShow(player) ? Message.SEL_SHOW_PLR_ENABLED : Message.SEL_SHOW_PLR_DISABLED).print(player);
    }
}
