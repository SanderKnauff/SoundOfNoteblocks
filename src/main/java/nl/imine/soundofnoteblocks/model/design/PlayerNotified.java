package nl.imine.soundofnoteblocks.model.design;

import nl.imine.soundofnoteblocks.model.Track;
import org.bukkit.entity.Player;

import java.util.Collection;

public interface PlayerNotified {

	void notifyPlayers(Track track);

	Collection<Player> getListeners();
}
