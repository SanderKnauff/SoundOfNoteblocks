package nl.imine.soundofnoteblocks.repository.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import nl.imine.soundofnoteblocks.model.Track;
import nl.imine.soundofnoteblocks.repository.TrackRepository;

public class TrackRepositoryImpl implements TrackRepository{

	private URI baseUri;

	public TrackRepositoryImpl(URI uri) {
		this.baseUri = uri;
	}

	@Override
	public List findAll() {
		List<Track> ret = new ArrayList<>();

		JsonArray jsonArray = null;
		try {
			jsonArray = new Gson().fromJson(Files.newBufferedReader(Paths.get(baseUri)), JsonArray.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		jsonArray.forEach(jsonElement -> {
			JsonObject object = jsonElement.getAsJsonObject();
		});

		return null;
	}

	@Override
	public Object findOne() {
		return null;
	}

	@Override
	public List findAllByName() {
		return null;
	}

	@Override
	public List findAllByArtist() {
		return null;
	}

	public URL getUrl() {
		try {
			return baseUri.toURL();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
