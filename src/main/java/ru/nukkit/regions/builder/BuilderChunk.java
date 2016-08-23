package ru.nukkit.regions.builder;

import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;

public class BuilderChunk {
    Level level;
    int x;
    int z;

    public BuilderChunk(Location location) {
        level = location.getLevel();
        this.x = location.getFloorX() >> 4;
        this.z = location.getFloorZ() >> 4;
    }

    public BuilderChunk(Level level, int chunkX, int chunkZ) {
        this.level = level;
        this.x = chunkX;
        this.z = chunkZ;
    }

    public void updateChunk() {
        Server.getInstance().getOnlinePlayers().values().forEach(p -> {
            Location center = new Location((x << 4) + 7, p.getY(), (z << 4) + 7, 0, 0, level);
            if (center.distance(p) <= (Server.getInstance().getViewDistance() >> 4)) ;
            level.requestChunk(x, z, p);
        });
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BuilderChunk that = (BuilderChunk) o;
        if (x != that.x) return false;
        if (z != that.z) return false;
        return level.equals(that.level);

    }

    @Override
    public int hashCode() {
        int result = level.hashCode();
        result = 31 * result + x;
        result = 31 * result + z;
        return result;
    }
}
