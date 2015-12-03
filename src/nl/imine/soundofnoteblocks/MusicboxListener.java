package nl.imine.soundofnoteblocks;

import nl.imine.gui.Container;
import nl.imine.gui.GuiManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class MusicboxListener implements Listener {

    private static final Material[] RECORDS = new Material[]{Material.RECORD_10, Material.RECORD_12,
        Material.RECORD_3, Material.RECORD_4, Material.RECORD_5, Material.RECORD_6,
        Material.RECORD_7, Material.RECORD_8, Material.RECORD_9, Material.GOLD_RECORD, Material.GREEN_RECORD};

    public static void init() {
        Bukkit.getPluginManager().registerEvents(new MusicboxListener(), SoundOfNoteBlocks.getInstance());
    }

    private MusicboxListener() {

    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent evt) {
        if (evt.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (evt.getClickedBlock().getType().equals(Material.JUKEBOX)) {
                if (!(((org.bukkit.block.Jukebox) evt.getClickedBlock().getState()).isPlaying())) {
                    if (evt.getPlayer().hasPermission("imine.uhc.vip")) {
                        System.out.println(evt.getItem());
                        //if (evt.getItem() == null && !(evt.getItem().getType().name().toLowerCase().contains("record"))) {
                            Musicbox jukebox = Musicbox.findJukebox(evt.getClickedBlock().getLocation());
                            Container c = GuiManager.getInstance().createContainer("Choose Track", 45);
                            for (Track track : SoundOfNoteBlocks.getTrackManager().getTracks()) {
                                c.addButton(new ButtonTrack(c, RECORDS[track.getName().length() % RECORDS.length], track.getName(), c.getButtons().size(), track.getArtist(), jukebox, track));
                            }
                            c.open(evt.getPlayer());
                            evt.setCancelled(true);
                        //}
                    }
                }
            }
        }

    }

    @EventHandler
    public void onPlayerBlockBreak(BlockBreakEvent evt) {
        if (evt.getBlock().getType().equals(Material.JUKEBOX)) {
            Musicbox.findJukebox(evt.getBlock().getLocation()).stopPlaying();
        }
    }
}