package nl.imine.soundofnoteblocks;

import java.util.ArrayList;
import nl.imine.api.gui.Button;
import nl.imine.api.gui.Container;

import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 *
 * @author Sander
 */
public class ButtonTrack extends Button {

    private Musicbox jukebox;
    private Track track;

    public ButtonTrack(Container container, Material material, String name, int slot, Musicbox jukebox, Track track) {

        super(container, material, name, slot);
        this.track = track;
        this.jukebox = jukebox;
    }

    public ButtonTrack(Container container, Material material, String name, int slot, ArrayList<String> subtext, Musicbox jukebox, Track track) {
        super(container, material, name, slot, subtext);
        this.track = track;
        this.jukebox = jukebox;
    }

    public ButtonTrack(Container container, Material material, String name, int slot, String subtext, Musicbox jukebox, Track track) {
        super(container, material, name, slot, subtext);
        this.track = track;
        this.jukebox = jukebox;
    }

    public Track getTrack() {
        return track;
    }

    @Override
    public void doAction(Player player) {
        if (jukebox != null) {
            if (jukebox.getLocation().getBlock().getType().equals(Material.JUKEBOX)) {
                jukebox.playTrack(track);
            }
        }
        player.closeInventory();
    }
}
