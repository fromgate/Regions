package ru.nukkit.regions.brush.brushes;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.level.Location;
import ru.nukkit.regions.clipboard.Clipboard;
import ru.nukkit.regions.util.BlockUtil;
import ru.nukkit.regions.util.Param;

import java.util.List;

/**
 * Created by Igor on 28.06.2016.
 */
public class BreakBrush extends Brush {


    Block block;
    boolean ball;

    public BreakBrush() {
        this.block = null;
        this.radius = 3;
        this.ball = true;
    }

    @Override
    public boolean init(String[] args) {
        Param param = Param.fromArgs(args, 0);
        if (param.matchAnyParam("(?i)radius|r"))
            this.radius = param.getParam("radius", param.getParam("r", radius));
        if (param.matchAnyParam("(?i)t|type"))
            this.ball = !param.getParam("type", param.getParam("t", "disk")).matches("(?i)disk|disc|d");
        if (param.matchAnyParam("(?i)block|b")) {
            String blockStr = param.getParam("block", param.getParam("b"));
            block = BlockUtil.getNewBlock(blockStr);
        }
        return true;
    }

    @Override
    public boolean paint(Player player, Block clickedBlock) {
        Location center = clickedBlock == null ? getTarget(player) : clickedBlock.getLocation();
        if (center == null) return false;
        List<Block> blocks = ball ? getBall(center, this.radius) : getDisk(center, radius);
        Block air = Block.get(0);
        Clipboard undo = Clipboard.createUndoClipBoard(player.getName());
        blocks.forEach(b -> {
            if (block == null || block.getId() == b.getId()) {
                if (undo != null) undo.add(b);
                b.getLevel().setBlock(b, air, true, true);
            }
        });

        getUndoManager().add(undo);

        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("BREAK (");
        sb.append("radius: ").append(radius);
        sb.append(", type: ").append(ball ? "ball" : "disk");
        if (block != null) {
            sb.append(", block: ").append(block.getName());
            if (block.getDamage() != 0) sb.append(":").append(block.getDamage());
        }
        sb.append(")");
        return sb.toString();
    }
}
