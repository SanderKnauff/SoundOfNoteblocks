package nl.imine.soundofnoteblocks;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import nl.imine.gui.Container;
import nl.imine.gui.GuiManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;

/**
 *
 * @author Sander
 */
public class SoundOfNoteBlocks extends JavaPlugin implements Listener {

    public static Plugin plugin;
    private static TrackManager trackManager = new TrackManager();

    @Override
    public void onEnable() {
        plugin = this;
        GuiManager.init(plugin);
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
        for(Musicbox m : Musicbox.getMusicBoxes()){
            m.stopPlaying();
        }
        for(Container c : GuiManager.getInstance().getContainers()){
            c.close();
        }
    }

    public static Plugin getInstance() {
        return plugin;
    }

    public static TrackManager getTrackManager() {
        return trackManager;
    }
}
