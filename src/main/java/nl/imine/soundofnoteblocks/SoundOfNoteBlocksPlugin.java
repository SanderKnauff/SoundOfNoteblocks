package nl.imine.soundofnoteblocks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nl.imine.api.gui.Container;
import nl.imine.api.gui.GuiManager;
import nl.imine.api.holotag.TagAPI;
import nl.imine.soundofnoteblocks.controller.MusicPlayerListener;
import nl.imine.soundofnoteblocks.controller.MusicboxCommandExecutor;
import nl.imine.soundofnoteblocks.controller.TrackManager;
import nl.imine.soundofnoteblocks.model.Track;
import nl.imine.soundofnoteblocks.serialize.TrackTypeAdapter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class SoundOfNoteBlocksPlugin extends JavaPlugin implements Listener {

    private static SoundOfNoteBlocksPlugin plugin;
    public static final Gson GSON = new GsonBuilder().registerTypeAdapter(Track.class, new TrackTypeAdapter()).create();

    @Override
    public void onEnable() {
        plugin = this;
        GuiManager.init(this);
        TagAPI.init();
        TrackManager.reloadTracks();
        getCommand("jukebox").setExecutor(new MusicboxCommandExecutor());
        Bukkit.getPluginManager().registerEvents(new MusicPlayerListener(), this);
    }

    @Override
    public void onDisable() {
        plugin = null;
        for (Container container : GuiManager.getInstance().getContainers()) {
            container.close();
        }
    }

    public static SoundOfNoteBlocksPlugin getInstance() {
        return plugin;
    }
}
