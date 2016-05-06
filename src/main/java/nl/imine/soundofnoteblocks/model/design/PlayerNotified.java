package nl.imine.soundofnoteblocks.model.design;

import java.util.Collection;

import org.bukkit.entity.Player;

import nl.imine.soundofnoteblocks.model.Track;

public interface PlayerNotified {

	public void notifyPlayers(Track track);

	public Collection<Player> getListeners();
}
