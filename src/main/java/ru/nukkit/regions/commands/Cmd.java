package ru.nukkit.regions.commands;


import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.permission.Permission;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import ru.nukkit.regions.util.Message;

public abstract class Cmd {
    private String command;
    private String[] aliases;
    private String[] subCommands;
    private String permission;
    private boolean allowConsole;
    Message description;
    private String defaultPerm;
    String cmdDesc;

    public Cmd() {
        if (this.getClass().isAnnotationPresent(CmdDefine.class)) {
            CmdDefine cd = this.getClass().getAnnotation(CmdDefine.class);
            this.command = cd.command();
            this.subCommands = cd.subCommands();
            this.permission = cd.permission();
            this.defaultPerm = cd.defaultPerm();
            this.allowConsole = cd.allowConsole();
            this.description = cd.description();
            if (cd.alias().isEmpty()) this.aliases = new String[]{};
            else this.aliases = cd.alias().split(",");
        }
    }

    public boolean isCommand(String cmdLabel) {
        if (this.command.equalsIgnoreCase(cmdLabel)) return true;
        if (aliases != null){
            for (String alias : aliases){
                if (alias.equalsIgnoreCase(cmdLabel)) return true;
            }
        }
        return false;
    }

    public boolean canExecute(CommandSender sender) {
        Player player = (sender instanceof Player) ? (Player) sender : null;
        if (player == null) return this.allowConsole;
        if (this.permission == null || this.permission.isEmpty()) return true;
        return player.hasPermission(this.permission);
    }

    public boolean isValidCommand() {
        return (this.getCommand() != null && !this.getCommand().isEmpty());
    }

    public String getCommand() {
        return this.command;
    }

    public String[] getAliases() {
        return this.aliases;
    }

    public boolean checkParams(String[] params) {
        if (this.subCommands == null) return true;
        if (this.subCommands.length == 0) return true;
        if (params.length < this.subCommands.length) return false;
        for (int i = 0; i < this.subCommands.length; i++) {
            if (!params[i].matches("(?i)" + this.subCommands[i])) return false;
        }
        return true;
    }

    public boolean executeCommand(CommandSender sender, String[] params) {
        if (!this.checkParams(params)) return false;
        Player player = sender instanceof Player ? (Player) sender : null;
        if (player == null && !this.allowConsole) return Message.PLAYER_COMMAD_ONLY.print(sender);
        if (!canExecute(sender)) return Message.PERMISSION_FAIL.print(sender);
        return execute(sender, player, params);
    }

    public abstract boolean execute(CommandSender sender, Player player, String[] args);

    public String getDescription() {
        return this.description.getText("NOCOLOR");
    }

    public PluginBase getPlugin() {
        return Commander.getPlugin();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(this.getCommand());
        if (this.getAliases().length > 0) {
            String[] ln = this.getAliases();
            sb.append(ln.length > 1 ? " (aliases: " : " (alias: ");
            for (int i = 0; i < ln.length; i++) {
                if (i > 0) sb.append(", ");
                sb.append(ln[i]);
            }
            sb.append(")");
        }

        if (this.subCommands.length > 0) {
            sb.append(" (sub command: ");
            for (int i = 0; i < this.subCommands.length; i++) {
                if (i > 0) sb.append(", ");
                sb.append(this.subCommands[i]);
            }
            sb.append(")");
        }

        if (permission != null && !this.permission.isEmpty()) sb.append(" permission: ").append(this.permission);
        return sb.toString();
    }

    public String getHelpString() {
        return TextFormat.clean(/*"/"+this.getCommand()+(this.subCommands.length>0? " "+ Commander.unsplit(this.subCommands) : "")+" - "+*/this.getDescription());
    }

    public String[] getSubCommands() {
        return this.subCommands;
    }

    public String getPermission() {
        return permission;
    }

    public String getDefaultPerm() {
        return this.defaultPerm;
    }


    public void registerPermission() {
        Permission permission = Server.getInstance().getPluginManager().getPermission(getPermission());
        if (permission != null) {
            Message.debugMessage("Permission already registered: ", permission.getName(), permission.getDefault());
            return;
        }
        permission = new Permission(getPermission(), getDescription(), defaultPerm);
        Server.getInstance().getPluginManager().addPermission(permission);
        Message.debugMessage("Permission registered:", getPermission(), defaultPerm);
    }
}
