package nl.imine.soundofnoteblocks.model;

import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.UUID;

public class Track implements Serializable {

	private static final long serialVersionUID = -6901663374788956944L;

	private final UUID id;
	private final String name;
	private final String artist;
	private final Path path;
	private transient Song song;

	public Track(String id, String name, String artist, Path path) {
		this.id = UUID.fromString(id);
		this.name = name;
		this.artist = artist;
		this.path = path;
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
		return path;
	}
}
