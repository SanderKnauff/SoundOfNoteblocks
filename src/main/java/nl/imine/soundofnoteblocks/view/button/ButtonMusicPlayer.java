package nl.imine.soundofnoteblocks.view.button;

import nl.imine.api.gui.Button;
import nl.imine.soundofnoteblocks.model.MusicPlayer;
import org.bukkit.inventory.ItemStack;

public abstract class ButtonMusicPlayer extends Button {

	private MusicPlayer mp;

	public ButtonMusicPlayer(ItemStack is, MusicPlayer mp, int slot) {
		super(is, slot);
		this.mp = mp;
	}

	public MusicPlayer getMusicPlayer() {
		return mp;
	}

}
