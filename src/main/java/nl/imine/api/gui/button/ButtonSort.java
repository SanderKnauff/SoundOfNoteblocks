package nl.imine.api.gui.button;

import nl.imine.api.event.ContainerBuildInventoryEvent;
import nl.imine.api.gui.Button;
import nl.imine.api.gui.Container;
import nl.imine.api.gui.InventorySorter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ButtonSort extends Button {

	private final InventorySorter[] sorters;
	private int page;

	public ButtonSort(ItemStack is, int slot) {
		this(is, slot, new InventorySorter("On name"));
	}

	public ButtonSort(ItemStack is, int slot, InventorySorter... sorters) {
		super(is, slot);
		this.sorters = sorters;
		this.page = 0;
	}

	@Override
	public ItemStack getItemStack() {
		ItemStack is = super.getItemStack();
		ItemMeta im = is.getItemMeta();
		im.setLore(getInfo());
		is.setItemMeta(im);
		return is;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public InventorySorter[] getSorters() {
		return sorters;
	}

	public List<String> getInfo() {
		int page = getPage();
		InventorySorter[] sorters = getSorters();
		List<String> ret = new ArrayList<>();
		if (sorters[page] != null) {
			ret.add(ChatColor.YELLOW + sorters[page].getName());
		} else {
			ret.add("&8Null");
		}
		ret.add("");
		ret.add("" + ChatColor.YELLOW + ChatColor.ITALIC + "LClick " + ChatColor.YELLOW + ChatColor.ITALIC + "lower page");
		ret.add("" + ChatColor.YELLOW + ChatColor.ITALIC + "RClick " + ChatColor.YELLOW + ChatColor.ITALIC + "higher page");
		ret.add(ChatColor.GRAY + "Current page: " + ChatColor.RED + (page + 1) + ChatColor.GRAY + "/" + ChatColor.RED + (sorters.length) + ChatColor.GRAY + ".");
		return ret;
	}

	@Override
	public void onRefresh(ContainerBuildInventoryEvent event) {
		int page = getPage();
		InventorySorter[] sorters = getSorters();
		if (event.getContainer().hasButton(this)) {
			if (sorters[page] != null) {
				event.getContainer().setSorter(sorters[page]);
			}
		}
	}

	@Override
	public void doAction(Player player, Container container, ClickType clickType) {
		if (clickType.isLeftClick()) {
			setPage(Math.max(0, getPage() - 1));
		} else if (clickType.isRightClick()) {
			setPage(Math.min(getSorters().length - 1, getPage() + 1));
		}
		container.refresh();
	}
}
