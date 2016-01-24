package nl.imine.soundofnoteblocks;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import nl.imine.api.gui.Container;
import nl.imine.api.gui.GuiManager;
import nl.imine.api.holotag.ActionType;
import nl.imine.api.holotag.PlayerInteractTagEvent;
import nl.imine.api.util.ItemUtil;

public class MusicboxListener implements Listener {

    private static final Material[] RECORDS = new Material[] {
            Material.RECORD_10,
            Material.RECORD_12,
            Material.RECORD_3,
            Material.RECORD_4,
            Material.RECORD_5,
            Material.RECORD_6,
            Material.RECORD_7,
            Material.RECORD_8,
            Material.RECORD_9,
            Material.GOLD_RECORD,
            Material.GREEN_RECORD };

    public static void init() {
        Bukkit.getPluginManager().registerEvents(new MusicboxListener(), SoundOfNoteBlocks.getInstance());
    }

    private MusicboxListener() {

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
    public void onPlayerInteract(PlayerInteractEvent evt) {
        if (evt.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (evt.getClickedBlock().getType().equals(Material.JUKEBOX) && !evt.getPlayer().isSneaking()) {
                if (evt.getItem() == null || !evt.getItem().getType().name().toLowerCase().contains("record")) {
                    Musicbox jukebox = Musicbox.findJukebox(evt.getClickedBlock().getLocation());
                    openJukebox(evt.getPlayer(), jukebox);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerTagInteract(PlayerInteractTagEvent evt) {
        if (evt.getAction().equals(ActionType.RICHT_CLICK)) {
            for (Musicbox jukebox : Musicbox.getMusicBoxes()) {
                if (evt.getTag().equals(jukebox.getTag())) {
                    openJukebox(evt.getPlayer(), jukebox);
                }
            }
        }
    }

    private void openJukebox(Player player, Musicbox jukebox) {
        if (!(((org.bukkit.block.Jukebox) jukebox.getLocation().getBlock().getState()).isPlaying())) {
            if (player.hasPermission("iMine.jukebox.play")) {
                if (jukebox.isLocked() && !player.getPlayer().hasPermission("iMine.jukebox.lockbypass")) {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.VILLAGER_NO, 1F, 1F);
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
                    c.addButton(jukebox.createTrackButton(c, ItemUtil.getBuilder(RECORDS[track.getName().length() % RECORDS.length]).setName(track.getName()).setLore(track.getArtist()).build(), c.getButtons().size(), track));
                }
                c.open(player);
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
