package nl.imine.api.event;

import org.bukkit.event.HandlerList;

import nl.imine.api.gui.Container;

public class ContainerBuildInventoryEvent extends ContainerEvent {

	private static final HandlerList HANDLERS = new HandlerList();

	public ContainerBuildInventoryEvent(Container container) {
		super(container);
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
}
