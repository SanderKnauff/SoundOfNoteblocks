package nl.imine.soundofnoteblocks;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.xxmicloxx.NoteBlockAPI.PositionSongPlayer;
import com.xxmicloxx.NoteBlockAPI.Song;
import com.xxmicloxx.NoteBlockAPI.SongDestroyingEvent;

import nl.imine.api.gui.Button;
import nl.imine.api.gui.Container;
import nl.imine.api.gui.button.ButtonSort;
import nl.imine.api.gui.button.ButtonSort.InventorySorter;
import nl.imine.api.holotag.Tag;
import nl.imine.api.holotag.TagAPI;
import nl.imine.api.util.ColorUtil;
import nl.imine.api.util.ItemUtil;

/**
 *
 * @author Sander
 */
public class Musicbox implements Listener, Serializable {

    private static final long serialVersionUID = 3971612771253959236L;
    private static final double DISTANCE = Math.pow(35, 2);
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

    private Coordinate coordinate;

    private Tag tag;
    private boolean tagVisible = true;

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
        tag = TagAPI.createTag(coordinate.toLocation().add(0.5, 0.5, 0.5));
        tag.addLine(" ");
        tag.addLine(" ");
        tag.setVisible(false);
    }

    private Musicbox(Location location) {
        this(new Coordinate(location));
    }

    public void playTrack(Track track) {
        if (this.getLocation().getBlock().getType().equals(Material.JUKEBOX)) {
            if (isPlaying) {
                stopPlaying();
            }
            lastTrack = track;
            isPlaying = true;
            Song song = track.getSong();
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
            tag.getLine(0).setLabel(ChatColor.GOLD + lastTrack.getName());
            tag.getLine(1).setLabel(ChatColor.BLUE + lastTrack.getArtist());
            tag.setVisible(tagVisible);
        }
    }

    public void stopPlaying() {
        isPlaying = false;
        if (songPlayer != null) {
            if (songPlayer.isPlaying()) {
                songPlayer.setPlaying(false);
            }
            songPlayer.destroy();
        }
        tag.setVisible(false);
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

    public Tag getTag() {
        return tag;
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
            tag.setVisible(false);
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
        return new ButtonStop(c, slot);
    }

    public Button createTogglenametagButton(Container c, int slot) {
        return new ButtonToggleNameTag(c, slot);
    }

    public Button createReplayButton(Container c, int slot) {
        return new ButtonReplay(c, slot);
    }

    public Button createRandomButton(Container c, int slot) {
        return new ButtomRandomTrack(c, slot);
    }

    public Button createLockButton(Container c, int slot) {
        return new ButtonLock(c, slot);
    }

    public ButtonTrack createTrackButton(Container container, Track track, int slot) {
        return new ButtonTrack(container, track, slot);
    }

    public ButtonMusicSort createSortButton(Container container, int slot) {
        return new ButtonMusicSort(container, slot);
    }

    // Radio
    // Volume
    private class ButtonLock extends Button {

        public ButtonLock(Container container, int slot) {
            super(container, ItemUtil.getBuilder(Material.REDSTONE_TORCH_ON).setName("Lock").build(), slot);
        }

        @Override
        public void doAction(Player player, ClickType clickType) {
            if (songPlayer != null && songPlayer.isPlaying()) {
                lock = true;
                player.closeInventory();
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1F, 1F);
            }
        }

        @Override
        public ItemStack getItemStack() {
            ItemStack is = super.getItemStack();
            is.setType((songPlayer != null && songPlayer.isPlaying()) ? Material.REDSTONE_TORCH_ON : Material.REDSTONE_TORCH_OFF);
            return is;
        }
    }

    private class ButtonToggleNameTag extends Button {

        public ButtonToggleNameTag(Container container, int slot) {
            super(container, ItemUtil.getBuilder(Material.NAME_TAG).setName("Toggle Currentsong Nametag").build(), slot);
        }

        @Override
        public void doAction(Player player, ClickType clickType) {
            tagVisible = !tagVisible;
            tag.setVisible(tagVisible);
        }
    }

    private class ButtonReplay extends Button {

        public ButtonReplay(Container container, int slot) {
            super(container, ItemUtil.getBuilder(Material.FIREWORK_CHARGE).setName("Replay").build(), slot);
        }

        @Override
        public void doAction(Player player, ClickType clickType) {
            replayLastSong(true);
        }
    }

    private class ButtomRandomTrack extends Button {

        public ButtomRandomTrack(Container container, int slot) {
            super(container, ItemUtil.getBuilder(Material.RECORD_11).setName("Random").build(), slot);
        }

        @Override
        public void doAction(Player player, ClickType clickType) {
            List<Track> tracks = SoundOfNoteBlocks.getInstance().getTrackManager().getTracks();
            playTrack(tracks.get((int) (Math.random() * (double) (tracks.size() - 1D))));
            player.closeInventory();
        }
    }

    private class ButtonStop extends Button {

        public ButtonStop(Container container, int slot) {
            super(container, ItemUtil.getBuilder(Material.APPLE).setName("Stop").setLore("Stop current song").build(), slot);
        }

        @Override
        public void doAction(Player player, ClickType clickType) {
            stopPlaying();
        }
    }

    public class ButtonTrack extends Button {

        private final Track track;

        public ButtonTrack(Container container, Track track, int slot) {
            super(container, ItemUtil.getBuilder(RECORDS[track.getName().length() % RECORDS.length]).build(), slot);
            this.track = track;
        }

        public Track getTrack() {
            return track;
        }

        @Override
        public ItemStack getItemStack() {
            ItemStack is = super.getItemStack();
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(ColorUtil.replaceColors("&b" + track.getName()));
            im.setLore(Arrays.asList(new String[] {
                    ColorUtil.replaceColors("&e" + track.getArtist()),
                    ColorUtil.replaceColors("&c" + track.getSong().getLength()) }));
            is.setItemMeta(im);
            return is;
        }

        @Override
        public void doAction(Player player, ClickType clickType) {
            if (isLocked() && !player.hasPermission("iMine.jukebox.lockbypass")) {
                player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1F, 1F);
            } else {
                playTrack(track);
            }
            player.closeInventory();
        }
    }

    private class ButtonMusicSort extends ButtonSort {
        public ButtonMusicSort(Container container, int slot) {
            super(container, ItemUtil.getBuilder(Material.SIGN).setName(ColorUtil.replaceColors("&6Sort on")).build(), slot, new InventorySorter[] {
                    new InventoryTrackNameSorter(),
                    new InventoryTrackArtistSorter(),
                    new InventoryTrackSongLenghtSorter() });
        }
    }

    private static class InventoryTrackNameSorter extends InventorySorter {
        public InventoryTrackNameSorter() {
            super("On name");
        }

        @Override
        public int compare(Button o1, Button o2) {
            if (o1 instanceof ButtonTrack && o2 instanceof ButtonTrack) {
                Track t1 = ((ButtonTrack) o1).getTrack();
                Track t2 = ((ButtonTrack) o2).getTrack();
                return t1.getName().compareTo(t2.getName());
            }
            return 1000;
        }
    }

    private static class InventoryTrackArtistSorter extends InventorySorter {
        public InventoryTrackArtistSorter() {
            super("On artist");
        }

        @Override
        public int compare(Button o1, Button o2) {
            if (o1 instanceof ButtonTrack && o2 instanceof ButtonTrack) {
                Track t1 = ((ButtonTrack) o1).getTrack();
                Track t2 = ((ButtonTrack) o2).getTrack();
                return t1.getArtist().compareTo(t2.getArtist());
            }
            return 1000;
        }
    }

    private static class InventoryTrackSongLenghtSorter extends InventorySorter {
        public InventoryTrackSongLenghtSorter() {
            super("On length");
        }

        @Override
        public int compare(Button o1, Button o2) {
            if (o1 instanceof ButtonTrack && o2 instanceof ButtonTrack) {
                Song s1 = ((ButtonTrack) o1).getTrack().getSong();
                Song s2 = ((ButtonTrack) o2).getTrack().getSong();
                return s1.getLength() - s2.getLength();
            }
            return 1000;
        }
    }
}
