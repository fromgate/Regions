package ru.nukkit.regions.commands;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class CommandExecutor extends Command {
    public CommandExecutor(String name) {
        super(name);
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        return Commander.execute(commandSender, s, strings);
    }
}
