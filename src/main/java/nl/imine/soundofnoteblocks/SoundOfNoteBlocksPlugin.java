package nl.imine.soundofnoteblocks;

import java.io.File;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.Gson;

import nl.imine.api.holotag.ITag;
import nl.imine.api.util.ConfigUtil;
import nl.imine.api.util.serilize.GsonBukkitBuilder;
import nl.imine.api.util.serilize.InterfaceAdapter;
import nl.imine.soundofnoteblocks.controller.MusicPlayerListener;
import nl.imine.soundofnoteblocks.controller.MusicPlayerManager;
import nl.imine.soundofnoteblocks.controller.MusicboxCommandExecutor;
import nl.imine.soundofnoteblocks.controller.TrackManager;
import nl.imine.soundofnoteblocks.model.MusicPlayer;

public class SoundOfNoteBlocksPlugin extends JavaPlugin implements Listener {

	public static SoundOfNoteBlocksPlugin plugin;
	private static boolean ready;
	private static final Gson GSON = GsonBukkitBuilder.getBukkitBuilder()
			.registerTypeAdapter(MusicPlayer.class, new InterfaceAdapter<MusicPlayer>())
			.registerTypeAdapter(ITag.class, new InterfaceAdapter<ITag>()).create();

	private File tempFolder;

	// TODO: show tag by default & safe tag showing in file
	// FIXME: walkman is now broken

	@Override
	public void onEnable() {
		plugin = this;
		setupConfig();
		tempFolder = new File(getDataFolder().getAbsolutePath(), "tmp");
		tempFolder.mkdirs();
		TrackManager.getTracks();
		MusicPlayerManager.load();
		getCommand("jukebox").setExecutor(new MusicboxCommandExecutor());
		Bukkit.getPluginManager().registerEvents(new MusicPlayerListener(), this);
	}

	@Override
	public void onDisable() {
		plugin = null;
		setReady(false);
		for (File tempFile : tempFolder.listFiles()) {
			if (!tempFile.delete()) {
				tempFile.deleteOnExit();
			}
		}
		MusicPlayerManager.safe();
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
		SoundOfNoteBlocksPlugin.ready = ready;
		if (ready) {
			for (MusicPlayer mp : MusicPlayerManager.getMusicPlayer()) {
				if (mp.isRadioMode()) {
					mp.replayForce();
				}
			}
		} else {
			for (MusicPlayer mp : MusicPlayerManager.getMusicPlayer()) {
				mp.stopPlaying();
			}
		}
	}

	public static boolean isLoaded() {
		return SoundOfNoteBlocksPlugin.ready;
	}

	public File getTempFolder() {
		return tempFolder;
	}

	public static SoundOfNoteBlocksPlugin getInstance() {
		return plugin;
	}

	public static Gson getGson() {
		return GSON;
	}
}
