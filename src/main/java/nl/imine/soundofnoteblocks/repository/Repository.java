package nl.imine.soundofnoteblocks.repository;

import java.net.URL;
import java.util.List;

public interface Repository<T> {

	List<T> findAll();
	T findOne();

	URL getUrl();


}
