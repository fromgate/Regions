package ru.nukkit.regions.commands.select;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.permission.Permission;
import ru.nukkit.regions.Regions;
import ru.nukkit.regions.areas.Area;
import ru.nukkit.regions.commands.Cmd;
import ru.nukkit.regions.commands.CmdDefine;
import ru.nukkit.regions.util.Message;

@CmdDefine(command = "expand", alias = "selexpand", subCommands = {"up|u|down|d|north|n|south|s|west|west|east|e|x|y|z", "-?\\d+"}, defaultPerm = Permission.DEFAULT_TRUE, permission = "regions.select.expand", description = Message.CMD_EXPAND_DESC)
public class CmdExpand extends Cmd {
    @Override
    public boolean execute(CommandSender sender, Player player, String[] args) {
        int delta = Integer.parseInt(args[1]);

        if (delta <= 0) return Message.SEL_EXPAND_DELTA.print(player);

        if (!Regions.getSelector().isSelectionFinished(player)) return Message.SEL_NEEDSELECT.print(player);
        ExpandType et = ExpandType.getByName(args[0]);
        if (et == null) return Message.SEL_EXPAND_DIR_FAIL.print(player, args[0]);
        Area sel = Regions.getSelector().getSelectedArea(player);
        if (args[0].matches("(?i)w|west|n|north|d|down")) delta = -delta;
        Area area = expandArea(sel, et, delta);
        Regions.getSelector().selectArea(player, area);
        return Message.SEL_OK_AREA.print(player, area.toString());
    }

    private Area expandArea(Area area, ExpandType et, int delta) {
        Area newArea = new Area(area.getLoc1(), area.getLoc2());
        switch (et) {
            case X:
                if (delta < 0) newArea.setX1(newArea.getX1() + delta);
                else newArea.setX2(newArea.getX2() + delta);
            case Y:
                if (delta < 0) newArea.setY1(newArea.getY1() + delta);
                else newArea.setY2(newArea.getY2() + delta);
                break;
            case Z:
                if (delta < 0) newArea.setZ1(newArea.getZ1() + delta);
                else newArea.setZ2(newArea.getZ2() + delta);
                break;
        }
        return newArea;
    }


    enum ExpandType {
        X("(?i)x|east|e|west|w"),
        Y("(?i)up|u|y|d|down"),
        Z("(?i)z|south|s|north|n");

        private String alias;

        ExpandType(String s) {
            this.alias = s;
        }

        public static ExpandType getByName(String expStr) {
            for (ExpandType et : values())
                if (expStr.matches(et.alias)) return et;
            return null;
        }
    }
}
