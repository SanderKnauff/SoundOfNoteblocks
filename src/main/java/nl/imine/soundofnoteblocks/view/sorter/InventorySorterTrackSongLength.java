package nl.imine.soundofnoteblocks.view.sorter;

import com.xxmicloxx.NoteBlockAPI.model.Song;
import nl.imine.api.gui.Button;
import nl.imine.api.gui.InventorySorter;
import nl.imine.soundofnoteblocks.view.button.ButtonTrack;

public class InventorySorterTrackSongLength extends InventorySorter {

	public InventorySorterTrackSongLength() {
		super("Length");
	}

	@Override
	public int compare(Button o1, Button o2) {
		if (o1 instanceof ButtonTrack firstTrack && o2 instanceof ButtonTrack secondTrack) {
			Song s1 = firstTrack.getTrack().song();
			int ds1 = (int) (s1.getLength() / s1.getSpeed());
			Song s2 = secondTrack.getTrack().song();
			int ds2 = (int) (s2.getLength() / s2.getSpeed());
			return ds1 - ds2;
		}
		return 1000;
	}
}
