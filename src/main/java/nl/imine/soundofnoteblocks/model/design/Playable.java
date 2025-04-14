package nl.imine.soundofnoteblocks.model.design;

import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import nl.imine.soundofnoteblocks.model.Track;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

public interface Playable {

    void playRandomTrack(List<Track> repo);

    void playTrack(Track track);

    boolean isPlaying();

    void setPlaying(boolean playing);

    void stopPlaying();

    void replay();

    void replayForce();

    Track getCurrentTrack();

    SongPlayer getSongPlayer();

    void setSongPlayer(SongPlayer songPlayer);

    Collection<Player> getListeners();

}
