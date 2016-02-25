package nl.imine.soundofnoteblocks;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import nl.imine.api.gui.Container;
import nl.imine.api.gui.GuiManager;

public class SoundOfNoteBlocks extends JavaPlugin implements Listener {

    public static SoundOfNoteBlocks plugin;
    private static TrackManager trackManager = new TrackManager();

    private File temp;

    @Override
    public void onEnable() {
        plugin = this;
        MusicboxListener.init();
        getDataFolder().mkdirs();
        temp = new File(getDataFolder().getAbsolutePath() + File.separator + "tmp");
        temp.mkdirs();
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
    }

    public File getTempFolder() {
        return temp;
    }

    public static SoundOfNoteBlocks getInstance() {
        return plugin;
    }

    public static TrackManager getTrackManager() {
        return trackManager;
    }
}
