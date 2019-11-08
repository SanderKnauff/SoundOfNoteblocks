package nl.imine.api.gui.button;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import nl.imine.api.gui.Button;
import nl.imine.api.gui.Container;

public class ButtonList extends Button {

    private final List<String> info;
    private int infoPage;
    private int page;

    public ButtonList(ItemStack is, List<String> info, int slot) {
        super(is, slot);
        this.info = info;
        this.page = 0;
        this.infoPage = 10;
    }

    @Override
    public ItemStack getItemStack() {
        ItemStack is = super.getItemStack();
        ItemMeta im = is.getItemMeta();
        im.setLore(getInfoPage());
        is.setItemMeta(im);
        return is;
    }

    public List<String> getInfo() {
        return info;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getItemsPage() {
        return infoPage;
    }

    public void setItemsPage(int infoPage) {
        this.infoPage = infoPage;
    }

    public List<String> getInfoPage() {
        List<String> ret = new ArrayList<>();
        int page = getPage();
        int infoPage = getItemsPage();
        List<String> info = getInfo();
        Iterator<String> it = info.listIterator(Math.min(page * infoPage, info.size()));
        int max = 10;
        while (it.hasNext() && max-- > 0) {
            ret.add(it.next());
        }
        ret.add("");
        ret.add("" + ChatColor.YELLOW + ChatColor.ITALIC + "LClick " + ChatColor.YELLOW + ChatColor.ITALIC + "lower page");
        ret.add("" + ChatColor.YELLOW + ChatColor.ITALIC + "RClick " + ChatColor.YELLOW + ChatColor.ITALIC + "higher page");
        ret.add(ChatColor.GRAY + "Current page: " + ChatColor.RED + (page + 1) + ChatColor.GRAY + "/" + ChatColor.RED + ((info.size() / infoPage) + 1) + ChatColor.GRAY + ".");
        return ret;
    }

    @Override
    public void doAction(Player player, Container container, ClickType clickType) {
        if (clickType.isLeftClick()) {
            setPage(Math.max(0, getPage() - 1));
        } else if (clickType.isRightClick()) {
            setPage(Math.min(getInfo().size() / getItemsPage(), getPage() + 1));
        }
        container.refresh();
    }
}
