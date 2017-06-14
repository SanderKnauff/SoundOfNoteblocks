package nl.imine.soundofnoteblocks.repository;

import java.io.IOException;
import java.util.List;

public interface TrackRepository<Track> extends Repository {

	List<Track> findAll();
	Track findOne();
	List<Track> findAllByName();
	List<Track> findAllByArtist();
}
