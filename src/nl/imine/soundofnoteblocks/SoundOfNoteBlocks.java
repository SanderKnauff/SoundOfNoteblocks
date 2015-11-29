package nl.imine.soundofnoteblocks;

import nl.imine.gui.Button;
import nl.imine.gui.Container;
import nl.imine.gui.GuiManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
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
                if (evt.getPlayer().hasPermission("imine.uhc.vip")) {
                    if (evt.getItem() == null) {
                        Jukebox jukebox = Jukebox.findJukebox(evt.getClickedBlock().getLocation());
                        Container c = GuiManager.getInstance().createContainer("Choose Track", 45);
                        for (Track track : tm.getTracks()) {
                            c.addButton(new ButtonTrack(c, Material.GOLD_RECORD, track.getName(), c.getButtons().size(), track.getArtist(), jukebox, track));
                        }
                        c.open(evt.getPlayer());
                        evt.setCancelled(true);

                    } else if (!(evt.getItem().getType().name().toLowerCase().contains("record"))) {
                        Jukebox jukebox = Jukebox.findJukebox(evt.getClickedBlock().getLocation());
                        Container c = GuiManager.getInstance().createContainer("Choose Track", 45);
                        for (Track track : tm.getTracks()) {
                            c.addButton(new ButtonTrack(c, Material.GOLD_RECORD, track.getName(), c.getButtons().size(), track.getArtist(), jukebox, track));
                        }
                        c.open(evt.getPlayer());
                        evt.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerBlockBreak(BlockBreakEvent evt) {
        if (evt.getBlock().getType().equals(Material.JUKEBOX)) {
            Jukebox.findJukebox(evt.getBlock().getLocation()).stopPlaying();
        }
    }
}
