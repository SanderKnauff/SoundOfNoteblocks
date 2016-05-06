package nl.imine.soundofnoteblocks.model;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.xxmicloxx.NoteBlockAPI.Song;
import com.xxmicloxx.NoteBlockAPI.SongPlayer;

import nl.imine.api.util.ColorUtil;
import nl.imine.soundofnoteblocks.Track;
import nl.imine.soundofnoteblocks.TrackManager;
import nl.imine.soundofnoteblocks.model.design.Playable;
import nl.imine.soundofnoteblocks.model.design.Radioable;
import nl.imine.soundofnoteblocks.model.design.Tagable;

public abstract class MusicPlayer implements Playable, Radioable {

	protected boolean inRadioMode;
	protected Track lastTrack;
	protected transient SongPlayer songplayer;

	public MusicPlayer(boolean radioMode, UUID lastTrackId) {
		inRadioMode = radioMode;
		if (lastTrackId != null) {
			lastTrack = TrackManager.getTrack(lastTrackId);
		}
	}

	public abstract SongPlayer generateSongPlayer(Song song);

	@Override
	public void setRadioMode(boolean isRadioMode) {
		inRadioMode = isRadioMode;
		if (inRadioMode && !isPlaying()) {
			playRandomTrack(TrackManager.getTrackArray());
		}
	}

	@Override
	public boolean isRadioMode() {
		return inRadioMode;
	}

	@Override
	public boolean isPlaying() {
		if (songplayer == null) {
			return false;
		}
		return songplayer.isPlaying();
	}

	@Override
	public void setPlaying(boolean playing) {
		if (songplayer != null) {
			songplayer.setPlaying(playing);
		}
	}

	@Override
	public void playRandomTrack(Track[] repos) {
		playTrack(repos[(int) (Math.random() * repos.length)]);
	}

	@Override
	public void playTrack(Track track) {
		if (isPlaying()) {
			if (isRadioMode()) {
				return;
			}
			stopPlaying();
		}
		lastTrack = track;
		Song song = track.getSong();
		setSongPlayer(generateSongPlayer(song));
		songplayer.setAutoDestroy(true);
		songplayer.setPlaying(true);
		for (UUID uuid : songplayer.getPlayerList()) {
			songplayer.removePlayer(Bukkit.getPlayer(uuid));
		}
		for (Player pl : getListeners()) {
			songplayer.addPlayer(pl);
		}
		if (this instanceof Tagable) {
			Tagable tag = (Tagable) this;
			tag.getTag().getLine(0).setLabel(ColorUtil.replaceColors("&e%s", lastTrack.getName()));
			tag.getTag().getLine(1).setLabel(ColorUtil.replaceColors("&9%s", lastTrack.getArtist()));
			tag.getTag().setLocation(tag.getTagLocation());
		}
	}

	@Override
	public void stopPlaying() {
		if (songplayer != null) {
			if (isPlaying()) {
				songplayer.setPlaying(false);
			}
			songplayer.destroy();
		}
	}

	@Override
	public void replay() {
		Track lasttrack = getLastTrack();
		if (!isPlaying() && lasttrack != null) {
			playTrack(lasttrack);
		}
	}

	@Override
	public void replayForce() {
		stopPlaying();
		replay();
	}

	@Override
	public Track getLastTrack() {
		return lastTrack;
	}

	@Override
	public void setLastTrack(Track track) {
		lastTrack = track;
	}

	@Override
	public SongPlayer getSongPlayer() {
		return songplayer;
	}

	@Override
	public void setSongPlayer(SongPlayer songPlayer) {
		this.songplayer = songPlayer;
	}
}
