package nl.imine.soundofnoteblocks.model.design;

import org.bukkit.Location;

import nl.imine.api.holotag.ITag;

public interface Tagable {

	public Location getTagLocation();

	public ITag getTag();

	public void setVisible(boolean visible);

	public boolean isVisible();

	public void setTagLines(String... lines);
}
