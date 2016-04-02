package nl.imine.soundofnoteblocks;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import com.google.gson.Gson;

import nl.imine.api.file.FileFilter;
import nl.imine.api.util.WebUtil;

public class TrackManager {

	private List<Track> trackList = new ArrayList<>();

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

	public static File getFile(Track track) {
		File ret = null;
		File[] tempFolder = SoundOfNoteBlocks.getInstance().getTempFolder().listFiles(new FileFilter(".nbs"));
		for (File tempFile : tempFolder) {
			if (tempFile.getName().startsWith(track.getId())) {
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
			FileUtils.copyURLToFile(new URL(track.getUrl() + track.getId() + ".nbs"), ret);
		} catch (Exception ex) {
			ex.printStackTrace();
			ret = null;
		}
		return ret;
	}
}