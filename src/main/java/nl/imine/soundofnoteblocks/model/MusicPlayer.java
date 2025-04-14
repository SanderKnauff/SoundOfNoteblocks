package nl.imine.soundofnoteblocks.model;

import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import nl.imine.soundofnoteblocks.controller.TrackManager;
import nl.imine.soundofnoteblocks.model.design.Playable;
import nl.imine.soundofnoteblocks.model.design.PlayerNotified;
import nl.imine.soundofnoteblocks.model.design.Radioable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public abstract class MusicPlayer implements Playable, Radioable {
    private final TrackManager trackManager;
    private boolean inRadioMode;
    private Track currentTrack;
    private SongPlayer songplayer;

    public MusicPlayer(boolean radioMode, UUID lastTrackId, TrackManager trackManager) {
        this.inRadioMode = radioMode;
        if (lastTrackId != null) {
            this.currentTrack = trackManager.getTrack(lastTrackId);
        }
        this.trackManager = trackManager;
    }

    public abstract SongPlayer generateSongPlayer(Song song);

    @Override
    public void setRadioMode(boolean isRadioMode) {
        inRadioMode = isRadioMode;
        if (inRadioMode && !isPlaying()) {
            playRandomTrack(trackManager.getTracks());
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
    public void playRandomTrack(List<Track> tracks) {
        playTrack(tracks.get((int) (Math.random() * tracks.size())));
    }

    @Override
    public void playTrack(Track track) {
        if (isPlaying()) {
            stopPlaying();
        }
        currentTrack = track;
        Song song = track.song();
        setSongPlayer(generateSongPlayer(song));
        songplayer.setAutoDestroy(true);
        songplayer.setPlaying(true);
        for (UUID uuid : songplayer.getPlayerUUIDs()) {
            songplayer.removePlayer(Bukkit.getPlayer(uuid));
        }
        for (Player pl : getListeners()) {
            songplayer.addPlayer(pl);
        }
        if (this instanceof PlayerNotified) {
            ((PlayerNotified) this).notifyPlayers(track);
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
        Track lasttrack = currentTrack;
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
    public Track getCurrentTrack() {
        return currentTrack;
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
