package nl.imine.soundofnoteblocks.view.button;

import nl.imine.api.gui.button.ButtonSort;
import nl.imine.api.util.ColorUtil;
import nl.imine.api.util.ItemUtil;
import nl.imine.soundofnoteblocks.view.sorter.InventorySorterTrackArtist;
import nl.imine.soundofnoteblocks.view.sorter.InventorySorterTrackName;
import nl.imine.soundofnoteblocks.view.sorter.InventorySorterTrackSongLength;
import org.bukkit.Material;

public class ButtonMusicSort extends ButtonSort {

    public ButtonMusicSort(int slot) {
        super(
                ItemUtil
                        .getBuilder(Material.OAK_SIGN)
                        .setName(ColorUtil.replaceColors("&6Sort by:"))
                        .build(),
                slot,
                new InventorySorterTrackName(),
                new InventorySorterTrackArtist(),
                new InventorySorterTrackSongLength()
        );
    }

}
