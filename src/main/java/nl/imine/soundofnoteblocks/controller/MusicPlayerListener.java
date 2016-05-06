package nl.imine.soundofnoteblocks.controller;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import com.xxmicloxx.NoteBlockAPI.SongDestroyingEvent;

import nl.imine.api.event.PlayerInteractTagEvent;
import nl.imine.api.holotag.ActionType;
import nl.imine.api.util.ColorUtil;
import nl.imine.api.util.PlayerUtil;
import nl.imine.soundofnoteblocks.SoundOfNoteBlocksPlugin;
import nl.imine.soundofnoteblocks.model.Jukebox;
import nl.imine.soundofnoteblocks.model.MusicPlayer;
import nl.imine.soundofnoteblocks.model.Walkman;
import nl.imine.soundofnoteblocks.model.design.Lockable;
import nl.imine.soundofnoteblocks.model.design.Tagable;
import nl.imine.soundofnoteblocks.view.MusicPlayerView;

public class MusicPlayerListener implements Listener {

	@EventHandler
	public void onRedstoneEvent(BlockRedstoneEvent bre) {
		if (!SoundOfNoteBlocksPlugin.isLoaded()) {
			return;
		}
		if (bre.getNewCurrent() > 2 && bre.getNewCurrent() > bre.getOldCurrent()) {
			for (int i = -1; i < 2; i++) {
				final int increment = i;
				MusicPlayerManager.getJukeboxes().stream()
						.filter(m -> (m.getLocation().equals(bre.getBlock().getLocation().clone().add(increment, 0, 0)))
								|| m.getLocation().equals(bre.getBlock().getLocation().clone().add(0, 0, increment)))
						.forEach(m -> {
							m.replay();
						});
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteract(PlayerInteractEvent pie) {
		if (!SoundOfNoteBlocksPlugin.isLoaded() || pie.isCancelled()) {
			notLoadedMssg(pie.getPlayer());
			return;
		}
		if (pie.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if (pie.getClickedBlock().getType().equals(Material.JUKEBOX)) {
				Jukebox jukebox = MusicPlayerManager.getJukebox(pie.getClickedBlock().getLocation());
				if (!pie.getPlayer().isSneaking()) {
					if (((org.bukkit.block.Jukebox) pie.getClickedBlock().getState()).getPlaying()
							.equals(Material.AIR)) {
						if (pie.getItem() == null || !pie.getItem().getType().name().toLowerCase().contains("record")) {
							if (jukebox.isRadioMode()) {
								MusicPlayerView.getRadiomodeContainer(jukebox).open(pie.getPlayer());
							} else {
								MusicPlayerView.getMusicPlayerConatainer(jukebox).open(pie.getPlayer());
							}
							pie.setCancelled(true);
						}
					}
				}
				if (jukebox.isPlaying()) {
					pie.setCancelled(true);

				}
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerTagInteract(PlayerInteractTagEvent pite) {
		if (!SoundOfNoteBlocksPlugin.isLoaded() || pite.isCancelled()) {
			notLoadedMssg(pite.getPlayer());
			return;
		}
		if (pite.getAction().equals(ActionType.RICHT_CLICK)) {
			for (Jukebox jukebox : MusicPlayerManager.getJukeboxes()) {
				if (pite.getTag().equals(jukebox.getTag())) {
					if (jukebox.isRadioMode()) {
						MusicPlayerView.getRadiomodeContainer(jukebox).open(pite.getPlayer());
					} else {
						MusicPlayerView.getMusicPlayerConatainer(jukebox).open(pite.getPlayer());
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerDeath(PlayerDeathEvent pde) {
		if (pde.getEntity().hasPermission("imine.jukebox.play")) {
			MusicPlayerManager.removeWalkman(pde.getEntity());
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerItemHandSwitch(PlayerSwapHandItemsEvent sphie) {
		if (!SoundOfNoteBlocksPlugin.isLoaded() || sphie.isCancelled()) {
			notLoadedMssg(sphie.getPlayer());
			return;
		}
		Player player = sphie.getPlayer();
		if (player.hasPermission("imine.jukebox.play")) {
			if (sphie.getOffHandItem().getType() == Material.JUKEBOX) {
				Bukkit.getScheduler().runTaskLater(SoundOfNoteBlocksPlugin.getInstance(), () -> {
					Walkman wm = MusicPlayerManager.getWalkman(player);
					if (wm.isRadioMode()) {
						MusicPlayerView.getRadiomodeContainer(wm).open(player);
					} else {
						MusicPlayerView.getMusicPlayerConatainer(wm).open(player);
					}
				} , 1);
			} else if (sphie.getMainHandItem().getType() == Material.JUKEBOX) {
				MusicPlayerManager.removeWalkman(player);
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerBlockBreak(BlockBreakEvent bbe) {
		if (bbe.isCancelled()) {
			return;
		}
		if (bbe.getBlock().getType().equals(Material.JUKEBOX)) {
			MusicPlayerManager.removeJukebox(bbe.getBlock().getLocation());
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerMove(PlayerMoveEvent pme) {
		if (!SoundOfNoteBlocksPlugin.isLoaded() || pme.isCancelled()) {
			return;
		}
		for (Jukebox jukebox : MusicPlayerManager.getJukeboxes()) {
			if (jukebox.isPlaying() && jukebox.getLocation().getWorld() == pme.getTo().getWorld()) {
				if (pme.getTo().distance(jukebox.getLocation()) < Jukebox.DISTANCE) {
					jukebox.getSongPlayer().addPlayer(pme.getPlayer());
				} else if (jukebox.getSongPlayer().getPlayerList().contains(pme.getPlayer().getUniqueId())) {
					jukebox.getSongPlayer().removePlayer(pme.getPlayer());
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onWorldChange(PlayerChangedWorldEvent pcwe) {
		if (!SoundOfNoteBlocksPlugin.isLoaded()) {
			return;
		}
		for (Jukebox jukebox : MusicPlayerManager.getJukeboxes()) {
			if (jukebox.getSongPlayer().getPlayerList().contains(pcwe.getPlayer().getUniqueId())) {
				jukebox.getSongPlayer().removePlayer(pcwe.getPlayer());
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onSongStop(SongDestroyingEvent sde) {
		if (!SoundOfNoteBlocksPlugin.isLoaded() || sde.isCancelled()) {
			return;
		}
		for (MusicPlayer mp : MusicPlayerManager.getAllMusicPlayers()) {
			if (mp.getSongPlayer() == sde.getSongPlayer()) {
				mp.setPlaying(false);
				if (mp.isRadioMode()) {
					Bukkit.getScheduler().scheduleSyncDelayedTask(SoundOfNoteBlocksPlugin.plugin, () -> {
						mp.playRandomTrack(TrackManager.getTrackArray());
					} , 20L);
				}
				if (mp instanceof Tagable) {
					Tagable tag = (Tagable) mp;
					tag.getTag().setVisible(false);
				}
				if (mp instanceof Lockable) {
					Lockable lock = (Lockable) mp;
					lock.setLocked(false);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onChunkLoad(ChunkLoadEvent cle) {
		if (!SoundOfNoteBlocksPlugin.isLoaded()) {
			return;
		}
		for (Jukebox jukebox : MusicPlayerManager.getJukeboxes()) {
			if (cle.getChunk() == jukebox.getLocation().getChunk()) {
				if (jukebox.isRadioMode()) {
					Bukkit.getScheduler().scheduleSyncDelayedTask(SoundOfNoteBlocksPlugin.plugin, () -> {
						jukebox.playRandomTrack(TrackManager.getTrackArray());
					} , 20L);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onChunkUnload(ChunkUnloadEvent cue) {
		if (!SoundOfNoteBlocksPlugin.isLoaded() || cue.isCancelled()) {
			return;
		}
		for (Jukebox jukebox : MusicPlayerManager.getJukeboxes()) {
			if (cue.getChunk() == jukebox.getLocation().getChunk()) {
				jukebox.stopPlaying();
				jukebox.getTag().remove();
			}
		}
	}

	private void notLoadedMssg(Player pl) {
		if (!SoundOfNoteBlocksPlugin.isLoaded()) {
			PlayerUtil.sendActionMessage(pl,
				ColorUtil.replaceColors("&cSoundOfNoteBlocks is loading... plz wait a second!"));
		}
	}

}
