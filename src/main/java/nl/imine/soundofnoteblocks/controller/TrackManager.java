package nl.imine.soundofnoteblocks.controller;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import com.google.gson.Gson;

import nl.imine.api.file.FileFilter;
import nl.imine.api.util.FileUtil;
import nl.imine.api.util.WebUtil;
import nl.imine.soundofnoteblocks.SoundOfNoteBlocksPlugin;
import nl.imine.soundofnoteblocks.model.Track;

public class TrackManager {

	private static List<Track> trackList = new ArrayList<>();

	static {
		loadTracks();
	}

	private TrackManager() {
	}

	public static List<Track> getTrackList() {
		return trackList;
	}

	public static Track[] getTrackArray() {
		return trackList.toArray(new Track[trackList.size()]);
	}

	private static void loadTracks() {
		Bukkit.getScheduler().runTaskAsynchronously(SoundOfNoteBlocksPlugin.plugin, () -> {
			try {
				Gson gson = new Gson();
				Plugin pl = SoundOfNoteBlocksPlugin.getInstance();
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
			Bukkit.getScheduler().scheduleSyncDelayedTask(SoundOfNoteBlocksPlugin.plugin,
				() -> SoundOfNoteBlocksPlugin.setReady(true));

		});
	}

	public static void setTracks(List<Track> trackList) {
		TrackManager.trackList = trackList;
	}

	public static List<Track> getTracks() {
		return trackList;
	}

	public static void reloadTracks() {
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
		File[] tempFolder = SoundOfNoteBlocksPlugin.getInstance().getTempFolder().listFiles(new FileFilter(".nbs"));
		for (File tempFile : tempFolder) {
			if (tempFile.getName().startsWith(track.getId().toString())) {
				ret = tempFile;
				break;
			}
		}
		if (ret != null) {
			return ret;
		}
		ret = new File(String.format("%s%s%s.nbs",
			SoundOfNoteBlocksPlugin.getInstance().getTempFolder().getAbsolutePath(), File.separator, track.getId()));
		try {
			FileUtil.copyURLtoFile(new URL(track.getUrl() + track.getId() + ".nbs"), ret);
		} catch (Exception ex) {
			System.err.println(ex);
		}
		return ret;
	}
}