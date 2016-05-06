package nl.imine.soundofnoteblocks.view.button;

import org.bukkit.inventory.ItemStack;

import nl.imine.api.gui.Button;
import nl.imine.soundofnoteblocks.model.MusicPlayer;

public class ButtonMusicPlayer extends Button {

	private MusicPlayer mp;

	public ButtonMusicPlayer(ItemStack is, MusicPlayer mp, int slot) {
		super(is, slot);
		this.mp = mp;
	}

	public MusicPlayer getMusicPlayer() {
		return mp;
	}

}
