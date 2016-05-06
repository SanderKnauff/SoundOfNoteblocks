package nl.imine.soundofnoteblocks.view.button;

import org.bukkit.Material;

import nl.imine.api.gui.InventorySorter;
import nl.imine.api.gui.button.ButtonSort;
import nl.imine.api.util.ColorUtil;
import nl.imine.api.util.ItemUtil;
import nl.imine.soundofnoteblocks.view.sorter.InventorySorterTrackArtist;
import nl.imine.soundofnoteblocks.view.sorter.InventorySorterTrackName;
import nl.imine.soundofnoteblocks.view.sorter.InventorySorterTrackSongLenght;

public class ButtonMusicSort extends ButtonSort {

	public ButtonMusicSort(int slot) {
		super(ItemUtil.getBuilder(Material.SIGN).setName(ColorUtil.replaceColors("&6Sort on")).build(), slot,
				new InventorySorter[]{new InventorySorterTrackName(), new InventorySorterTrackArtist(),
						new InventorySorterTrackSongLenght()});
	}

}
