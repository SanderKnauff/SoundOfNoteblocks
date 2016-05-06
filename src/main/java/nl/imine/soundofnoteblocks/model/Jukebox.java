package nl.imine.soundofnoteblocks.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.xxmicloxx.NoteBlockAPI.PositionSongPlayer;
import com.xxmicloxx.NoteBlockAPI.Song;
import com.xxmicloxx.NoteBlockAPI.SongPlayer;

import nl.imine.api.holotag.ITag;
import nl.imine.api.holotag.TagAPI;
import nl.imine.soundofnoteblocks.model.design.Lockable;
import nl.imine.soundofnoteblocks.model.design.MusicLocation;
import nl.imine.soundofnoteblocks.model.design.Tagable;

public class Jukebox extends MusicPlayer implements Tagable, Lockable, MusicLocation {

	public static final double DISTANCE = Math.pow(6, 2);

	private Location location;
	private transient ITag tag;
	private boolean isLocked;
	private boolean isVisible;

	public Jukebox(Location loc) {
		this(loc, true, false, null);
	}

	public Jukebox(Location loc, boolean visible, boolean radioMode, UUID lastTrackId) {
		super(radioMode, lastTrackId);
		location = loc.clone();
		isVisible = visible;
		getTag();
	}

	@Override
	public Location getTagLocation() {
		Location loc = getLocation();
		if (loc.clone().add(0, 1, 0).getBlock().getType().isTransparent()) {
			loc.add(0, -1, 0);
		}
		return loc.add(0.5, 0.5, 0.5);
	}

	@Override
	public ITag getTag() {
		if (tag == null) {
			tag = TagAPI.createTag(getTagLocation());
			tag.addLine("Loading...");
			tag.addLine("^,^");
			tag.setVisible(false);
		}
		return tag;
	}

	@Override
	public void setTagLines(String... lines) {
		ITag tag = getTag();
		for (int i = 0; i < lines.length; i++) {
			tag.setLine(i, lines[i]);
		}
		tag.setVisible(isVisible());
	}

	@Override
	public void playTrack(Track track) {
		if (getLocation().getChunk().isLoaded()) {
			if (getLocation().getBlock().getType() == Material.JUKEBOX) {
				super.playTrack(track);
			}
		}
	}

	@Override
	public void stopPlaying() {
		super.stopPlaying();
		getTag().remove();
	}

	@Override
	public Location getLocation() {
		return location.clone();
	}

	@Override
	public Collection<Player> getListeners() {
		Collection<Player> ret = new ArrayList<>();
		for (Player p : getLocation().getWorld().getPlayers()) {
			if (getLocation().distance(p.getLocation()) < DISTANCE) {
				ret.add(p);
			}
		}
		return ret;
	}

	@Override
	public void setLocked(boolean locked) {
		isLocked = locked;
	}

	@Override
	public boolean isLocked() {
		return isLocked;
	}

	@Override
	public SongPlayer generateSongPlayer(Song song) {
		PositionSongPlayer psp = new PositionSongPlayer(song);
		psp.setTargetLocation(getLocation());
		return psp;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Jukebox) {
			Jukebox other = (Jukebox) obj;
			return this.getLocation().equals(other.getLocation());
		}
		return super.equals(obj);
	}

	@Override
	public void setVisible(boolean visible) {
		isVisible = visible;
		getTag().setVisible(visible);
	}

	@Override
	public boolean isVisible() {
		return isVisible;
	}
}
