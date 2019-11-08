package nl.imine.soundofnoteblocks.model.design;

import java.util.Collection;
import java.util.List;

import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import org.bukkit.entity.Player;


import nl.imine.soundofnoteblocks.model.Track;

public interface Playable {

    void playRandomTrack(List<Track> repo);

    void playTrack(Track track);

    boolean isPlaying();

    void setPlaying(boolean playing);

    void stopPlaying();

    void replay();

    void replayForce();

    Track getLastTrack();

    void setLastTrack(Track track);

    SongPlayer getSongPlayer();

    void setSongPlayer(SongPlayer songPlayer);

    Collection<Player> getListeners();

}
