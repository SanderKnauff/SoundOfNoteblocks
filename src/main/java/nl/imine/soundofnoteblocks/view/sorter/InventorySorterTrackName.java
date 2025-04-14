package nl.imine.soundofnoteblocks.view.sorter;

import nl.imine.api.gui.Button;
import nl.imine.api.gui.InventorySorter;
import nl.imine.soundofnoteblocks.model.Track;
import nl.imine.soundofnoteblocks.view.button.ButtonTrack;

public class InventorySorterTrackName extends InventorySorter {
	public InventorySorterTrackName() {
		super("Name");
	}

	@Override
	public int compare(Button o1, Button o2) {
		if (o1 instanceof ButtonTrack && o2 instanceof ButtonTrack) {
			Track t1 = ((ButtonTrack) o1).getTrack();
			Track t2 = ((ButtonTrack) o2).getTrack();
			return t1.name().compareTo(t2.name());
		}
		return 1000;
	}
}
