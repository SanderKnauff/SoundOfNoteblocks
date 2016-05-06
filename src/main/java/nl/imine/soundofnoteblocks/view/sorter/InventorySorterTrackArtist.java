package nl.imine.soundofnoteblocks.view.sorter;

import nl.imine.api.gui.Button;
import nl.imine.api.gui.InventorySorter;
import nl.imine.soundofnoteblocks.Track;
import nl.imine.soundofnoteblocks.view.button.ButtonTrack;

public class InventorySorterTrackArtist extends InventorySorter {
	public InventorySorterTrackArtist() {
		super("On artist");
	}

	@Override
	public int compare(Button o1, Button o2) {
		if (o1 instanceof ButtonTrack && o2 instanceof ButtonTrack) {
			Track t1 = ((ButtonTrack) o1).getTrack();
			Track t2 = ((ButtonTrack) o2).getTrack();
			return t1.getArtist().compareTo(t2.getArtist());
		}
		return 1000;
	}
}
