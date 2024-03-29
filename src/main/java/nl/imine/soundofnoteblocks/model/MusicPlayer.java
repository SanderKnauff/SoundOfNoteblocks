package nl.imine.soundofnoteblocks.model;

import java.util.List;
import java.util.UUID;

import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.xxmicloxx.NoteBlockAPI.model.Song;

import nl.imine.soundofnoteblocks.controller.TrackManager;
import nl.imine.soundofnoteblocks.model.design.Playable;
import nl.imine.soundofnoteblocks.model.design.PlayerNotified;
import nl.imine.soundofnoteblocks.model.design.Radioable;
import nl.imine.soundofnoteblocks.model.design.Tagable;

public abstract class MusicPlayer implements Playable, Radioable {

    private boolean inRadioMode;
    private Track lastTrack;
    private transient SongPlayer songplayer;

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
            playRandomTrack(TrackManager.getTracks());
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
        for (UUID uuid : songplayer.getPlayerUUIDs()) {
            songplayer.removePlayer(Bukkit.getPlayer(uuid));
        }
        for (Player pl : getListeners()) {
            songplayer.addPlayer(pl);
        }
        if (this instanceof Tagable) {
            Tagable tag = (Tagable) this;
            tag.getTag().setLocation(tag.getTagLocation());
            tag.setTagLines(ChatColor.YELLOW + track.getName(), ChatColor.DARK_AQUA + track.getArtist());
            tag.setVisible(tag.isVisible());
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
