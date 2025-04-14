package nl.imine.soundofnoteblocks;

import com.google.gson.GsonBuilder;
import nl.imine.api.gui.Container;
import nl.imine.api.gui.GuiManager;
import nl.imine.api.holotag.TagAPI;
import nl.imine.soundofnoteblocks.controller.MusicPlayerListener;
import nl.imine.soundofnoteblocks.controller.MusicPlayerManager;
import nl.imine.soundofnoteblocks.controller.MusicboxCommandExecutor;
import nl.imine.soundofnoteblocks.controller.TrackManager;
import nl.imine.soundofnoteblocks.model.Track;
import nl.imine.soundofnoteblocks.serialize.TrackTypeAdapter;
import nl.imine.soundofnoteblocks.view.MusicPlayerView;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.command.Commands;
import org.bukkit.plugin.java.annotation.dependency.Dependency;
import org.bukkit.plugin.java.annotation.dependency.DependsOn;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Plugin;

import javax.annotation.Nullable;

import static java.util.Objects.requireNonNull;

@Plugin(name = "SoundOfNoteblocks", version = "1.1.0-SNAPSHOT")
@DependsOn(@Dependency("NoteBlockAPI"))
@ApiVersion("1.21.5")
@Commands(
        @Command(
                name = "jukebox",
                desc = "Main command for musicboxes",
                usage = "/jukebox reload to reload the track list",
                aliases = {"musicbox", "tracklist"}
        )
)
public class SoundOfNoteBlocksPlugin extends JavaPlugin implements Listener {
    private @Nullable GuiManager guiManager;

    @Override
    public void onEnable() {
        guiManager = new GuiManager();
        guiManager.init(this);

        final var tagApi = new TagAPI();
        tagApi.init(this);
        final var gson = new GsonBuilder().registerTypeAdapter(Track.class, new TrackTypeAdapter(this)).create();
        final var trackManager = new TrackManager(this, gson);
        final var musicPlayerManager = new MusicPlayerManager(tagApi, trackManager);
        trackManager.reloadTracks();

        requireNonNull(getCommand("jukebox")).setExecutor(new MusicboxCommandExecutor(trackManager));
        Bukkit.getPluginManager().registerEvents(new MusicPlayerListener(this, musicPlayerManager, new MusicPlayerView(guiManager), trackManager), this);
    }

    @Override
    public void onDisable() {
        if (guiManager != null) {
            guiManager.getContainers().forEach(Container::close);
        }
    }
}
