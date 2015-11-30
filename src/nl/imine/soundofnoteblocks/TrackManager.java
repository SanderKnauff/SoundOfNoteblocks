package nl.imine.soundofnoteblocks;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Sander
 */
public class TrackManager {

    public static final String TRACK_REPO_DIR = "/home/webserver/files/iMineNetwork/NBS/".replaceAll("///", File.separator);
    public static final String NBS_EXSTENTION = ".nbs";
    public static final String JSON_EXSTENTION = ".json";

    private ArrayList<Track> trackList = new ArrayList<>();

    public TrackManager() {
        trackList = loadTracks();
    }

    private ArrayList<Track> loadTracks() {
        ArrayList<Track> ret = new ArrayList<>();
        try {
            Gson gson = new Gson();
            Track[] tracks = gson.fromJson(new FileReader(TRACK_REPO_DIR + "trackList" + JSON_EXSTENTION), Track[].class);
            ret = new ArrayList<>(Arrays.asList(tracks));
        } catch (FileNotFoundException fnfe) {
            System.out.println("FileNotFoundException: " + fnfe.getMessage());
        }
        return ret;
    }
    
    public void setTracks(ArrayList<Track> trackList){
        this.trackList = trackList;
    }
    
    public ArrayList<Track> getTracks(){
        return trackList;
    }
    
    public void reloadTracks(){
        trackList = loadTracks();
    }
}
