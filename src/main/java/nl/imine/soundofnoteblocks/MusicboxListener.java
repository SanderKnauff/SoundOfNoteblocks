package nl.imine.soundofnoteblocks;

import com.xxmicloxx.NoteBlockAPI.SongDestroyingEvent;
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

import nl.imine.api.event.PlayerInteractTagEvent;
import nl.imine.api.gui.Container;
import nl.imine.api.gui.GuiManager;
import nl.imine.api.holotag.ActionType;
import nl.imine.api.util.ColorUtil;
import org.bukkit.block.Jukebox;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

public class MusicboxListener implements Listener {

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

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteract(PlayerInteractEvent evt) {
		if (!evt.isCancelled()) {
			if (evt.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				if (evt.getClickedBlock().getType().equals(Material.JUKEBOX)) {
					Musicbox jukebox = Musicbox.findJukebox(evt.getClickedBlock().getLocation());
					if (!evt.getPlayer().isSneaking()) {
						if (((Jukebox) evt.getClickedBlock().getState()).getPlaying().equals(Material.AIR)) {
							if (evt.getItem() == null
									|| !evt.getItem().getType().name().toLowerCase().contains("record")) {
								openJukebox(evt.getPlayer(), jukebox);
								evt.setCancelled(true);
							}
						}
					}
					if (jukebox.isPlaying()) {
						evt.setCancelled(true);
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerTagInteract(PlayerInteractTagEvent evt) {
		if (!evt.isCancelled()) {
			if (evt.getAction().equals(ActionType.RICHT_CLICK)) {
				for (Musicbox jukebox : Musicbox.getMusicBoxes()) {
					if (evt.getTag().equals(jukebox.getTag())) {
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
				Container c;
				if (jukebox.isRadioMode()) {
					c = GuiManager.getInstance().createContainer(ColorUtil.replaceColors("&zRadio!"), 9, false, false);
					c.addButton(jukebox.createRadioButton(4));
				} else {
					c = GuiManager.getInstance().createContainer(
						ColorUtil.replaceColors("&dJukebox       &cChoose Track!"), 45, true, false);
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
					c.addStaticButton(jukebox.createRadioButton(7));
					c.addStaticButton(Container.getDefaultNextButton(c).setSlot(8));
				}
				c.open(player);
			}
		}
	}

	private void openWalkman(Player player, Walkman jukebox) {
		if (player.hasPermission("iMine.jukebox.play")) {
			Container c;
			if (jukebox.isRadioMode()) {
				c = GuiManager.getInstance().createContainer(ColorUtil.replaceColors("&zRadio!"), 9, false, false);
				c.addButton(jukebox.createRadioButton(4));
			} else {
				c = GuiManager.getInstance().createContainer(ColorUtil.replaceColors("&dJukebox       &cChoose Track!"),
					45, true, false);
				for (Track track : SoundOfNoteBlocks.getInstance().getTrackManager().getTracks()) {
					c.addButton(jukebox.createTrackButton(track, c.getButtons().size()));
				}
				c.addStaticButton(Container.getDefaultPreviousButton(c).setSlot(0));
				c.addStaticButton(jukebox.createSortButton(1));
				c.addStaticButton(jukebox.createReplayButton(2));
				c.addStaticButton(jukebox.createStopButton(3));
				c.addStaticButton(jukebox.createRandomButton(4));
				c.addStaticButton(jukebox.createRadioButton(7));
				c.addStaticButton(Container.getDefaultNextButton(c).setSlot(8));
			}
			c.open(player);
		}
	}

	@EventHandler
	public void onPlayerItemHandSwitch(PlayerSwapHandItemsEvent evt) {
		Player player = evt.getPlayer();
		if (player.hasPermission("imine.jukebox.play")) {
			if (evt.getOffHandItem().getType().equals(Material.JUKEBOX)) {
				Bukkit.getScheduler().runTaskLater(SoundOfNoteBlocks.getInstance(), () -> {
					openWalkman(player, Walkman.findWalkman(player));
				} , 1);
			} else if (evt.getMainHandItem().getType().equals(Material.JUKEBOX)) {
				Walkman.removeWalkman(Walkman.findWalkman(player));
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

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent evt) {
		for (Musicbox musicbox : Musicbox.getMusicBoxes()) {
			if (musicbox.getSongPlayer() != null) {
				if (musicbox.getLocation().getWorld().equals(evt.getPlayer().getLocation().getWorld())) {
					if (evt.getPlayer().getLocation().distance(musicbox.getLocation()) < Musicbox.DISTANCE) {
						musicbox.getSongPlayer().addPlayer(evt.getPlayer());
					} else {
						musicbox.getSongPlayer().removePlayer(evt.getPlayer());
					}
				} else {
					musicbox.getSongPlayer().removePlayer(evt.getPlayer());
				}
			}
		}
	}

	@EventHandler
	public void onSongStop(SongDestroyingEvent evt) {
		for (Musicbox musicbox : Musicbox.getMusicBoxes()) {
			if (musicbox.getSongPlayer() != null && musicbox.getSongPlayer().equals(evt.getSongPlayer())) {
				musicbox.setPlaying(false);
				musicbox.setSongPlayer(null);
				if (musicbox.isRadioMode()) {
					Bukkit.getScheduler().scheduleSyncDelayedTask(SoundOfNoteBlocks.plugin, () -> {
						musicbox.randomTrack();
					} , 20L);
				}
				musicbox.getTag().setVisible(false);
				musicbox.setLocked(false);
			}
		}
		for (Walkman walkman : Walkman.getWalkmans()) {
			if (walkman.getSongPlayer() != null && walkman.getSongPlayer().equals(evt.getSongPlayer())) {
				walkman.setPlaying(false);
				walkman.setSongPlayer(null);
				if (walkman.isRadioMode()) {
					Bukkit.getScheduler().scheduleSyncDelayedTask(SoundOfNoteBlocks.plugin, () -> {
						walkman.randomTrack();
					} , 20L);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onChunkLoad(ChunkLoadEvent evt) {
		for (Musicbox musicbox : Musicbox.getMusicBoxes()) {
			if (evt.getChunk().equals(musicbox.getLocation().getChunk())) {
				if (musicbox.isRadioMode()) {
					Bukkit.getScheduler().scheduleSyncDelayedTask(SoundOfNoteBlocks.plugin,
						() -> musicbox.randomTrack(), 20L);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onChunkUnload(ChunkUnloadEvent evt) {
		if (!evt.isCancelled()) {
			for (Musicbox musicbox : Musicbox.getMusicBoxes()) {
				if (evt.getChunk().equals(musicbox.getLocation().getChunk())) {
					if (musicbox.getSongPlayer() != null) {
						musicbox.getSongPlayer().destroy();
					}
					if (musicbox.getTag() != null) {
						musicbox.getTag().remove();
					}
				}
			}
		}
	}
}
