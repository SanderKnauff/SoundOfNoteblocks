package nl.imine.soundofnoteblocks;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 *
 * @author Sander
 */
public class Coordinate {

    private UUID uuid;
    private int x;
    private int y;
    private int z;

    public Coordinate(Location location) {
        this.uuid = location.getWorld().getUID();
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
    }

    public Coordinate(UUID uuid, int x, int y, int z) {
        this.uuid = uuid;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public World getWorld(){
        return Bukkit.getWorld(uuid);
    }
    
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }
    
    public Location toLocation(){
        return new Location(getWorld(), x, y, z);
    }

}
