package nl.imine.soundofnoteblocks;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Sander
 */
public class TrackManager {

    public static final String TRACK_REPO_DIR = "/home/webserver/files/iMineNetwork/NBS/".replaceAll("///", File.separator);
    public static final String NBS_EXSTENTION = ".nbs";
    public static final String JSON_EXSTENTION = ".json";

    private List<Track> trackList = new ArrayList<>();

    public TrackManager() {
        trackList = loadTracks();
    }

    private List<Track> loadTracks() {
        List<Track> ret = new ArrayList<>();
        try {
            Gson gson = new Gson();
            Track[] tracks = gson.fromJson(new FileReader(TRACK_REPO_DIR + "trackList" + JSON_EXSTENTION), Track[].class);
            ret = Arrays.asList(tracks);
        } catch (FileNotFoundException fnfe) {
            System.out.println("FileNotFoundException: " + fnfe.getMessage());
        }
        return ret;
    }
    
    public void setTracks(ArrayList<Track> trackList){
        this.trackList = trackList;
    }
    
    public List<Track> getTracks(){
        return trackList;
    }
    
    public void reloadTracks(){
        trackList = loadTracks();
    }
}
