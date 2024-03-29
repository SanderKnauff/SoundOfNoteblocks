package nl.imine.soundofnoteblocks.view.button;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import nl.imine.api.gui.Container;
import nl.imine.api.util.ColorUtil;
import nl.imine.api.util.ItemUtil;
import nl.imine.soundofnoteblocks.model.MusicPlayer;
import nl.imine.soundofnoteblocks.model.design.Lockable;
import nl.imine.soundofnoteblocks.model.design.Tagable;

public class ButtonToggleTag extends ButtonMusicPlayer {

	public ButtonToggleTag(MusicPlayer mp, int slot) {
		super(ItemUtil.getBuilder(Material.NAME_TAG).setName(ColorUtil.replaceColors("&eToggle Currentsong Nametag"))
				.build(), mp, slot);
	}

	@Override
	public void doAction(Player player, Container container, ClickType clickType) {
		if (getMusicPlayer().isPlaying() && getMusicPlayer() instanceof Tagable) {
			if (!(getMusicPlayer() instanceof Lockable && ((Lockable) getMusicPlayer()).isLocked())
					|| player.hasPermission("iMine.jukebox.lockbypass")) {
				Tagable tagMP = (Tagable) getMusicPlayer();
				tagMP.setVisible(!tagMP.isVisible());
				tagMP.getTag().setLocation(tagMP.getTagLocation());
			}
		}
	}

}
