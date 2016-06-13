package nl.imine.soundofnoteblocks.view.button;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import nl.imine.api.gui.Container;
import nl.imine.api.util.ColorUtil;
import nl.imine.api.util.ItemUtil;
import nl.imine.soundofnoteblocks.model.MusicPlayer;
import nl.imine.soundofnoteblocks.model.design.Lockable;

public class ButtonRadiomode extends ButtonMusicPlayer {

	public ButtonRadiomode(MusicPlayer mp, int slot) {
		super(ItemUtil.getBuilder(Material.ARMOR_STAND).setName("Radio").build(), mp, slot);
	}

	@Override
	public ItemStack getItemStack() {
		return ItemUtil.getBuilder(super.getItemStack().getType(), super.getItemStack().getItemMeta())
				.addLore(ColorUtil
						.replaceColors("&7RadioMode: " + (getMusicPlayer().isRadioMode() ? "&aEnabled" : "&cDisabled")))
				.build();
	}

	@Override
	public void doAction(Player player, Container container, ClickType clickType) {
		if (!(getMusicPlayer() instanceof Lockable && ((Lockable) getMusicPlayer()).isLocked())
				|| player.hasPermission("iMine.jukebox.lockbypass")) {
			getMusicPlayer().setRadioMode(!getMusicPlayer().isRadioMode());
			player.closeInventory();
		}
	}
}
