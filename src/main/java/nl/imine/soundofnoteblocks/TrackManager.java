package nl.imine.soundofnoteblocks;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;

import nl.imine.api.file.FileFilter;
import nl.imine.api.util.WebUtil;

public class TrackManager {

    private List<Track> trackList = new ArrayList<>();

    public TrackManager() {
        loadTracks();
    }

    private void loadTracks() {
        try {
            Gson gson = new Gson();
            Track[] tracks = gson.fromJson(WebUtil.getResponse("http://files.imine.nl/iMineNetwork/NBS/trackList.json"), Track[].class);
            trackList = Arrays.asList(tracks);
        } catch (Exception ex) {
            ex.printStackTrace();
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

    public static File getTrack(String id) {
        File ret = null;
        File[] tempFolder = SoundOfNoteBlocks.getInstance().getTempFolder().listFiles(new FileFilter(".nbs"));
        for (File tempFile : tempFolder) {
            if (tempFile.getName().startsWith(id)) {
                ret = tempFile;
                break;
            }
        }
        if (ret != null) {
            return ret;
        }
        ret = new File(String.format("%s%s%s.nbs", SoundOfNoteBlocks.getInstance().getTempFolder().getAbsolutePath(), File.separator, id));
        try {
            FileUtils.copyURLToFile(new URL("http://files.imine.nl/iMineNetwork/NBS/" + id + ".nbs"), ret);
        } catch (Exception ex) {
            ex.printStackTrace();
            ret = null;
        }
        return ret;
    }
}
