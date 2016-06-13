package nl.imine.soundofnoteblocks.view.button;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import nl.imine.api.gui.Container;
import nl.imine.api.util.ColorUtil;
import nl.imine.api.util.ItemUtil;
import nl.imine.soundofnoteblocks.model.MusicPlayer;
import nl.imine.soundofnoteblocks.model.design.Lockable;

public class ButtonStop extends ButtonMusicPlayer {
	public ButtonStop(MusicPlayer mp, int slot) {
		super(ItemUtil.getBuilder(Material.APPLE).setName(ColorUtil.replaceColors("&4Stop"))
				.setLore(ColorUtil.replaceColors("&7Stop current song")).build(), mp, slot);
	}

	@Override
	public void doAction(Player player, Container container, ClickType clickType) {
		if (!(getMusicPlayer() instanceof Lockable && ((Lockable) getMusicPlayer()).isLocked())
				|| player.hasPermission("iMine.jukebox.lockbypass")) {
			getMusicPlayer().stopPlaying();
		}
	}
}
