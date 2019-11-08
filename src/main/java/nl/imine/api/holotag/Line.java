package nl.imine.api.holotag;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

public class Line implements ILine {

	private ArmorStand armorStand;
	private String label;
	private Location location;
	private boolean visible;

	public Line(String label, Location location, boolean visible) {
		this.label = label;
		this.location = location;
		this.visible = visible;
		if (this.visible) {
			spawnArmorStand();
		}
	}

	@Override
	public void setLabel(String label) {
		this.label = label;
		if (isVisible()) {
			armorStand.setCustomName(label);
		}
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public void setVisible(boolean newVisible, boolean remember) {
		if (remember) {
			visible = newVisible;
		}
		if (visible) {
			spawnArmorStand();
		} else {
			removeArmorStand();
		}
	}

	@Override
	public void setVisible(boolean visible) {
		setVisible(visible, true);
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public void setLocation(Location location) {
		this.location = location;
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public void removeArmorStand() {
		TagAPI.unregisterLine(this);
		if (armorStand != null) {
			armorStand.setInvulnerable(false);
			armorStand.remove();
			armorStand = null;
		}
	}

	@Override
	public UUID getUniqueId() {
		if (armorStand != null) {
			return armorStand.getUniqueId();
		}
		return null;
	}

	private void spawnArmorStand() {
		if (armorStand != null) {
			return;
		}
		armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
		armorStand.setGravity(false);
		armorStand.setVisible(false);
		armorStand.setCustomNameVisible(true);
		armorStand.setCustomName(label);
		TagAPI.registerLine(this);
	}
}
