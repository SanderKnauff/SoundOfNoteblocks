package nl.imine.soundofnoteblocks;

import nl.imine.api.gui.Button;
import nl.imine.api.gui.Container;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Sander
 */
public class ButtonTrack extends Button {

    private final Musicbox jukebox;
    private final Track track;
    
    public ButtonTrack(Container container, ItemStack itemStack, int slot, Musicbox jukebox, Track track) {
        super(container, itemStack, slot);
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
