package nl.imine.soundofnoteblocks.view.button;

import nl.imine.api.gui.Container;
import nl.imine.api.util.ColorUtil;
import nl.imine.api.util.ItemUtil;
import nl.imine.soundofnoteblocks.controller.TrackManager;
import nl.imine.soundofnoteblocks.model.MusicPlayer;
import nl.imine.soundofnoteblocks.model.design.Lockable;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class ButtomRandomTrack extends ButtonMusicPlayer {
	private final TrackManager trackManager;

	public ButtomRandomTrack(TrackManager trackManager, MusicPlayer mp, int slot) {
		super(ItemUtil.getBuilder(Material.MUSIC_DISC_11).setName(ColorUtil.replaceColors("Random"))
				.addLore("", ColorUtil.replaceColors("&bDid you know?"),
					ColorUtil.replaceColors("&3Shift dropping a jukebox, becomes a Getto Blaster"),
					ColorUtil.replaceColors("&9Switching it to your offhand, becomes a Walkman"),
					ColorUtil.replaceColors("&3Radio mode automaticly plays the next song"))
				.build(), mp, slot);
		this.trackManager = trackManager;
	}

	@Override
	public void doAction(Player player, Container container, ClickType clickType) {
		if (!(getMusicPlayer() instanceof Lockable && ((Lockable) getMusicPlayer()).isLocked())
				|| player.hasPermission("iMine.jukebox.lockbypass")) {
			getMusicPlayer().playRandomTrack(trackManager.getTracks());
			player.closeInventory();
		}
	}

}
