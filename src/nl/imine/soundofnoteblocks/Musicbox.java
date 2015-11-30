package nl.imine.soundofnoteblocks;

import com.xxmicloxx.NoteBlockAPI.NBSDecoder;
import com.xxmicloxx.NoteBlockAPI.PositionSongPlayer;
import com.xxmicloxx.NoteBlockAPI.Song;
import java.io.Serializable;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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

    private Coordinate coordinate;

    private transient boolean isPlaying;
    private transient PositionSongPlayer songPlayer;

    private static ArrayList<Musicbox> jukeboxList = new ArrayList<>();

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
    }

    public void stopPlaying() {
        isPlaying = false;
        if (songPlayer != null) {
            songPlayer.setPlaying(false);
        }
    }

    public ArrayList<Player> getPlayersInRange() {
        ArrayList<Player> ret = new ArrayList<>();
        for (Player p : coordinate.getWorld().getPlayers()) {
            if (coordinate.toLocation().distance(p.getLocation()) < Math.pow(24, 2)) {
                ret.add(p);
            }
        }
        return ret;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent evt) {
        if (songPlayer != null) {
            if (coordinate.toLocation().getWorld().equals(evt.getPlayer().getLocation().getWorld())) {
                if (evt.getPlayer().getLocation().distance(coordinate.toLocation()) < Math.pow(24, 2)) {
                    songPlayer.addPlayer(evt.getPlayer());
                } else {
                    songPlayer.removePlayer(evt.getPlayer());
                }
            } else {
                songPlayer.removePlayer(evt.getPlayer());
            }
        }
    }

    public Location getLocation() {
        return coordinate.toLocation();
    }
}
