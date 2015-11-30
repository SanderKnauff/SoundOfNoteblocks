package nl.imine.soundofnoteblocks;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import nl.imine.gui.GuiManager;

/**
 *
 * @author Sander
 */
public class SoundOfNoteBlocks extends JavaPlugin implements Listener {

    private static Plugin plugin;
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
