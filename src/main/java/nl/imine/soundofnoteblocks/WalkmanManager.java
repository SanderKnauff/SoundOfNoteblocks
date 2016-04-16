package nl.imine.soundofnoteblocks;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;

public class WalkmanManager {
	private static final ArrayList<Walkman> walkmanList = new ArrayList<>();

	public static Walkman findWalkman(Player player) {
		for (Walkman w : WalkmanManager.walkmanList) {
			if (w.getPlayer().getUniqueId().equals(player.getUniqueId())) {
				return w;
			}
		}
		Walkman w = new Walkman(player);
		WalkmanManager.walkmanList.add(w);
		return w;
	}

	public static void removeWalkman(Walkman walkman) {
		walkman.stopPlaying();
		WalkmanManager.walkmanList.remove(walkman);
	}

	public static List<Walkman> getWalkmans() {
		return WalkmanManager.walkmanList;
	}

}
