package nl.imine.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import nl.imine.api.holotag.ActionType;
import nl.imine.api.holotag.ITag;

public class PlayerInteractTagEvent extends PlayerEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private final ITag tag;
	private final ActionType action;
	private boolean cancelled;

	public PlayerInteractTagEvent(Player player, ITag tag, ActionType action) {
		super(player);
		this.player = player;
		this.tag = tag;
		this.action = action;
		this.cancelled = false;
	}

	public ITag getTag() {
		return tag;
	}

	public ActionType getAction() {
		return action;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
