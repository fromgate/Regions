package ru.nukkit.regions.commands.builder;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import ru.nukkit.regions.Regions;
import ru.nukkit.regions.commands.Cmd;
import ru.nukkit.regions.commands.CmdDefine;
import ru.nukkit.regions.util.Message;

@CmdDefine(command = "undo", subCommands = {}, permission = "regions.undo", description = Message.UNDO_DESC)
public class CmdUndo extends Cmd {
    @Override
    public boolean execute(CommandSender sender, Player player, String[] args) {
        if (!Regions.getBuilder().getUndoManager().playerUndoExist(player.getName())) return Message.UNDO_NOT_FOUND.print(sender,sender.getName());
        if (!Regions.getBuilder().getUndoManager().performUndo(player.getName())) return Message.UNDO_FAIL.print(sender);
        return true;
    }
}
