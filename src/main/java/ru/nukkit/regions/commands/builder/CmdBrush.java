package ru.nukkit.regions.commands.builder;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.item.Item;
import ru.nukkit.regions.RegionsPlugin;
import ru.nukkit.regions.brush.BrushManager;
import ru.nukkit.regions.brush.brushes.Brush;
import ru.nukkit.regions.commands.Cmd;
import ru.nukkit.regions.commands.CmdDefine;
import ru.nukkit.regions.util.Message;


@CmdDefine(command = "brush", alias = "b|brsh", subCommands = {}, permission = "regions.brush", description = Message.BRUSH_DESC)
public class CmdBrush extends Cmd {
    @Override
    public boolean execute(CommandSender sender, Player player, String[] args) {
        if (args.length == 0){
            // brush  - show info
            if (!BrushManager.hasBrush(player)) return Message.BRUSH_NOBRUSH.print(sender);
            Brush brush = BrushManager.getBrush(player);
            Message.BRUSH_INFO.print(sender, brush.toString());
        } else {
            if (args[0].equalsIgnoreCase("give")){
                // brush give
                Item item = BrushManager.createBrushItem();
                if (item == null) Message.BRUSH_ITEM_FAIL.print(sender,RegionsPlugin.getCfg().brushName,RegionsPlugin.getCfg().brushItem);
                player.getInventory().addItem(item);
                Message.BRUSH_ITEM_GIVE.print(sender);
            } else if (args[0].equalsIgnoreCase("set")){
                // /brush set <param> <value>
                if (!BrushManager.hasBrush(player)) return Message.BRUSH_MODIFY_NOBRUSH.print(sender);
                BrushManager.modifyBrush (player, args);
                Brush brush = BrushManager.getBrush(player);
                Message.BRUSH_MODIFY_OK.print(sender,brush.toString());
            } else {
                //brush <brushType> r <radius> [parameters]
                if (!BrushManager.takeBrush(player,args)) return Message.BRUSH_TAKE_FAIL.print(sender);
                Brush brush = BrushManager.getBrush(player);
                Message.BRUSH_TAKE_OK.print(sender, brush.toString());
            }
        }
        return true;
    }
}
