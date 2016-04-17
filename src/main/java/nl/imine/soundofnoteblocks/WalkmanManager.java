package nl.imine.soundofnoteblocks;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;

public class WalkmanManager {
	private static final ArrayList<Walkman> walkmanList = new ArrayList<>();

	public static Walkman findWalkman(Player player) {
		Walkman walkman = null;
		if (SoundOfNoteBlocks.isReady()) {
			for (Walkman w : WalkmanManager.walkmanList) {
				if (w.getPlayer().getUniqueId().equals(player.getUniqueId())) {
					return w;
				}
			}
			walkman = new Walkman(player);
			WalkmanManager.walkmanList.add(walkman);
		}
		return walkman;
	}

	public static void removeWalkman(Walkman walkman) {
		walkman.setRadioMode(false);
		walkman.stopPlaying();
		WalkmanManager.walkmanList.remove(walkman);
	}

	public static List<Walkman> getWalkmans() {
		return WalkmanManager.walkmanList;
	}

}
