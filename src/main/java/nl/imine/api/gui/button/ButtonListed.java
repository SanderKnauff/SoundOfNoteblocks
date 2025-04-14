package nl.imine.api.gui.button;

import nl.imine.api.gui.Button;
import nl.imine.api.gui.Container;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ButtonListed extends Button {

	private final List<String>[] info;
	private int page;

	public ButtonListed(ItemStack is, List<String>[] info, int slot) {
		super(is, slot);
		this.info = info;
		this.page = 0;
	}

	@Override
	public ItemStack getItemStack() {
		ItemStack is = super.getItemStack();
		ItemMeta im = is.getItemMeta();
		im.setLore(getInfoPaged());
		is.setItemMeta(im);
		return is;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public List<String>[] getInfo() {
		return info;
	}

	public List<String> getInfoPaged() {
		List<String> ret = new ArrayList<>();
		List<String>[] info = getInfo();
		int page = getPage();
		if (info[page] != null) {
			info[page].forEach(str -> ret.add(str));
		} else {
			ret.add("&8Null");
		}
		ret.add("");
		ret.add("" + ChatColor.YELLOW + ChatColor.ITALIC + "LClick " + ChatColor.YELLOW + ChatColor.ITALIC + "lower page");
		ret.add("" + ChatColor.YELLOW + ChatColor.ITALIC + "RClick " + ChatColor.YELLOW + ChatColor.ITALIC + "higher page");
		ret.add(ChatColor.GRAY + "Current page: " + ChatColor.RED + (page + 1) + ChatColor.GRAY + "/" + ChatColor.RED + (info.length) + ChatColor.GRAY + ".");
		return ret;
	}

	@Override
	public void doAction(Player player, Container container, ClickType clickType) {
		if (clickType.isLeftClick()) {
			page = Math.max(0, page - 1);
		} else if (clickType.isRightClick()) {
			page = Math.min(info.length - 1, page + 1);
		}
		container.refresh();
	}
}
