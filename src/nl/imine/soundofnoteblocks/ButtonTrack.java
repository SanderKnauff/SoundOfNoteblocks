package nl.imine.soundofnoteblocks;

import com.xxmicloxx.NoteBlockAPI.NBSDecoder;
import com.xxmicloxx.NoteBlockAPI.PositionSongPlayer;
import com.xxmicloxx.NoteBlockAPI.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.Song;
import com.xxmicloxx.NoteBlockAPI.SongPlayer;
import java.util.ArrayList;
import nl.imine.gui.Button;
import nl.imine.gui.Container;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 *
 * @author Sander
 */
public class ButtonTrack extends Button {

    private MusicBox jukebox;
    private Track track;

    public ButtonTrack(Container container, Material material, String name, int slot, MusicBox jukebox, Track track) {
        super(container, material, name, slot);
        this.track = track;
        this.jukebox = jukebox;
    }

    public ButtonTrack(Container container, Material material, String name, int slot, ArrayList<String> subtext, MusicBox jukebox, Track track) {
        super(container, material, name, slot, subtext);
        this.track = track;
        this.jukebox = jukebox;
    }

    public ButtonTrack(Container container, Material material, String name, int slot, String subtext, MusicBox jukebox, Track track) {
        super(container, material, name, slot, subtext);
        this.track = track;
        this.jukebox = jukebox;
    }

    public Track getTrack() {
        return track;
    }

    @Override
    public void doAction(Player player) {
        jukebox.playTrack(track);
    }
}
