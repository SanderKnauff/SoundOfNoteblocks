package nl.imine.soundofnoteblocks;

import com.xxmicloxx.NoteBlockAPI.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.Song;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import nl.imine.api.holotag.Tag;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Walkman extends Musicbox {

	private final Player player;

	private static final ArrayList<Walkman> walkmanList = new ArrayList<>();

	public static List<Walkman> getWalkmans() {
		return walkmanList;
	}

	public static Walkman findWalkman(Player player) {
		for (Walkman w : walkmanList) {
			if (w.getPlayer().equals(player)) {
				return w;
			}
		}
		Walkman w = new Walkman(player);
		walkmanList.add(w);
		return w;
	}

	public static void removeWalkman(Walkman walkman) {
		walkman.stopPlaying();
		walkmanList.remove(walkman);
	}

	public Walkman(Player player) {
		super();
		this.player = player;
	}

	@Override
	public void playTrack(Track track) {
		if (isPlaying) {
			if (isRadioMode()) {
				return;
			}
			stopPlaying();
		}
		lastTrack = track;
		isPlaying = true;
		Song song = track.getSong();
		songPlayer = new RadioSongPlayer(song);
		songPlayer.setAutoDestroy(true);
		songPlayer.setPlaying(true);
		for (UUID uuid : songPlayer.getPlayerList()) {
			songPlayer.removePlayer(Bukkit.getPlayer(uuid));
		}
		songPlayer.addPlayer(player);

	}

	@Override
	public void stopPlaying() {
		isPlaying = false;
		if (songPlayer != null) {
			if (songPlayer.isPlaying()) {
				songPlayer.setPlaying(false);
			}
			songPlayer.destroy();
		}
	}

	@Override
	public Location getTagLocation() {
		return null;
	}

	@Override
	public Tag getTag() {
		return null;
	}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}
}
