package nl.imine.soundofnoteblocks;

import com.xxmicloxx.NoteBlockAPI.NBSDecoder;
import com.xxmicloxx.NoteBlockAPI.PositionSongPlayer;
import com.xxmicloxx.NoteBlockAPI.Song;
import com.xxmicloxx.NoteBlockAPI.SongDestroyingEvent;
import com.xxmicloxx.NoteBlockAPI.SongEndEvent;
import com.xxmicloxx.NoteBlockAPI.SongStoppedEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 *
 * @author Sander
 */
public class Musicbox implements Listener, Serializable {

    private static final long serialVersionUID = 3971612771253959236L;

    private static final double DISTANCE = Math.pow(35, 2);

    private Coordinate coordinate;

    private ArmorStand tag;

    private transient boolean isPlaying;
    private transient PositionSongPlayer songPlayer;

    private static ArrayList<Musicbox> jukeboxList = new ArrayList<>();

    public static List<Musicbox> getMusicBoxes() {
        return jukeboxList;
    }

    public static Musicbox findJukebox(Location location) {
        for (Musicbox j : jukeboxList) {
            if (j.getLocation().equals(location)) {
                return j;
            }
        }
        Musicbox j = new Musicbox(location);
        jukeboxList.add(j);
        Bukkit.getPluginManager().registerEvents(j, SoundOfNoteBlocks.getInstance());
        return j;
    }

    public static void removeJukebox(Musicbox musicbox) {
        jukeboxList.remove(musicbox);
    }

    private Musicbox(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    private Musicbox(Location location) {
        this(new Coordinate(location));
    }

    public void playTrack(Track track) {
        if (isPlaying) {
            stopPlaying();
        }
        isPlaying = true;
        Song song = NBSDecoder.parse(track.getFile());
        songPlayer = new PositionSongPlayer(song);
        songPlayer.setTargetLocation(coordinate.toLocation());
        songPlayer.setAutoDestroy(true);
        songPlayer.setPlaying(true);
        for (String pname : songPlayer.getPlayerList()) {
            songPlayer.removePlayer(Bukkit.getPlayer(pname));
        }
        for (Player p : getPlayersInRange()) {
            songPlayer.addPlayer(p);
        }
        tag = (ArmorStand) coordinate.getWorld().spawnEntity(coordinate.toLocation().add(0.5, -0.5, 0.5), EntityType.ARMOR_STAND);
        tag.setVisible(false);
        tag.setGravity(false);
        tag.setBasePlate(false);
        tag.setRemoveWhenFarAway(true);
        tag.setCustomName(ChatColor.GOLD + track.getName() + "\n" + ChatColor.BLUE + track.getArtist());
        tag.setCustomNameVisible(true);
    }

    public void stopPlaying() {
        isPlaying = false;
        if (songPlayer != null) {
            songPlayer.setPlaying(false);
        }
        songPlayer.destroy();
        songPlayer = null;
        tag.remove();
    }

    public ArrayList<Player> getPlayersInRange() {
        ArrayList<Player> ret = new ArrayList<>();
        for (Player p : coordinate.getWorld().getPlayers()) {
            if (coordinate.toLocation().distance(p.getLocation()) < DISTANCE) {
                ret.add(p);
            }
        }
        return ret;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent evt) {
        if (songPlayer != null) {
            if (coordinate.toLocation().getWorld().equals(evt.getPlayer().getLocation().getWorld())) {
                if (evt.getPlayer().getLocation().distance(coordinate.toLocation()) < DISTANCE) {
                    songPlayer.addPlayer(evt.getPlayer());
                } else {
                    songPlayer.removePlayer(evt.getPlayer());
                }
            } else {
                songPlayer.removePlayer(evt.getPlayer());
            }
        }
    }

    @EventHandler
    public void onSongStop(SongStoppedEvent evt) {
        stopPlaying();
    }

    public Location getLocation() {
        return coordinate.toLocation();
    }
}
