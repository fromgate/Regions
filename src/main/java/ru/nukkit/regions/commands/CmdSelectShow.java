package ru.nukkit.regions.commands;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import ru.nukkit.regions.RegionsPlugin;
import ru.nukkit.regions.selector.ShowParticle;
import ru.nukkit.regions.util.Message;

@CmdDefine(command = "select", alias = "sel",subCommands = {"(?i)show"} , permission = "regions.select.particles", description = Message.SEL_SHOW_DESC)
public class CmdSelectShow extends Cmd {
    @Override
    public boolean execute(CommandSender sender, Player player, String[] args) {
        if (!RegionsPlugin.getCfg().selectionShow) return Message.SEL_SHOW_DISABLED.print(player);
        ShowParticle.toggleSelShow(player);
        return (ShowParticle.isSelShow(player) ? Message.SEL_SHOW_PLR_ENABLED : Message.SEL_SHOW_PLR_DISABLED).print(player);
    }
}
