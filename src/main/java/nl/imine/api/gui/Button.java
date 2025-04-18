package nl.imine.api.gui;

import nl.imine.api.event.ContainerBuildInventoryEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Button {
	protected ItemStack itemStack;
	protected int slot;

	public Button(ItemStack is, int slot) {
		this.itemStack = is;
		this.slot = slot;
	}

	public ItemStack getItemStack() {
		ItemMeta meta = itemStack.getItemMeta();
		if (meta == null) {
			return itemStack;
		}
		meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP, ItemFlag.HIDE_ATTRIBUTES);
		itemStack.setItemMeta(meta);
		return itemStack;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return itemStack.getItemMeta().getDisplayName();
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		ItemMeta im = itemStack.getItemMeta();
		im.setDisplayName(name);
		itemStack.setItemMeta(im);
	}

	/**
	 * @return the subtext
	 */
	public List<String> getSubtext() {
		return itemStack.getItemMeta().getLore();
	}

	/**
	 * @param subtext
	 *            the subtext to set
	 */
	public void setSubtext(List<String> subtext) {
		ItemMeta im = itemStack.getItemMeta();
		im.setLore(subtext);
		itemStack.setItemMeta(im);
	}

	/**
	 * @return the slot
	 */
	public int getSlot() {
		return slot;
	}

	/**
	 * @param slot
	 *            the slot to set
	 */
	public Button setSlot(int slot) {
		this.slot = slot;
		return this;
	}

	public void onRefresh(ContainerBuildInventoryEvent cbie) {
	}

	public void doAction(final Player player, final Container container, final ClickType clickType) {
	}

	@Override
	public String toString() {
		return String.format("{Slot:%d, Item:%s}", slot, itemStack.toString());
	}
}
