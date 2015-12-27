package nl.imine.soundofnoteblocks;

import nl.imine.api.gui.Container;
import nl.imine.api.gui.GuiManager;
import nl.imine.api.util.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

public class MusicboxListener implements Listener {

    private static final Material[] RECORDS = new Material[]{Material.RECORD_10, Material.RECORD_12,
        Material.RECORD_3, Material.RECORD_4, Material.RECORD_5, Material.RECORD_6, Material.RECORD_7,
        Material.RECORD_8, Material.RECORD_9, Material.GOLD_RECORD, Material.GREEN_RECORD};

    public static void init() {
        Bukkit.getPluginManager().registerEvents(new MusicboxListener(), SoundOfNoteBlocks.getInstance());
    }

    private MusicboxListener() {

    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent evt) {
        for (Entity e : evt.getChunk().getEntities()) {
            if (e instanceof ArmorStand) {
                ArmorStand as = (ArmorStand) e;
                if (!as.hasBasePlate()) {
                    as.remove();
                }
            }
        }
    }

    @EventHandler
    public void onRedstoneEvent(BlockRedstoneEvent evt) {
        if (evt.getNewCurrent() > 2 && evt.getNewCurrent() > evt.getOldCurrent()) {
            for (int i = -1; i < 2; i++) {
                checkRedstoneRenew(evt.getBlock().getLocation().add(i, 0, 0).getBlock());
                checkRedstoneRenew(evt.getBlock().getLocation().add(0, 0, i).getBlock());
            }
        }
    }

    private void checkRedstoneRenew(Block bl) {
        if (bl.getType() == Material.JUKEBOX) {
            Musicbox mb = Musicbox.findJukebox(bl.getLocation());
            mb.replayLastSong(false);
        }
    }

    @EventHandler
    public void onChunkLoad(ChunkUnloadEvent evt) {
        for (Entity e : evt.getChunk().getEntities()) {
            if (e instanceof ArmorStand) {
                ArmorStand as = (ArmorStand) e;
                if (!as.hasBasePlate()) {
                    as.remove();
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent evt) {
        if (evt.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (evt.getClickedBlock().getType().equals(Material.JUKEBOX) && !evt.getPlayer().isSneaking()) {
                if (!(((org.bukkit.block.Jukebox) evt.getClickedBlock().getState()).isPlaying())) {
                    if (evt.getPlayer().hasPermission("iMine.jukebox.play")) {
                        if (evt.getItem() == null || !evt.getItem().getType().name().toLowerCase().contains("record")) {
                            Musicbox jukebox = Musicbox.findJukebox(evt.getClickedBlock().getLocation());
                            if (jukebox.isLocked() && !evt.getPlayer().hasPermission("iMine.jukebox.lockbypass")) {
                                evt.getPlayer().playSound(evt.getPlayer().getLocation(), Sound.VILLAGER_NO, 1F, 1F);
                                evt.setCancelled(true);
                                return;
                            }
                            Container c = GuiManager.getInstance().createContainer("Choose Track", 45, true, false);
                            c.addStaticButton(Container.getDefaultPreviousButton(c).setSlot(0));
                            c.addStaticButton(jukebox.createReplayButton(c, 2));
                            c.addStaticButton(jukebox.createStopButton(c, 3));
                            c.addStaticButton(jukebox.createRandomButton(c, 4));
                            c.addStaticButton(jukebox.createTogglenametagButton(c, 5));
                            c.addStaticButton(jukebox.createLockButton(c, 6));
                            c.addStaticButton(Container.getDefaultNextButton(c).setSlot(8));
                            for (Track track : SoundOfNoteBlocks.getTrackManager().getTracks()) {
                                c.addButton(new ButtonTrack(c, ItemUtil.getBuilder(RECORDS[track.getName().length() % RECORDS.length])
                                        .setName(track.getName())
                                        .setLore(track.getArtist())
                                        .build(),
                                        c.getButtons().size(), jukebox, track));
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
            Musicbox musicbox = Musicbox.findJukebox(evt.getBlock().getLocation());
            musicbox.stopPlaying();
            Musicbox.removeJukebox(musicbox);
        }
    }
}
