package nl.imine.soundofnoteblocks;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import com.xxmicloxx.NoteBlockAPI.NBSDecoder;
import com.xxmicloxx.NoteBlockAPI.PositionSongPlayer;
import com.xxmicloxx.NoteBlockAPI.Song;
import com.xxmicloxx.NoteBlockAPI.SongDestroyingEvent;

import java.util.UUID;

import nl.imine.api.gui.Button;
import nl.imine.api.gui.Container;

/**
 *
 * @author Sander
 */
public class Musicbox implements Listener, Serializable {

    private static final long serialVersionUID = 3971612771253959236L;

    private static final double DISTANCE = Math.pow(35, 2);

    private Coordinate coordinate;

    private ArmorStand tag;
    private boolean haveTag = true;

    private transient Track lastTrack;
    private transient boolean isPlaying, lock;
    private transient PositionSongPlayer songPlayer;

    private static ArrayList<Musicbox> jukeboxList = new ArrayList<>();

    public static List<Musicbox> getMusicBoxes() {
        return jukeboxList;
    }

    public static Musicbox findJukebox(Location location) {
        for (Musicbox j : jukeboxList) {
            if (j.getLocation().equals(location)) {
                return j;
            }
        }
        Musicbox j = new Musicbox(location);
        jukeboxList.add(j);
        Bukkit.getPluginManager().registerEvents(j, SoundOfNoteBlocks.getInstance());
        return j;
    }

    public static void removeJukebox(Musicbox musicbox) {
        jukeboxList.remove(musicbox);
    }

    private Musicbox(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    private Musicbox(Location location) {
        this(new Coordinate(location));
    }

    public void playTrack(Track track) {
        if (isPlaying) {
            stopPlaying();
        }
        lastTrack = track;
        isPlaying = true;
        Song song = NBSDecoder.parse(track.getFile());
        songPlayer = new PositionSongPlayer(song);
        songPlayer.setTargetLocation(coordinate.toLocation());
        songPlayer.setAutoDestroy(true);
        songPlayer.setPlaying(true);
        for (UUID uuid : songPlayer.getPlayerList()) {
            songPlayer.removePlayer(Bukkit.getPlayer(uuid));
        }
        for (Player p : getPlayersInRange()) {
            songPlayer.addPlayer(p);
        }
        if (haveTag) {
            summonTag();
        }
    }

    private void summonTag() {
        tag = (ArmorStand) coordinate.getWorld().spawnEntity(coordinate.toLocation().add(0.5, -0.5, 0.5),
                EntityType.ARMOR_STAND);
        tag.setVisible(false);
        tag.setGravity(false);
        tag.setBasePlate(false);
        tag.setRemoveWhenFarAway(true);
        tag.setCustomName(ChatColor.GOLD + lastTrack.getName() + " || " + ChatColor.BLUE + lastTrack.getArtist());
        tag.setCustomNameVisible(true);
    }

    public void stopPlaying() {
        isPlaying = false;
        if (songPlayer != null) {
            if (songPlayer.isPlaying()) {
                songPlayer.setPlaying(false);
            }
            songPlayer.destroy();
        }
        if (tag != null) {
            tag.remove();
            tag = null;
        }
    }

    public ArrayList<Player> getPlayersInRange() {
        ArrayList<Player> ret = new ArrayList<>();
        for (Player p : coordinate.getWorld().getPlayers()) {
            if (coordinate.toLocation().distance(p.getLocation()) < DISTANCE) {
                ret.add(p);
            }
        }
        return ret;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent evt) {
        if (songPlayer != null) {
            if (coordinate.toLocation().getWorld().equals(evt.getPlayer().getLocation().getWorld())) {
                if (evt.getPlayer().getLocation().distance(coordinate.toLocation()) < DISTANCE) {
                    songPlayer.addPlayer(evt.getPlayer());
                } else {
                    songPlayer.removePlayer(evt.getPlayer());
                }
            } else {
                songPlayer.removePlayer(evt.getPlayer());
            }
        }
    }

    @EventHandler
    public void onSongStop(SongDestroyingEvent evt) {
        if (songPlayer != null && songPlayer.equals(evt.getSongPlayer())) {
            isPlaying = false;
            songPlayer = null;
            if (tag != null) {
                tag.remove();
                tag = null;
            }
        }
        lock = false;
    }

    public void replayLastSong(boolean force) {
        if (force) {
            stopPlaying();
        }
        if (!isPlaying && lastTrack != null) {
            playTrack(lastTrack);
        }
    }

    public boolean isLocked() {
        return lock;
    }

    public Location getLocation() {
        return coordinate.toLocation();
    }

    public Button createStopButton(Container c, int slot) {
        return new StopButton(c, slot);
    }

    public Button createTogglenametagButton(Container c, int slot) {
        return new ToggleNametagButton(c, slot);
    }

    public Button createReplayButton(Container c, int slot) {
        return new ReplayButton(c, slot);
    }

    public Button createRandomButton(Container c, int slot) {
        return new RandomNumberButton(c, slot);
    }

    public Button createLockButton(Container c, int slot) {
        return new LockButton(c, slot);
    }

    // Radio
    // Volume
    private class LockButton extends Button {

        public LockButton(Container container, int slot) {
            super(container, Material.REDSTONE_TORCH_ON, "Lock", slot);
        }

        @Override
        public void doAction(Player player) {
            if (songPlayer != null && songPlayer.isPlaying()) {
                lock = true;
                player.closeInventory();
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1F, 1F);
                player.closeInventory();
            }
        }

        @Override
        public ItemStack getItemStack() {
            ItemStack is = super.getItemStack();
            is.setType((songPlayer != null && songPlayer.isPlaying()) ? Material.REDSTONE_TORCH_ON
                    : Material.REDSTONE_TORCH_OFF);
            return is;
        }
    }

    private class ToggleNametagButton extends Button {

        public ToggleNametagButton(Container container, int slot) {
            super(container, Material.NAME_TAG, "Toggle Currentsong Nametag", slot);
        }

        @Override
        public void doAction(Player player) {
            haveTag = !haveTag;
            if (!haveTag && tag != null) {
                tag.remove();
                tag = null;
            }
            if (haveTag && tag == null && lastTrack != null) {
                summonTag();
            }
        }
    }

    private class ReplayButton extends Button {

        public ReplayButton(Container container, int slot) {
            super(container, Material.FIREWORK_CHARGE, "Replay", slot);
        }

        @Override
        public void doAction(Player player) {
            replayLastSong(true);
        }
    }

    private class RandomNumberButton extends Button {

        public RandomNumberButton(Container container, int slot) {
            super(container, Material.RECORD_11, "Random", slot);
        }

        @Override
        public void doAction(Player player) {
            List<Track> tracks = SoundOfNoteBlocks.getTrackManager().getTracks();
            playTrack(tracks.get((int) (Math.random() * (double) (tracks.size() - 1D))));
            player.closeInventory();
        }
    }

    private class StopButton extends Button {

        public StopButton(Container container, int slot) {
            super(container, Material.APPLE, "Stop", slot, "Stop current song");
        }

        @Override
        public void doAction(Player player) {
            stopPlaying();
        }
    }
}
