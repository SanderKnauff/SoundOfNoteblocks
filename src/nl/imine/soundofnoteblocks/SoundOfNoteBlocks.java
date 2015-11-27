package nl.imine.soundofnoteblocks;

import nl.imine.gui.Button;
import nl.imine.gui.Container;
import nl.imine.gui.GuiManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Sander
 */
public class SoundOfNoteBlocks extends JavaPlugin implements Listener {

    public static Plugin plugin;
    TrackManager tm = new TrackManager();

    @Override
    public void onEnable() {
        plugin = this;
        GuiManager.init(plugin);
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        plugin = null;
    }

    public static Plugin getInstance() {
        return plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent evt) {
        if (evt.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (evt.getClickedBlock().getType().equals(Material.JUKEBOX)) {
                Container c = GuiManager.getInstance().createContainer("Choose Track", 45);
                for (Track t : tm.getTracks()) {
                    c.addButton(new Button(c, Material.GOLD_RECORD, t.getName(), c.getFreeSlot(), t.getArtist()));
                }
                c.open(evt.getPlayer());
            }
        }
    }
}
