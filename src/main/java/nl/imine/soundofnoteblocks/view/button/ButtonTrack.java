package nl.imine.soundofnoteblocks.view.button;

import java.util.Arrays;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import nl.imine.api.gui.Container;
import nl.imine.api.util.ColorUtil;
import nl.imine.api.util.ItemUtil;
import nl.imine.soundofnoteblocks.model.MusicPlayer;
import nl.imine.soundofnoteblocks.model.Track;
import nl.imine.soundofnoteblocks.model.design.Lockable;
import nl.imine.soundofnoteblocks.view.MusicPlayerView;

public class ButtonTrack extends ButtonMusicPlayer {

	private final Track track;

	public ButtonTrack(Track track, MusicPlayer mp, int slot) {
			super(ItemUtil.getBuilder(MusicPlayerView.RECORDS.get(track.name().length() % MusicPlayerView.RECORDS.size()))
					.build(), mp, slot);
			this.track = track;
	}

	public Track getTrack() {
		return track;
	}

	@Override
	public ItemStack getItemStack() {
		ItemStack stack = super.getItemStack();
		ItemMeta meta = stack.getItemMeta();
		if (meta == null) {
			return stack;
		}
		meta.setDisplayName(ColorUtil.replaceColors("&b" + track.name()));
		int duration = (int) (track.song().getLength() / track.song().getSpeed());
		meta.setLore(Arrays.asList(ColorUtil.replaceColors("&eArtist: " + track.artist()),
				ColorUtil.replaceColors("&cLength: %d:%02d", duration / 60, duration % 60)));
		stack.setItemMeta(meta);
		return stack;
	}

	@Override
	public void doAction(Player player, Container container, ClickType clickType) {
		if (getMusicPlayer() instanceof Lockable && ((Lockable) getMusicPlayer()).isLocked()
				&& !player.hasPermission("iMine.jukebox.lockbypass")) {
			player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1F, 1F);
		} else {
			getMusicPlayer().playTrack(track);
		}
		player.closeInventory();
	}
}
