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
		super(ItemUtil.getBuilder(MusicPlayerView.RECORDS[track.getName().length() % MusicPlayerView.RECORDS.length])
				.build(), mp, slot);
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
		if (getMusicPlayer() instanceof Lockable && ((Lockable) getMusicPlayer()).isLocked()
				&& !player.hasPermission("iMine.jukebox.lockbypass")) {
			player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1F, 1F);
		} else {
			getMusicPlayer().playTrack(track);
		}
		player.closeInventory();
	}
}
