package nl.imine.soundofnoteblocks.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import nl.imine.soundofnoteblocks.model.Track;
import org.bukkit.plugin.Plugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TrackManager {
    private static final String TRACK_LIST_FILE_NAME = "trackList.json";

    private final Plugin plugin;
    private final Gson gson;
    private final List<Track> trackList;

    public TrackManager(Plugin plugin, Gson gson) {
        this.plugin = plugin;
        this.gson = gson;
        this.trackList = new ArrayList<>();
    }

    private void loadTracks() {
        trackList.clear();
        try {
            Path resolve = plugin.getDataFolder().toPath().resolve(TRACK_LIST_FILE_NAME);
            BufferedReader bufferedReader = Files.newBufferedReader(resolve);
            trackList.addAll(gson.fromJson(bufferedReader, new TypeToken<ArrayList<Track>>() {
            }.getType()));
        } catch (IOException e) {
            plugin.getLogger().warning("Failed loading tracks from disk. (" + e.getClass().getName() + ": " + e.getMessage() + ")");
        }
    }

    public List<Track> getTracks() {
        return trackList;
    }

    public void reloadTracks() {
        loadTracks();
    }

    public Track getTrack(UUID trackId) {
        return trackList.stream().filter(t -> t.id().equals(trackId)).findFirst().orElse(null);
    }
}
