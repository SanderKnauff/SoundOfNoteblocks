package nl.imine.soundofnoteblocks;

import com.xxmicloxx.NoteBlockAPI.NBSDecoder;
import com.xxmicloxx.NoteBlockAPI.Song;
import java.io.File;
import java.io.Serializable;

public class Track implements Serializable {

	private static final long serialVersionUID = -6901663374788956944L;

	private String id;
	private String name;
	private String artist;
	private String url;
	private transient Song song;

	public Track() {
	}

	public Track(String id, String name, String artist, String url) {
		this.id = id;
		this.name = name;
		this.artist = artist;
		this.url = url;
	}

	public String getUrl() {
		return url;
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

	public Song getSong() {
		if (song == null) {
			song = NBSDecoder.parse(getFile());
		}
		return song;
	}

	public File getFile() {
		return TrackManager.getFile(this);
	}

	public void setUrlIfNotSet(String url) {
		if (this.url == null || this.url.isEmpty() || this.url.equalsIgnoreCase("null")) {
			setUrl(url);
		}
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "Track{id:" + this.getId() + ",name:" + this.getName() + ",artist:" + this.getArtist() + "}";
	}
}
