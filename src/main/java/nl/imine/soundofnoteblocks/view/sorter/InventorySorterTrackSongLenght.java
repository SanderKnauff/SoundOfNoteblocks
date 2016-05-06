package nl.imine.soundofnoteblocks.view.sorter;

import com.xxmicloxx.NoteBlockAPI.Song;

import nl.imine.api.gui.Button;
import nl.imine.api.gui.InventorySorter;
import nl.imine.soundofnoteblocks.view.button.ButtonTrack;

public class InventorySorterTrackSongLenght extends InventorySorter {

	public InventorySorterTrackSongLenght() {
		super("On length");
	}

	@Override
	public int compare(Button o1, Button o2) {
		if (o1 instanceof ButtonTrack && o2 instanceof ButtonTrack) {
			Song s1 = ((ButtonTrack) o1).getTrack().getSong();
			int ds1 = (int) (s1.getLength() / s1.getSpeed());
			Song s2 = ((ButtonTrack) o2).getTrack().getSong();
			int ds2 = (int) (s2.getLength() / s2.getSpeed());
			return ds1 - ds2;
		}
		return 1000;
	}
}
