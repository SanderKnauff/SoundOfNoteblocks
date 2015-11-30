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

    public static void init() {
        Bukkit.getPluginManager().registerEvents(new MusicboxListener(), SoundOfNoteBlocks.getInstance());
    }

    private MusicboxListener() {

    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent evt) {
        if (evt.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (evt.getClickedBlock().getType().equals(Material.JUKEBOX)) {
                if (evt.getPlayer().hasPermission("imine.uhc.vip")) {
                    if (evt.getItem() == null) {
                        Jukebox jukebox = Jukebox.findJukebox(evt.getClickedBlock().getLocation());
                        Container c = GuiManager.getInstance().createContainer("Choose Track", 45);
                        for (Track track : SoundOfNoteBlocks.getTrackManager().getTracks()) {
                            c.addButton(new ButtonTrack(c, Material.GOLD_RECORD, track.getName(), c.getButtons().size(),
                                    track.getArtist(), jukebox, track));
                        }
                        c.open(evt.getPlayer());
                        evt.setCancelled(true);

                    } else if (!(evt.getItem().getType().name().toLowerCase().contains("record"))) {
                        Jukebox jukebox = Jukebox.findJukebox(evt.getClickedBlock().getLocation());
                        Container c = GuiManager.getInstance().createContainer("Choose Track", 45);
                        for (Track track : SoundOfNoteBlocks.getTrackManager().getTracks()) {
                            c.addButton(new ButtonTrack(c, Material.GOLD_RECORD, track.getName(), c.getButtons().size(),
                                    track.getArtist(), jukebox, track));
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
