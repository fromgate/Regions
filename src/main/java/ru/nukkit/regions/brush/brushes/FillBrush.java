package ru.nukkit.regions.brush.brushes;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import ru.nukkit.regions.Regions;
import ru.nukkit.regions.clipboard.Clipboard;
import ru.nukkit.regions.util.BlockUtil;
import ru.nukkit.regions.util.Param;

import java.util.LinkedList;

/**
 * Created by Igor on 29.06.2016.
 */
public class FillBrush extends Brush {



    Block block;

    public FillBrush(){
        this.block = null;
        this.radius = 10;
    }

    @Override
    public boolean init(String[] args) {
        Param param = Param.fromArgs(args,0);
        if (param.matchAnyParam("(?i)radius|r"))
            this.radius = param.getParam("radius",param.getParam("r",10));
        if (param.matchAnyParam("(?i)block|b")){
            String blockStr = param.getParam("block", param.getParam("b"));
            block = BlockUtil.getNewBlock(blockStr);
        }
        return block != null && block.getId() != 0;
    }

    @Override
    public boolean paint(Player player, Block clickedBlock) {
        Location center = getTargetAir(player);
        if (center == null) return false;
        return floodFill(player, center);
    }


    public boolean floodFill (Player player, Location start){
        LinkedList<Position> blocks = new LinkedList<>();
        if (start.getLevelBlock().getId() !=0) return false;
        if (block == null|| block.getId() == 0) return false;

        Clipboard undo = Clipboard.createUndoClipBoard(player.getName());

        undo.add(start.getLevelBlock());

        blocks.addFirst(new Position(start.getFloorX(),start.getFloorY(), start.getFloorZ(), start.getLevel()));
        while (!blocks.isEmpty()){
            Position p = blocks.removeLast();
            if (p.distance(new Position(start.getFloorX(), p.getY(), start.getFloorZ(), p.getLevel())) > radius) continue;
            if (p.getY()<0||p.getY()>127) continue;
            Block b = p.getLevelBlock();
            if (b.getId()!=0) continue;

            if (undo!=null) undo.add(b);

            p.getLevel().setBlock(b,block, false, true);

            blocks.addFirst(new Position(b.x-1,b.y,b.z,b.level));
            blocks.addFirst(new Position(b.x+1,b.y,b.z,b.level));
            blocks.addFirst(new Position(b.x,b.y,b.z-1,b.level));
            blocks.addFirst(new Position(b.x,b.y,b.z+1,b.level));
            blocks.addFirst(new Position(b.x,b.y-1,b.z,b.level));

        }
        Regions.getUndoManager().add(undo);
        return true;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder("FILL (");
        sb.append("radius: ").append(radius);


        if (block!=null) {
            sb.append(", block: ").append(block.getName());
            if (block.getDamage()!=0) sb.append(":").append(block.getDamage());
        }

        sb.append(")");
        return sb.toString();
    }

}
