package nl.imine.api.event;

import org.bukkit.event.HandlerList;

import nl.imine.api.gui.Container;

public class ContainerUpdateEvent extends ContainerEvent {

	private static final HandlerList HANDLERS = new HandlerList();

	public ContainerUpdateEvent(Container container) {
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
