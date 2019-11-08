package nl.imine.api.holotag;

import java.util.UUID;
import org.bukkit.Location;

public interface ILine {

	public void setLabel(String label);

	public String getLabel();

	public void setVisible(boolean visible);

	public void setVisible(boolean visible, boolean remember);

	public boolean isVisible();

	public void setLocation(Location location);

	public Location getLocation();

	public UUID getUniqueId();

	public void removeArmorStand();
}
