package nl.imine.api.holotag;

import org.bukkit.Location;

public interface ITag {
	@Deprecated
	public void setLine(String line, int row);

	public void setLine(int row, String line);

	public void addLine(String line);

	public void removeLine(int row);

	public void removeLine(ILine line);

	public ILine getLine(int row);

	public ILine[] getLines();

	public void setLineDistance(double distance);

	public double getLineDistance();

	public void setLocation(Location location);

	public Location getLocation();

	public void setVisible(boolean visible);

	public boolean isVisible();

	public void remove();

}
