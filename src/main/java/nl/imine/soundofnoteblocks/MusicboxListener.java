package nl.imine.soundofnoteblocks;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import com.xxmicloxx.NoteBlockAPI.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.SongPlayer;

import nl.imine.api.event.PlayerInteractTagEvent;
import nl.imine.api.gui.Container;
import nl.imine.api.gui.GuiManager;
import nl.imine.api.holotag.ActionType;
import nl.imine.api.util.ColorUtil;

public class MusicboxListener implements Listener {

    public static void init() {
        Bukkit.getPluginManager().registerEvents(new MusicboxListener(), SoundOfNoteBlocks.getInstance());
    }

    private MusicboxListener() {
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClickEntity(PlayerLoginEvent ple) {
        if (ple.getPlayer().getName().equalsIgnoreCase("MakerTim")) {
            Track happyBirthbday = null;
            for (Track track : SoundOfNoteBlocks.getInstance().getTrackManager().getTracks()) {
                if (track.getName().contains("Birthday")) {
                    happyBirthbday = track;
                    break;
                }
            }
            if (happyBirthbday != null) {
                System.out.println(happyBirthbday);
                SongPlayer sp = new RadioSongPlayer(happyBirthbday.getSong());
                sp.addPlayer(ple.getPlayer());
                for (Player pl : Bukkit.getOnlinePlayers()) {
                    sp.addPlayer(pl);
                }
                sp.setPlaying(true);
                System.out.println(sp.getPlayerList());
            } else {
                System.err.println("no happy birthday");
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
    public void onPlayerInteract(PlayerInteractEvent evt) {
        if (evt.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (evt.getClickedBlock().getType().equals(Material.JUKEBOX) && !evt.getPlayer().isSneaking()) {
                if (evt.getItem() == null || !evt.getItem().getType().name().toLowerCase().contains("record")) {
                    Musicbox jukebox = Musicbox.findJukebox(evt.getClickedBlock().getLocation());
                    openJukebox(evt.getPlayer(), jukebox);
                    evt.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerTagInteract(PlayerInteractTagEvent evt) {
        if (evt.getAction().equals(ActionType.RICHT_CLICK)) {
            for (Musicbox jukebox : Musicbox.getMusicBoxes()) {
                if (evt.getTag().equals(jukebox.getTag())) {
                    if (!evt.getPlayer().isSneaking()) {
                        openJukebox(evt.getPlayer(), jukebox);
                    }
                }
            }
        }
    }

    private void openJukebox(Player player, Musicbox jukebox) {
        if (!(((org.bukkit.block.Jukebox) jukebox.getLocation().getBlock().getState()).isPlaying())) {
            if (player.hasPermission("iMine.jukebox.play")) {
                if (jukebox.isLocked() && !player.getPlayer().hasPermission("iMine.jukebox.lockbypass")) {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_VILLAGER_NO, 1F, 1F);
                    return;
                }
                Container c = GuiManager.getInstance().createContainer(ColorUtil.replaceColors("&dJukebox       &cChoose Track!"), 45, true, false);
                for (Track track : SoundOfNoteBlocks.getInstance().getTrackManager().getTracks()) {
                    c.addButton(jukebox.createTrackButton(track, c.getButtons().size()));
                }
                c.addStaticButton(Container.getDefaultPreviousButton(c).setSlot(0));
                c.addStaticButton(jukebox.createSortButton(1));
                c.addStaticButton(jukebox.createReplayButton(2));
                c.addStaticButton(jukebox.createStopButton(3));
                c.addStaticButton(jukebox.createRandomButton(4));
                c.addStaticButton(jukebox.createTogglenametagButton(5));
                c.addStaticButton(jukebox.createLockButton(6));
                c.addStaticButton(Container.getDefaultNextButton(c).setSlot(8));
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
