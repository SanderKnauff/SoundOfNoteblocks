package nl.imine.soundofnoteblocks.model;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import nl.imine.api.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import nl.imine.soundofnoteblocks.model.design.PlayerNotified;

public class Walkman extends MusicPlayer implements PlayerNotified {

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
		return Collections.singletonList(getPlayer());
	}

	@Override
	public SongPlayer generateSongPlayer(Song song) {
		return new RadioSongPlayer(song);
	}

	@Override
	public void notifyPlayers(Track track) {
		for (Player pl : getListeners()) {
			pl.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ColorUtil.replaceColors("&e%s &7from &e%s&7.", track.getName(), track.getArtist())));
		}
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
