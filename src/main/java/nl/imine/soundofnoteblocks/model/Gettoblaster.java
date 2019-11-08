package nl.imine.soundofnoteblocks.model;

import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.model.SoundCategory;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import nl.imine.api.util.ColorUtil;
import nl.imine.soundofnoteblocks.controller.EntitySongPlayer;
import nl.imine.soundofnoteblocks.model.design.PlayerNotified;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class Gettoblaster extends MusicPlayer implements PlayerNotified {

	public static final double DISTANCE = Math.pow(6, 2);

	private Entity entityCenter;

	public Gettoblaster(Entity entity) {
		this(entity, false, null);
	}

	public Gettoblaster(Entity entity, boolean radioMode, UUID lastTrackId) {
		super(radioMode, lastTrackId);
		entityCenter = entity;
	}

	public Entity getCenteredEntity() {
		return entityCenter;
	}

	@Override
	public Collection<Player> getListeners() {
		Collection<Player> ret = new ArrayList<>();
		for (Player p : getCenteredEntity().getLocation().getWorld().getPlayers()) {
			if (getCenteredEntity().getLocation().distance(p.getLocation()) < DISTANCE) {
				ret.add(p);
			}
		}
		return ret;
	}

	@Override
	public SongPlayer generateSongPlayer(Song song) {
		EntitySongPlayer esp = new EntitySongPlayer(song);
		esp.setCategory(SoundCategory.RECORDS);
		esp.setTargetEntity(entityCenter);
		return esp;
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
			Gettoblaster other = (Gettoblaster) obj;
			return this.getCenteredEntity().equals(other.getCenteredEntity());
		}
		return super.equals(obj);
	}
}
