package nl.imine.soundofnoteblocks.model.design;

import org.bukkit.Location;

import nl.imine.api.holotag.ITag;

public interface Tagable {

	Location getTagLocation();

	ITag getTag();

	void setVisible(boolean visible);

	boolean isVisible();

	void setTagLines(String... lines);
}
