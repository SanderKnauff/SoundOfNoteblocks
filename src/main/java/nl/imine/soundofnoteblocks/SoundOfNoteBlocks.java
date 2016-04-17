package nl.imine.soundofnoteblocks;

import java.io.File;
import java.util.Arrays;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import nl.imine.api.gui.Container;
import nl.imine.api.gui.GuiManager;
import nl.imine.api.util.ConfigUtil;

public class SoundOfNoteBlocks extends JavaPlugin implements Listener {

	public static SoundOfNoteBlocks plugin;

	private static boolean ready;
	private TrackManager trackManager;

	private File tempFolder;

	@Override
	public void onEnable() {
		plugin = this;
		setupConfig();
		tempFolder = new File(getDataFolder().getAbsolutePath(), "tmp");
		tempFolder.mkdirs();
		trackManager = new TrackManager();
		MusicboxListener.init();
		getCommand("jukebox").setExecutor(new MusicboxCommandExecutor());
	}

	@Override
	public void onDisable() {
		trackManager = null;
		plugin = null;
		for (Musicbox m : MusicboxManager.getMusicboxes()) {
			m.stopPlaying();
		}
		for (Container c : GuiManager.getInstance().getContainers()) {
			c.close();
		}
		for (File tempFile : tempFolder.listFiles()) {
			if (!tempFile.delete()) {
				tempFile.deleteOnExit();
			}
		}
	}

	private void setupConfig() {
		if (getConfig().getDouble("version", 0) <= 0.0D) {
			getConfig().set("version", 0.1D);
			ConfigUtil.configSetIfNotSet(this, "repositories",
				Arrays.asList(new String[]{"http://files.imine.nl/iMineNetwork/NBS/trackList.json"}));
			saveConfig();
		}
	}

	public static void setReady(boolean ready) {
		SoundOfNoteBlocks.ready = ready;
	}

	public static boolean isReady() {
		return SoundOfNoteBlocks.ready;
	}

	public File getTempFolder() {
		return tempFolder;
	}

	public static SoundOfNoteBlocks getInstance() {
		return plugin;
	}

	public TrackManager getTrackManager() {
		return trackManager;
	}
}
