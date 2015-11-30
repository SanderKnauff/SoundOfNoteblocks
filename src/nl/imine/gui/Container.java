package nl.imine.gui;

import java.util.ArrayList;
import nl.imine.gui.button.BrowseDirection;
import nl.imine.gui.button.ButtonChangePage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 *
 * @author Sansko1337
 */
public abstract class Container {

    private final ArrayList<Inventory> openInvs = new ArrayList<>();

    private final ArrayList<Button> buttons = new ArrayList<>();

    private final String title;

    private boolean autoResize = false;
    private int maxScreenSize = 0;
    
    private int openPage = 0;

    public Container(String title) {
        this.title = title;
        this.autoResize = true;
        this.maxScreenSize = 54;
    }

    public Container(String title, int maxSize) {
        this(title);
        this.maxScreenSize = maxSize;
        this.autoResize = false;
    }

    public Container(String title, int maxSize, boolean autoResize) {
        this(title, maxSize);
        this.autoResize = autoResize;
    }

    public void open(Player player, int page) {
        for (Button b : (ArrayList<Button>) buttons.clone()) {
            if (b instanceof ButtonChangePage) {
                buttons.remove(b);
            }
        }
        Inventory inv;
        if (buttons.size() <= maxScreenSize) {
            inv = Bukkit.createInventory(null, getInventorySize(maxScreenSize), title);
        } else {
            inv = Bukkit.createInventory(null, getInventorySize(maxScreenSize) + 9, title);
            Button back = new ButtonChangePage(this, getLastRowSlot((maxScreenSize * page) + 3), page, page == 0 ? BrowseDirection.BLOCKED : BrowseDirection.PREVIOUS);
            inv.setItem(back.getSlot() - (maxScreenSize * page), back.getItemStack());
            buttons.add(back);
            Button next = new ButtonChangePage(this, getLastRowSlot((maxScreenSize * page) + 5), page, page == getPageAmount() ? BrowseDirection.BLOCKED : BrowseDirection.NEXT);
            inv.setItem(next.getSlot() - (maxScreenSize * page), next.getItemStack());
            buttons.add(next);
        }
        for (int i = ((page) * maxScreenSize); i < ((page + 1) * maxScreenSize); i++) {
            if (i < buttons.size()) {
                Button b = buttons.get(i);
                if (!(b instanceof ButtonChangePage)) {
                    inv.setItem(i - ((page) * maxScreenSize), b.getItemStack());
                }
            }
        }
        openInvs.add(inv);
        player.openInventory(inv);
    }

    public void open(Player player) {
        this.open(player, 0);
    }

    public Button getButton(int slot, int page) {
        int id = slot + (maxScreenSize * page);
        for (Button b : buttons) {
            if (b.getSlot() == id) {
                return b;
            }
        }
        return null;
    }

    public Button getPageButton(int slot, int page) {
        int id = slot + (maxScreenSize * page);
        for (Button b : buttons) {
            if (b instanceof ButtonChangePage) {
                if (b.getSlot() == id) {
                    return b;
                }
            }
        }
        return null;
    }

    public ArrayList<Inventory> getOpenInventories() {
        return openInvs;
    }

    public void removeInventory(Inventory inv) {
        openInvs.remove(inv);
    }

    public void addButton(Button button) {
        buttons.add(button);
    }

    public ArrayList<Button> getButtons() {
        return buttons;
    }

    public String getTitle() {
        return title;
    }

    public int getFreeSlot() {
        ArrayList<Integer> nr = new ArrayList<>();
        for (int i = 0; i <= buttons.size(); i++) {
            nr.add(i);
        }
        int maxSize = nr.size();
        for (Button b : this.getButtons()) {
            if (b.getSlot() < maxSize) {
                nr.remove((Integer) b.getSlot());
            }
        }
        if (nr.isEmpty()) {
            return 0;
        }
        return (nr.get(0) == null ? 0 : nr.get(0));
    }

    public void setMaxScreenSize(int maxScreenSize) {
        this.maxScreenSize = maxScreenSize;
    }

    public int getMaxScreenSize() {
        return this.maxScreenSize;
    }

    public int getPageAmount() {
        return ((int) ((buttons.size() - 1) / maxScreenSize));
    }

    public int getPageSlot(int slot, int page){
        return slot + (page * this.getMaxScreenSize());
    }
    
    public int getLastRowSlot(int slot) {
        return (maxScreenSize) + slot;
    }

    public void setOpenPage(int openPage) {
        this.openPage = openPage;
    }

    public int getOpenPage() {
        return openPage;
    }

    public static int getInventorySize(int maxSize) {
        int size = 9;
        size = ((double) maxSize < 45) ? ((int) ((maxSize - 1) / 9) + 1) * 9 : 45;
        return size;
    }

    @Override
    public String toString() {
        return "Container:{Title:" + title + ",MaxSize:" + maxScreenSize + ",AutoResize:" + autoResize + "}";
    }
}
