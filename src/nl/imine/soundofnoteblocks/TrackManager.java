package nl.imine.soundofnoteblocks;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

/**
 *
 * @author Sander
 */
public class TrackManager {

    public static final String TRACK_REPO_DIR = "/home/webserver/files/iMineNetwork/NBS/";
    public static final String NBS_EXSTENTION = ".nbs";
    public static final String JSON_EXSTENTION = ".json";

    private List<Track> trackList = new ArrayList<>();

    public TrackManager() {
        loadTracks();
    }

    private void loadTracks() {
        try {
            Gson gson = new Gson();
            Track[] tracks = gson.fromJson(new FileReader(TRACK_REPO_DIR + "trackList" + JSON_EXSTENTION),
                    Track[].class);
            trackList = Arrays.asList(tracks);
        } catch (FileNotFoundException fnfe) {
            System.out.println("FileNotFoundException: " + fnfe.getMessage());
        }
    }

    public void setTracks(List<Track> trackList) {
        this.trackList = trackList;
    }

    public List<Track> getTracks() {
        return trackList;
    }

    public void reloadTracks() {
        loadTracks();
    }
}
