package nl.imine.soundofnoteblocks;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import nl.imine.api.gui.Container;
import nl.imine.api.gui.GuiManager;
import nl.imine.api.util.MktUtil;

public class SoundOfNoteBlocks extends JavaPlugin implements Listener {

    public static SoundOfNoteBlocks plugin;
    private TrackManager trackManager;

    private File tempFolder;

    @Override
    public void onEnable() {
        plugin = this;
        setupConfig();
        tempFolder = new File(getDataFolder().getAbsolutePath() + File.separator + "tmp");
        tempFolder.mkdirs();
        trackManager = new TrackManager();
        MusicboxListener.init();
        getCommand("jukebox").setExecutor(new MusicboxCommandExecutor());
        for (World w : Bukkit.getWorlds()) {
            for (ArmorStand as : w.getEntitiesByClass(ArmorStand.class)) {
                if (!as.hasBasePlate()) {
                    as.remove();
                }
            }
        }
    }

    @Override
    public void onDisable() {
        trackManager = null;
        plugin = null;
        for (World w : Bukkit.getWorlds()) {
            for (ArmorStand as : w.getEntitiesByClass(ArmorStand.class)) {
                if (!as.hasBasePlate()) {
                    as.remove();
                }
            }
        }
        for (Musicbox m : Musicbox.getMusicBoxes()) {
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
            MktUtil.configSetIfNotSet(this, "repositories", MktUtil.toList(new String[] { "http://files.imine.nl/iMineNetwork/NBS/trackList.json" }));
            saveConfig();
        }
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
