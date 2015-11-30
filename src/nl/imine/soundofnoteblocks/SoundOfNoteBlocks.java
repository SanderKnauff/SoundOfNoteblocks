package nl.imine.soundofnoteblocks;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import nl.imine.gui.GuiManager;

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
    }

    @Override
    public void onDisable() {
        trackManager = null;
        plugin = null;
    }

    public static Plugin getInstance() {
        return plugin;
    }
    
    public static TrackManager getTrackManager(){
        return trackManager;
    }
}
