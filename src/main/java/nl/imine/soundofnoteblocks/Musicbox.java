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
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.xxmicloxx.NoteBlockAPI.PositionSongPlayer;
import com.xxmicloxx.NoteBlockAPI.Song;
import com.xxmicloxx.NoteBlockAPI.SongDestroyingEvent;

import nl.imine.api.gui.Button;
import nl.imine.api.gui.Container;
import nl.imine.api.gui.InventorySorter;
import nl.imine.api.gui.button.ButtonSort;
import nl.imine.api.holotag.Tag;
import nl.imine.api.holotag.TagAPI;
import nl.imine.api.util.ColorUtil;
import nl.imine.api.util.ItemUtil;

public class Musicbox implements Listener, Serializable {

	private static final long serialVersionUID = 3971612771253959236L;
	private static final double DISTANCE = Math.pow(35, 2);
	private static final Material[] RECORDS = new Material[]{Material.RECORD_10, Material.RECORD_12, Material.RECORD_3,
			Material.RECORD_4, Material.RECORD_5, Material.RECORD_6, Material.RECORD_7, Material.RECORD_8,
			Material.RECORD_9, Material.GOLD_RECORD, Material.GREEN_RECORD};

	private Coordinate coordinate;

	private Tag tag;
	private boolean tagVisible = true;
	private boolean radioMode = false;

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
		tag = TagAPI.createTag(getTagLocation());
		tag.addLine(" ");
		tag.addLine(" ");
		tag.setVisible(false);
	}

	private Musicbox(Location location) {
		this(new Coordinate(location));
	}

	public void randomTrack() {
		List<Track> tracks = SoundOfNoteBlocks.getInstance().getTrackManager().getTracks();
		playTrack(tracks.get((int) (Math.random() * (tracks.size() - 1D))));
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
			tag.setLocation(getTagLocation());
			tag.setVisible(tagVisible);
		}
	}

	public Location getTagLocation() {
		Location loc = coordinate.toLocation();
		if (loc.clone().add(0, 1, 0).getBlock().getType() == Material.AIR) {
			loc = loc.add(0, -1, 0);
		}
		return loc.add(0.5, 0.5, 0.5);
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
		if (isRadioMode()) {
			randomTrack();
		}
	}

	@EventHandler
	public void onChunkUnload(ChunkUnloadEvent evt) {
		if (evt.getChunk().equals(getLocation().getChunk())) {
			if (songPlayer != null) {
				songPlayer.destroy();
			}
			if (tag != null) {
				tag.remove();
			}
		}
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

	public void setRadioMode(boolean radioMode) {
		this.radioMode = radioMode;
	}

	public boolean isRadioMode() {
		return radioMode;
	}

	public Location getLocation() {
		return coordinate.toLocation();
	}

	public Button createStopButton(int slot) {
		return new ButtonStop(slot);
	}

	public Button createTogglenametagButton(int slot) {
		return new ButtonToggleNameTag(slot);
	}

	public Button createReplayButton(int slot) {
		return new ButtonReplay(slot);
	}

	public Button createRandomButton(int slot) {
		return new ButtomRandomTrack(slot);
	}

	public Button createLockButton(int slot) {
		return new ButtonLock(slot);
	}

	public ButtonTrack createTrackButton(Track track, int slot) {
		return new ButtonTrack(track, slot);
	}

	public ButtonMusicSort createSortButton(int slot) {
		return new ButtonMusicSort(slot);
	}

	public ButtonRadiomode createRadioButton(int slot) {
		return new ButtonRadiomode(slot);
	}

	private class ButtonLock extends Button {

		public ButtonLock(int slot) {
			super(ItemUtil.getBuilder(Material.REDSTONE_TORCH_ON).setName("Lock").build(), slot);
		}

		@Override
		public void doAction(Player player, Container container, ClickType clickType) {
			if (songPlayer != null && songPlayer.isPlaying()) {
				lock = true;
				player.closeInventory();
				player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
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

	private class ButtonToggleNameTag extends Button {

		public ButtonToggleNameTag(int slot) {
			super(ItemUtil.getBuilder(Material.NAME_TAG).setName("Toggle Currentsong Nametag").build(), slot);
		}

		@Override
		public void doAction(Player player, Container container, ClickType clickType) {
			if (isPlaying) {
				tagVisible = !tagVisible;
				tag.setLocation(getTagLocation());
				tag.setVisible(tagVisible);
			}
		}
	}

	private class ButtonReplay extends Button {

		public ButtonReplay(int slot) {
			super(ItemUtil.getBuilder(Material.FIREWORK_CHARGE).setName("Replay").build(), slot);
		}

		@Override
		public void doAction(Player player, Container container, ClickType clickType) {
			replayLastSong(true);
		}
	}

	private class ButtonRadiomode extends Button {

		public ButtonRadiomode(int slot) {
			super(ItemUtil.getBuilder(Material.ARMOR_STAND).setName("Radio").build(), slot);
		}

		@Override
		public ItemStack getItemStack() {
			return ItemUtil.getBuilder(super.getItemStack().getType(), super.getItemStack().getItemMeta())
					.addLore(ColorUtil.replaceColors("&7RadioMode: " + (isRadioMode() ? "&aEnabled" : "&cDisabled")))
					.build();
		}

		@Override
		public void doAction(Player player, Container container, ClickType clickType) {
			setRadioMode(!isRadioMode());
			player.closeInventory();
		}
	}

	private class ButtomRandomTrack extends Button {

		public ButtomRandomTrack(int slot) {
			super(ItemUtil.getBuilder(Material.RECORD_11).setName("Random").build(), slot);
		}

		@Override
		public void doAction(Player player, Container container, ClickType clickType) {
			randomTrack();
			player.closeInventory();
		}
	}

	private class ButtonStop extends Button {

		public ButtonStop(int slot) {
			super(ItemUtil.getBuilder(Material.APPLE).setName("Stop").setLore("Stop current song").build(), slot);
		}

		@Override
		public void doAction(Player player, Container container, ClickType clickType) {
			stopPlaying();
		}
	}

	public class ButtonTrack extends Button {

		private final Track track;

		public ButtonTrack(Track track, int slot) {
			super(ItemUtil.getBuilder(RECORDS[track.getName().length() % RECORDS.length]).build(), slot);
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
			int duratio = (int) (track.getSong().getLength() / track.getSong().getSpeed());
			im.setLore(Arrays.asList(new String[]{ColorUtil.replaceColors("&eArtist: " + track.getArtist()),
					ColorUtil.replaceColors("&cLength: %d:%02d", duratio / 60, duratio % 60)}));
			is.setItemMeta(im);
			return is;
		}

		@Override
		public void doAction(Player player, Container container, ClickType clickType) {
			if (isLocked() && !player.hasPermission("iMine.jukebox.lockbypass")) {
				player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1F, 1F);
			} else {
				playTrack(track);
			}
			player.closeInventory();
		}
	}

	private class ButtonMusicSort extends ButtonSort {

		public ButtonMusicSort(int slot) {
			super(ItemUtil.getBuilder(Material.SIGN).setName(ColorUtil.replaceColors("&6Sort on")).build(), slot,
					new InventorySorter[]{new InventoryTrackNameSorter(), new InventoryTrackArtistSorter(),
							new InventoryTrackSongLenghtSorter()});
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
				int ds1 = (int) (s1.getLength() / s1.getSpeed());
				Song s2 = ((ButtonTrack) o2).getTrack().getSong();
				int ds2 = (int) (s2.getLength() / s2.getSpeed());
				return ds1 - ds2;
			}
			return 1000;
		}
	}
}
