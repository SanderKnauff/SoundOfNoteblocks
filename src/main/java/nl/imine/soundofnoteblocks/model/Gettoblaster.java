package nl.imine.soundofnoteblocks.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.xxmicloxx.NoteBlockAPI.Song;
import com.xxmicloxx.NoteBlockAPI.SongPlayer;

import nl.imine.api.util.ColorUtil;
import nl.imine.api.util.PlayerUtil;
import nl.imine.soundofnoteblocks.controller.EntitySongPlayer;
import nl.imine.soundofnoteblocks.model.design.PlayerNotified;

public class Gettoblaster extends MusicPlayer implements PlayerNotified {

	public static final double DISTANCE = Math.pow(35, 2);

	private Entity entityCenter;

	public Gettoblaster(Entity entity) {
		this(entity, false, null);
	}

	public Gettoblaster(Entity entity, boolean radioMode, UUID lastTrackId) {
		super(radioMode, lastTrackId);
		entityCenter = entity;
	}

	public Entity getCenterdEntity() {
		return entityCenter;
	}

	@Override
	public Collection<Player> getListeners() {
		Collection<Player> ret = new ArrayList<>();
		for (Player p : getCenterdEntity().getLocation().getWorld().getPlayers()) {
			if (getCenterdEntity().getLocation().distance(p.getLocation()) < DISTANCE) {
				ret.add(p);
			}
		}
		return ret;
	}

	@Override
	public SongPlayer generateSongPlayer(Song song) {
		EntitySongPlayer esp = new EntitySongPlayer(song);
		esp.setTargetEntity(entityCenter);
		return esp;
	}

	@Override
	public void notifyPlayers(Track track) {
		for (Player pl : getListeners()) {
			PlayerUtil.sendActionMessage(pl,
				ColorUtil.replaceColors("&e%s &7from &e%s&7.", track.getName(), track.getArtist()));
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Walkman) {
			Gettoblaster other = (Gettoblaster) obj;
			return this.getCenterdEntity().equals(other.getCenterdEntity());
		}
		return super.equals(obj);
	}
}
