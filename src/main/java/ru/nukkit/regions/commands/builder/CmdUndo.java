package ru.nukkit.regions.commands.builder;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import ru.nukkit.regions.Regions;
import ru.nukkit.regions.commands.Cmd;
import ru.nukkit.regions.commands.CmdDefine;
import ru.nukkit.regions.util.Message;

@CmdDefine(command = "undo", alias = "u", subCommands = {}, permission = "regions.undo", description = Message.UNDO_DESC)
public class CmdUndo extends Cmd {
    @Override
    public boolean execute(CommandSender sender, Player player, String[] args) {
        if (!Regions.getUndoManager().playerUndoExist(player.getName()))
            return Message.UNDO_NOT_FOUND.print(sender, sender.getName());
        if (args.length==1 && args[0].equals("clear")){
            Regions.getUndoManager().clear(player.getName());
            Message.UNDO_CLEAR_OK.print(sender);
        } else {
            if (Regions.getUndoManager().performUndo(player.getName())) Message.UNDO_OK.print(sender);
            else Message.UNDO_FAIL.print(sender);
        }
        return true;
    }
}
