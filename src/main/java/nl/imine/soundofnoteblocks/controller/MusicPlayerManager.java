package nl.imine.soundofnoteblocks.controller;

import nl.imine.soundofnoteblocks.model.Gettoblaster;
import nl.imine.soundofnoteblocks.model.Jukebox;
import nl.imine.soundofnoteblocks.model.MusicPlayer;
import nl.imine.soundofnoteblocks.model.Walkman;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

public class MusicPlayerManager {

    private static Set<MusicPlayer> musicPlayers = new HashSet<>();

    public static Collection<MusicPlayer> getAllMusicPlayers() {
        return musicPlayers;
    }

    public static Collection<Jukebox> getJukeboxes() {
        return musicPlayers.stream()
                .filter(Jukebox.class::isInstance)
                .map(Jukebox.class::cast)
                .collect(Collectors.toSet());
    }

    public static Jukebox getOrCreateJukebox(Location location) {
        return musicPlayers.stream()
                .filter(Jukebox.class::isInstance)
                .map(Jukebox.class::cast)
                .filter(jukebox -> jukebox.getLocation().equals(location))
                .findAny()
                .orElseGet(() -> {
                    Jukebox jukebox = new Jukebox(location);
                    musicPlayers.add(jukebox);
                    return jukebox;
                });
    }

    public static void removeJukebox(Location location) {
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

    public static Collection<Walkman> getWalkmans() {
        return musicPlayers.stream()
                .filter(Walkman.class::isInstance)
                .map(Walkman.class::cast)
                .collect(Collectors.toSet());
    }

    public static Walkman getOrCreateWalkman(Player player) {
        return musicPlayers.stream()
                .filter(Walkman.class::isInstance)
                .map(Walkman.class::cast)
                .filter(walkman -> walkman.getPlayer().equals(player))
                .findAny()
                .orElseGet(() -> {
                    Walkman walkman = new Walkman(player);
                    musicPlayers.add(walkman);
                    return walkman;
                });
    }

    public static void removeWalkman(Player player) {
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

    public static Collection<Gettoblaster> getGettoblasters() {
        return musicPlayers.stream()
                .filter(Gettoblaster.class::isInstance)
                .map(Gettoblaster.class::cast)
                .collect(Collectors.toSet());
    }

    public static Gettoblaster getGettoblaster(Entity entity) {
        return musicPlayers.stream()
                .filter(Gettoblaster.class::isInstance)
                .map(Gettoblaster.class::cast)
                .filter(gettoblaster -> gettoblaster.getCenteredEntity().equals(entity))
                .findFirst()
                .orElse(null);
    }

    public static Gettoblaster getOrCreateGettoblaster(Entity entity) {
        return musicPlayers.stream()
                .filter(Gettoblaster.class::isInstance)
                .map(Gettoblaster.class::cast)
                .filter(gettoblaster -> gettoblaster.getCenteredEntity().equals(entity))
                .findAny()
                .orElseGet(() -> {
                    Gettoblaster gettoblaster = new Gettoblaster(entity);
                    musicPlayers.add(gettoblaster);
                    return gettoblaster;
                });
    }

    public static void removeGettoblaster(Entity entity) {
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
}
