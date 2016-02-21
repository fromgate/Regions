package ru.nukkit.regions.commands.builder;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import ru.nukkit.regions.Regions;
import ru.nukkit.regions.commands.Cmd;
import ru.nukkit.regions.commands.CmdDefine;
import ru.nukkit.regions.util.Message;


@CmdDefine(command = "paste", subCommands ={} , permission = "regions.clipboard", description = Message.CBD_PASTE_DESK)
public class CmdPaste extends Cmd {
    @Override
    public boolean execute(CommandSender sender, Player player, String[] args) {
        if (!Regions.getBuilder().getClipboard().hasClipboard(player)) return Message.CBD_PASTE_NOCLIP.print(player);
        Message.CBD_PASTING.print(player);
        Regions.getBuilder().getClipboard().paste(player);
        return true;
    }
}
