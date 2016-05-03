package nl.imine.soundofnoteblocks;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import com.google.gson.Gson;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Optional;
import java.util.UUID;

import nl.imine.api.file.FileFilter;
import nl.imine.api.util.FileUtil;
import nl.imine.api.util.WebUtil;

public class TrackManager {

	private static List<Track> trackList = new ArrayList<>();

	public TrackManager() {
		loadTracks();
	}

	private void loadTracks() {
		Bukkit.getScheduler().runTaskAsynchronously(SoundOfNoteBlocks.plugin, () -> {
			try {
				Gson gson = new Gson();
				Plugin pl = SoundOfNoteBlocks.getInstance();
				FileConfiguration config = pl.getConfig();
				trackList.clear();
				for (Object url : config.getList("repositories")) {
					try {
						if (url instanceof String) {
							Track[] tracks = gson.fromJson(WebUtil.getResponse((String) url), Track[].class);
							for (Track track : tracks) {
								// Place where music should be
								track.setUrlIfNotSet(((String) url).replaceAll("\\/\\w{0,}\\.{0,}\\w{0,}$", "/"));
								track.getSong();
							}
							trackList.addAll(Arrays.asList(tracks));
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			MusicboxManager.loadMusicboxesFromConfig();
			SoundOfNoteBlocks.setReady(true);
		});
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

	public static Track getTrack(UUID trackId) {
		Optional<Track> oTrack = trackList.stream().filter(t -> t.getId().equals(trackId)).findFirst();
		if (oTrack.isPresent()) {
			return oTrack.get();
		}
		return null;
	}

	public static File getFile(Track track) {
		File ret = null;
		File[] tempFolder = SoundOfNoteBlocks.getInstance().getTempFolder().listFiles(new FileFilter(".nbs"));
		for (File tempFile : tempFolder) {
			if (tempFile.getName().startsWith(track.getId().toString())) {
				ret = tempFile;
				break;
			}
		}
		if (ret != null) {
			return ret;
		}
		ret = new File(String.format("%s%s%s.nbs", SoundOfNoteBlocks.getInstance().getTempFolder().getAbsolutePath(),
			File.separator, track.getId()));
		try {
			FileUtil.copyURLtoFile(new URL(track.getUrl() + track.getId() + ".nbs"), ret);
		} catch (MalformedURLException mue) {
			System.err.println("MalformedURLException: " + mue.getMessage());
		}
		return ret;
	}
}