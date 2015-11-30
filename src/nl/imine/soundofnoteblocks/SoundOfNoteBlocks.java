package nl.imine.soundofnoteblocks;

import nl.imine.gui.Container;
import nl.imine.gui.GuiManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Jukebox;
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

    private static final Material[] RECORDS = new Material[]{Material.RECORD_10, Material.RECORD_12, 
        Material.RECORD_3, Material.RECORD_4, Material.RECORD_5, Material.RECORD_6,
        Material.RECORD_7, Material.RECORD_8, Material.RECORD_9, Material.GOLD_RECORD, Material.GREEN_RECORD};

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
                if (!(((Jukebox) evt.getClickedBlock().getState()).isPlaying())) {
                    if (evt.getPlayer().hasPermission("imine.uhc.vip")) {
                        if (evt.getItem() == null) {
                            MusicBox jukebox = MusicBox.findJukebox(evt.getClickedBlock().getLocation());
                            Container c = GuiManager.getInstance().createContainer("Choose Track", 45);
                            for (Track track : tm.getTracks()) {
                                c.addButton(new ButtonTrack(c, RECORDS[track.getName().length() % RECORDS.length], track.getName(), c.getButtons().size(), track.getArtist(), jukebox, track));
                            }
                            c.open(evt.getPlayer());
                            evt.setCancelled(true);

                        } else if (!(evt.getItem().getType().name().toLowerCase().contains("record"))) {
                            MusicBox jukebox = MusicBox.findJukebox(evt.getClickedBlock().getLocation());
                            Container c = GuiManager.getInstance().createContainer("Choose Track", 45);
                            for (Track track : tm.getTracks()) {
                                c.addButton(new ButtonTrack(c, RECORDS[track.getName().length() % RECORDS.length], track.getName(), c.getButtons().size(), track.getArtist(), jukebox, track));
                            }
                            c.open(evt.getPlayer());
                            evt.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerBlockBreak(BlockBreakEvent evt) {
        if (evt.getBlock().getType().equals(Material.JUKEBOX)) {
            MusicBox.findJukebox(evt.getBlock().getLocation()).stopPlaying();
        }
    }
}
