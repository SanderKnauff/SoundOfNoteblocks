package nl.imine.soundofnoteblocks.controller;

import com.google.gson.reflect.TypeToken;
import nl.imine.soundofnoteblocks.SoundOfNoteBlocksPlugin;
import nl.imine.soundofnoteblocks.model.Track;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TrackManager {

    private static final String TRACK_LIST_FILE_NAME = "trackList.json";
    private static List<Track> trackList = new ArrayList<>();

    public TrackManager() {
    }

    private void loadTracks() {
        trackList.clear();
        try {
            Path resolve = SoundOfNoteBlocksPlugin.getInstance().getDataFolder().toPath().resolve(TRACK_LIST_FILE_NAME);
            BufferedReader bufferedReader = Files.newBufferedReader(resolve);
            trackList = SoundOfNoteBlocksPlugin.GSON.fromJson(bufferedReader, new TypeToken<ArrayList<Track>>() {
            }.getType());
        } catch (IOException e) {
            SoundOfNoteBlocksPlugin.getInstance().getLogger().warning("Failed loading tracks from disk. (" + e.getClass().getName() + ": " + e.getMessage() + ")");
            trackList = new ArrayList<>();
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
