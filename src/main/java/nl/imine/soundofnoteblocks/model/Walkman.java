package nl.imine.soundofnoteblocks.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.xxmicloxx.NoteBlockAPI.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.Song;
import com.xxmicloxx.NoteBlockAPI.SongPlayer;

public class Walkman extends MusicPlayer {

	private UUID playerId;

	public Walkman(Player player) {
		this(player, false, null);
	}

	public Walkman(Player player, boolean radioMode, UUID lastTrackId) {
		super(radioMode, lastTrackId);
		playerId = player.getUniqueId();
	}

	public Player getPlayer() {
		return Bukkit.getPlayer(playerId);
	}

	@Override
	public Collection<Player> getListeners() {
		return Arrays.asList(getPlayer());
	}

	@Override
	public SongPlayer generateSongPlayer(Song song) {
		return new RadioSongPlayer(song);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Walkman) {
			Walkman other = (Walkman) obj;
			return this.getPlayer().equals(other.getPlayer());
		}
		return super.equals(obj);
	}
}