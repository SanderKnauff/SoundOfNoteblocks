package nl.imine.soundofnoteblocks;

import java.io.File;
import java.io.Serializable;

/**
 *
 * @author Sander
 */
public class Track implements Serializable {
    private static final long serialVersionUID = -6901663374788956944L;

    private String id;
    private String name;
    private String artist;

    public Track() {
    }

    public Track(String id, String name, String artist) {
        this.id = id;
        this.name = name;
        this.artist = artist;
    }

    public String getId() {
        return id;
    }

    public String getArtist() {
        return artist;
    }

    public String getName() {
        return name;
    }

    public File getFile() {
        return TrackManager.getTrack(id);
    }

    @Override
    public String toString() {
        return "Track{id:" + this.getId() + ",name:" + this.getName() + ",artist:" + this.getArtist() + "}";
    }
}
