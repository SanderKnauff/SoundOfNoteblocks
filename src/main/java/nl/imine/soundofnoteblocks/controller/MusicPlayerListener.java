package nl.imine.soundofnoteblocks.controller;

import com.xxmicloxx.NoteBlockAPI.event.SongDestroyingEvent;
import nl.imine.api.holotag.TagAPI;
import nl.imine.soundofnoteblocks.SoundOfNoteBlocksPlugin;
import nl.imine.soundofnoteblocks.model.Gettoblaster;
import nl.imine.soundofnoteblocks.model.Jukebox;
import nl.imine.soundofnoteblocks.model.MusicPlayer;
import nl.imine.soundofnoteblocks.model.Walkman;
import nl.imine.soundofnoteblocks.model.design.Lockable;
import nl.imine.soundofnoteblocks.view.MusicPlayerView;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;

public class MusicPlayerListener implements Listener {
    private static final int CREATIVE_MODE_HELMET_SLOT_ID = 5;
    private static final int SURVIVAL_MODE_HELMET_SLOT_ID = 39;

    private final MusicPlayerManager musicPlayerManager;
    private final TrackManager trackManager;

    public MusicPlayerListener(
            MusicPlayerManager musicPlayerManager,
            TrackManager trackManager
    ) {
        this.musicPlayerManager = musicPlayerManager;
        this.trackManager = trackManager;
    }

    @EventHandler
    public void onRedstoneEvent(BlockRedstoneEvent event) {
        if (event.getNewCurrent() > 2 && event.getNewCurrent() > event.getOldCurrent()) {
            for (int i = -1; i < 2; i++) {
                final int increment = i;
                musicPlayerManager.getJukeboxes().stream()
                        .filter(m -> (m.getLocation().equals(event.getBlock().getLocation().clone().add(increment, 0, 0)))
                                || m.getLocation().equals(event.getBlock().getLocation().clone().add(0, 0, increment)))
                        .forEach(MusicPlayer::replay);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getClickedBlock() == null || !event.getClickedBlock().getType().equals(Material.JUKEBOX)) {
            return;
        }
        Jukebox jukebox = musicPlayerManager.getOrCreateJukebox(event.getClickedBlock().getLocation());
        Player player = event.getPlayer();
        if (player.hasPermission("imine.jukebox.play")) {
            if (!player.isSneaking()) {
                if (((org.bukkit.block.Jukebox) event.getClickedBlock().getState()).getPlaying().equals(Material.AIR)) {
                    if (event.getItem() == null || !event.getItem().getType().name().toLowerCase().contains("record")) {
                        if (!jukebox.isLocked() || player.hasPermission("iMine.jukebox.lockbypass")) {
                            if (jukebox.isRadioMode()) {
                                MusicPlayerView.getRadioModeContainer(jukebox).open(player);
                            } else {
                                MusicPlayerView.getMusicPlayerContainer(jukebox, trackManager).open(player);
                            }
                            event.setCancelled(true);
                        }
                    }
                }
            }
            if (jukebox.isPlaying()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent pde) {
        musicPlayerManager.removeGettoblaster(pde.getEntity());
        musicPlayerManager.removeWalkman(pde.getEntity());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent pqe) {
        musicPlayerManager.removeGettoblaster(pqe.getPlayer());
        musicPlayerManager.removeWalkman(pqe.getPlayer());
    }

    @EventHandler
    public void onPlayerRemoveHelmet(InventoryClickEvent event) {
        if (event.getSlotType() != InventoryType.SlotType.ARMOR
                || (event.getSlot() != CREATIVE_MODE_HELMET_SLOT_ID && event.getWhoClicked().getGameMode().equals(GameMode.CREATIVE))
                || (event.getSlot() != SURVIVAL_MODE_HELMET_SLOT_ID && event.getWhoClicked().getGameMode().equals(GameMode.CREATIVE))) {
            return;
        }
        musicPlayerManager.removeGettoblaster(event.getWhoClicked());
    }

    @EventHandler
    public void onPlayerInteractGettoblaster(PlayerInteractEvent event) {
        if (event.getPlayer().getInventory().getHelmet() == null || !event.getPlayer().getInventory().getHelmet().getType().equals(Material.JUKEBOX)) {
            return;
        }

        Player player = event.getPlayer();
        if (player.hasPermission("imine.jukebox.play") && player.isSneaking()
                && player.getInventory().getItemInMainHand().getType() == Material.AIR) {
            Gettoblaster gb = musicPlayerManager.getOrCreateGettoblaster(player);
            if (gb.isRadioMode()) {
                MusicPlayerView.getRadioModeContainer(gb).open(player);
            } else {
                MusicPlayerView.getMusicPlayerContainer(gb, trackManager).open(player);
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("imine.jukebox.play")) {
            if (player.isSneaking() && event.getItemDrop().getItemStack().getType() == Material.JUKEBOX) {
                Bukkit.getScheduler().runTaskLater(SoundOfNoteBlocksPlugin.getInstance(), () -> {
                    Gettoblaster gb = musicPlayerManager.getOrCreateGettoblaster(player);
                    if (gb.isRadioMode()) {
                        MusicPlayerView.getRadioModeContainer(gb).open(player);
                    } else {
                        MusicPlayerView.getMusicPlayerContainer(gb, trackManager).open(player);
                    }
                }, 0);
                ItemStack helmet = player.getInventory().getHelmet();
                if (helmet != null) {
                    helmet = helmet.clone();
                }
                player.getInventory().setHelmet(event.getItemDrop().getItemStack());
                if (helmet == null) {
                    event.getItemDrop().remove();
                } else {
                    event.getItemDrop().setItemStack(helmet);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerItemHandSwitch(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("imine.jukebox.play")) {
            if (event.getOffHandItem() != null && event.getOffHandItem().getType() == Material.JUKEBOX) {
                Bukkit.getScheduler().runTaskLater(SoundOfNoteBlocksPlugin.getInstance(), () -> {
                    Walkman walkman = musicPlayerManager.getOrCreateWalkman(player);
                    if (walkman.isRadioMode()) {
                        MusicPlayerView.getRadioModeContainer(walkman).open(player);
                    } else {
                        MusicPlayerView.getMusicPlayerContainer(walkman, trackManager).open(player);
                    }
                }, 1);
            } else if (event.getMainHandItem() != null && event.getMainHandItem().getType() == Material.JUKEBOX) {
                musicPlayerManager.removeWalkman(player);
            }
        }
    }

    @EventHandler
    public void onPlayerBlockBreak(BlockBreakEvent bbe) {
        if (bbe.isCancelled()) {
            return;
        }
        if (bbe.getBlock().getType().equals(Material.JUKEBOX)) {
            musicPlayerManager.removeJukebox(bbe.getBlock().getLocation());
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        for (Jukebox jukebox : musicPlayerManager.getJukeboxes()) {
            if (jukebox.isPlaying() && event.getTo() != null && jukebox.getLocation().getWorld() == event.getTo().getWorld()) {
                if (event.getTo().distance(jukebox.getLocation()) < Jukebox.DISTANCE) {
                    jukebox.getSongPlayer().addPlayer(event.getPlayer());
                } else if (jukebox.getSongPlayer().getPlayerUUIDs().contains(event.getPlayer().getUniqueId())) {
                    jukebox.getSongPlayer().removePlayer(event.getPlayer());
                }
            }
        }
        for (Gettoblaster gettoblaster : musicPlayerManager.getGettoblasters()) {
            if (gettoblaster.isPlaying() && event.getTo() != null && gettoblaster.getCenteredEntity().getLocation().getWorld() == event.getTo().getWorld()) {
                if (event.getTo().distance(gettoblaster.getCenteredEntity().getLocation()) < Gettoblaster.DISTANCE) {
                    gettoblaster.getSongPlayer().addPlayer(event.getPlayer());
                } else if (gettoblaster.getSongPlayer().getPlayerUUIDs().contains(event.getPlayer().getUniqueId())) {
                    gettoblaster.getSongPlayer().removePlayer(event.getPlayer());
                }
            }
        }
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        for (Jukebox jukebox : musicPlayerManager.getJukeboxes()) {
            if (jukebox.getSongPlayer() == null) {
                continue;
            }
            if (jukebox.getSongPlayer().getPlayerUUIDs().contains(event.getPlayer().getUniqueId())) {
                jukebox.getSongPlayer().removePlayer(event.getPlayer());
            }
        }
    }

    @EventHandler
    public void onSongStop(SongDestroyingEvent sde) {
        for (MusicPlayer musicPlayer : musicPlayerManager.getAllMusicPlayers()) {
            if (musicPlayer.getSongPlayer() == sde.getSongPlayer()) {
                musicPlayer.setPlaying(false);
                if (musicPlayer.isRadioMode()) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(SoundOfNoteBlocksPlugin.getInstance(), () -> musicPlayer.playRandomTrack(trackManager.getTracks()), 20L);
                }
                if (musicPlayer instanceof Lockable lock) {
                    lock.setLocked(false);
                }
            }
        }
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        for (Jukebox jukebox : musicPlayerManager.getJukeboxes()) {
            if (jukebox.getLocation().getWorld() == null) {
                continue;
            }
            if (!event.getChunk().equals(jukebox.getLocation().getChunk())) {
                continue;
            }
            if (!jukebox.isRadioMode()) {
                continue;
            }
            Bukkit.getScheduler().scheduleSyncDelayedTask(SoundOfNoteBlocksPlugin.getInstance(), () -> {
                jukebox.playRandomTrack(trackManager.getTracks());
            }, 20L);
        }
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        musicPlayerManager.removeJukeboxesFromChunk(event.getChunk());
    }

}
