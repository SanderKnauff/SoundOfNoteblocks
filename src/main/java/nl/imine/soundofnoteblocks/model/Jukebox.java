package nl.imine.soundofnoteblocks.model;

import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.PositionSongPlayer;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import nl.imine.api.holotag.TagAPI;
import nl.imine.soundofnoteblocks.controller.TrackManager;
import nl.imine.soundofnoteblocks.model.design.Lockable;
import nl.imine.soundofnoteblocks.model.design.MusicLocation;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

public class Jukebox extends MusicPlayer implements Lockable, MusicLocation {
    public static final double DISTANCE = Math.pow(6, 2);
    private static final double LINE_DISTANCE = .3;

    private final Location location;
    private final TagAPI tagAPI;

    private boolean locked;
    private boolean shouldShowTags;
    private @Nullable TextDisplay titleText;
    private @Nullable TextDisplay artistText;


    public Jukebox(Location location, TagAPI tagAPI, TrackManager trackManager) {
        super(false, null, trackManager);
        this.location = location;
        this.tagAPI = tagAPI;

        this.locked = false;
        this.shouldShowTags = true;
    }

    private Location getTagLocation() {
        return getLocation().add(0.5, 1.5, 0.5);
    }

    @Override
    public void playTrack(Track track) {
        super.playTrack(track);

        final var world = getTagLocation().getWorld();
        if (world == null) {
            return;
        }

        if (shouldShowTags) {
            spawnTextDisplays();
        }
    }

    @Override
    public void setPlaying(boolean playing) {
        super.setPlaying(playing);
        if (!playing) {
            removeTextDisplays();
        }
    }

    @Override
    public void stopPlaying() {
        super.stopPlaying();
        removeTextDisplays();
    }

    @Override
    public Location getLocation() {
        return location.clone();
    }

    @Override
    public Collection<Player> getListeners() {
        final var world = getLocation().getWorld();
        if (world == null) {
            return List.of();
        }

        return world
                .getPlayers()
                .stream()
                .filter(player -> getLocation().distance(player.getLocation()) <= DISTANCE)
                .toList();
    }

    @Override
    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    @Override
    public boolean isLocked() {
        return locked;
    }

    public SongPlayer generateSongPlayer(Song song) {
        PositionSongPlayer psp = new PositionSongPlayer(song);
        psp.setTargetLocation(getLocation());
        return psp;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Jukebox other) {
            return this.getLocation().equals(other.getLocation());
        }
        return super.equals(obj);
    }

    public void toggleTags() {
        if (this.shouldShowTags) {
            hideTags();
        } else {
            showTags();
        }
    }

    public void hideTags() {
        this.shouldShowTags = false;
        this.removeTextDisplays();
    }

    public void showTags() {
        this.shouldShowTags = true;
        this.spawnTextDisplays();
    }

    private void removeTextDisplays() {
        if (titleText != null) {
            titleText.remove();
            titleText = null;
        }
        if (artistText != null) {
            artistText.remove();
            artistText = null;
        }
    }

    private void spawnTextDisplays() {
        titleText = spawnTextDisplay(getTagLocation(), ChatColor.YELLOW + getCurrentTrack().name());
        artistText = spawnTextDisplay(getTagLocation().subtract(0, LINE_DISTANCE, 0), ChatColor.DARK_AQUA + getCurrentTrack().artist());
    }

    private TextDisplay spawnTextDisplay(Location location, String text) {
        if (location.getWorld() == null) {
            return null;
        }
        final var textDisplay = (TextDisplay) location.getWorld().spawnEntity(location, EntityType.TEXT_DISPLAY);
        textDisplay.setGravity(false);
        textDisplay.setText(text);
        textDisplay.setBillboard(Display.Billboard.CENTER);
        tagAPI.writeSessionKey(textDisplay);
        return textDisplay;
    }
}
