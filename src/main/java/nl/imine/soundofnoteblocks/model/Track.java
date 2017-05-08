package nl.imine.soundofnoteblocks.model;

import com.xxmicloxx.NoteBlockAPI.NBSDecoder;
import com.xxmicloxx.NoteBlockAPI.Song;

import nl.imine.soundofnoteblocks.controller.TrackManager;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.UUID;

public class Track implements Serializable {

	private static final long serialVersionUID = -6901663374788956944L;

	private UUID id;
	private String name;
	private String artist;
	private String url;
	private transient Song song;

	public Track() {
	}

	public Track(String id, String name, String artist, String url) {
		this.id = UUID.fromString(id);
		this.name = name;
		this.artist = artist;
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public UUID getId() {
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
			song = NBSDecoder.parse(getPath().toFile());
		}
		return song;
	}

	public Path getPath() {
		return TrackManager.getPath(this);
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
