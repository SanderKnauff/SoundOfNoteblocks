package nl.imine.soundofnoteblocks.model.design;

import java.util.Collection;

import org.bukkit.entity.Player;

import com.xxmicloxx.NoteBlockAPI.SongPlayer;

import nl.imine.soundofnoteblocks.Track;

public interface Playable {

	public void playRandomTrack(Track[] repo);

	public void playTrack(Track track);

	public boolean isPlaying();

	public void setPlaying(boolean playing);

	public void stopPlaying();

	public void replay();

	public void replayForce();

	public Track getLastTrack();

	void setLastTrack(Track track);

	public SongPlayer getSongPlayer();

	public void setSongPlayer(SongPlayer songPlayer);

	public Collection<Player> getListeners();

}
