package nl.imine.soundofnoteblocks.controller;

import nl.imine.api.holotag.TagAPI;
import nl.imine.soundofnoteblocks.model.Gettoblaster;
import nl.imine.soundofnoteblocks.model.Jukebox;
import nl.imine.soundofnoteblocks.model.MusicPlayer;
import nl.imine.soundofnoteblocks.model.Walkman;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

public class MusicPlayerManager {
    private final Set<MusicPlayer> musicPlayers;
    private final TagAPI tagAPI;
    private final TrackManager trackManager;

    public MusicPlayerManager(TagAPI tagAPI, TrackManager trackManager) {
        this.tagAPI = tagAPI;
        this.trackManager = trackManager;
        this.musicPlayers = new HashSet<>();
    }

    public Collection<Jukebox> getJukeboxes() {
        return musicPlayers.stream()
                .filter(Jukebox.class::isInstance)
                .map(Jukebox.class::cast)
                .collect(Collectors.toSet());
    }

    public Jukebox getOrCreateJukebox(Location location) {
        return musicPlayers.stream()
                .filter(Jukebox.class::isInstance)
                .map(Jukebox.class::cast)
                .filter(jukebox -> jukebox.getLocation().equals(location))
                .findAny()
                .orElseGet(() -> {
                    Jukebox jukebox = new Jukebox(location, tagAPI, trackManager);
                    musicPlayers.add(jukebox);
                    return jukebox;
                });
    }

    public void removeJukeboxesFromChunk(Chunk chunk) {
        Iterator<MusicPlayer> musicPlayerIterator = musicPlayers.iterator();
        while (musicPlayerIterator.hasNext()) {
            MusicPlayer musicPlayer = musicPlayerIterator.next();
            if (!(musicPlayer instanceof Jukebox jukebox)) {
                continue;
            }
            if (!isChunkAtLocation(chunk, jukebox.getLocation())) {
                continue;
            }
            jukebox.stopPlaying();
            if (!jukebox.isRadioMode()) {
                musicPlayerIterator.remove();
            }
        }
    }

    private boolean isChunkAtLocation(Chunk chunk, Location location) {
        return chunk.getX() == location.getBlockX() >> 4 && chunk.getZ() == location.getBlockZ() >> 4;
    }

    public void removeJukebox(Location location) {
        Iterator<MusicPlayer> musicPlayerIterator = musicPlayers.iterator();
        while (musicPlayerIterator.hasNext()) {
            MusicPlayer musicPlayer = musicPlayerIterator.next();
            if (musicPlayer instanceof Jukebox && ((Jukebox) musicPlayer).getLocation().equals(location)) {
                musicPlayer.setRadioMode(false);
                musicPlayer.stopPlaying();
                musicPlayerIterator.remove();
                break;
            }
        }
    }

    public Walkman getOrCreateWalkman(Player player) {
        return musicPlayers.stream()
                .filter(Walkman.class::isInstance)
                .map(Walkman.class::cast)
                .filter(walkman -> walkman.getPlayer().equals(player))
                .findAny()
                .orElseGet(() -> {
                    Walkman walkman = new Walkman(player, trackManager);
                    musicPlayers.add(walkman);
                    return walkman;
                });
    }

    public void removeWalkman(Player player) {
        Iterator<MusicPlayer> musicPlayerIterator = musicPlayers.iterator();
        while (musicPlayerIterator.hasNext()) {
            MusicPlayer musicPlayer = musicPlayerIterator.next();
            if (musicPlayer instanceof Walkman && ((Walkman) musicPlayer).getPlayer() == player) {
                musicPlayer.setRadioMode(false);
                musicPlayer.stopPlaying();
                musicPlayerIterator.remove();
                break;
            }
        }
    }

    public Collection<Gettoblaster> getGettoblasters() {
        return musicPlayers.stream()
                .filter(Gettoblaster.class::isInstance)
                .map(Gettoblaster.class::cast)
                .collect(Collectors.toSet());
    }

    public Gettoblaster getOrCreateGettoblaster(Entity entity) {
        return musicPlayers.stream()
                .filter(Gettoblaster.class::isInstance)
                .map(Gettoblaster.class::cast)
                .filter(gettoblaster -> gettoblaster.getCenteredEntity().equals(entity))
                .findAny()
                .orElseGet(() -> {
                    Gettoblaster gettoblaster = new Gettoblaster(entity, trackManager);
                    musicPlayers.add(gettoblaster);
                    return gettoblaster;
                });
    }

    public void removeGettoblaster(Entity entity) {
        Iterator<MusicPlayer> musicPlayerIterator = musicPlayers.iterator();
        while (musicPlayerIterator.hasNext()) {
            MusicPlayer mp = musicPlayerIterator.next();
            if (mp instanceof Gettoblaster && ((Gettoblaster) mp).getCenteredEntity() == entity) {
                mp.setRadioMode(false);
                mp.stopPlaying();
                musicPlayerIterator.remove();
                break;
            }
        }
    }

    public Collection<MusicPlayer> getAllMusicPlayers() {
        return musicPlayers;
    }
}
