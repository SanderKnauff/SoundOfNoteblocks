package nl.imine.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import nl.imine.gui.button.BrowseDirection;
import nl.imine.gui.button.ButtonChangePage;

/**
 *
 * @author Sansko1337
 */
public abstract class Container {

    private final List<Inventory> openInvs = new ArrayList<>();

    private final ArrayList<Button> buttons = new ArrayList<>();
    private final ArrayList<Button> staticButtons = new ArrayList<>();

    private final String title;

    private boolean autoResize;
    private int maxScreenSize;
    private int openPage = 0;

    public Container(String title) {
        this(title, 9);
    }

    public Container(String title, int maxSize) {
        this(title, maxSize, true);
    }

    public Container(String title, int maxSize, boolean autoResize) {
        this(title, maxSize, autoResize, true);
    }

    public Container(String title, int maxSize, boolean autoResize, boolean defaultButtons) {
        this.title = title;
        this.maxScreenSize = maxSize;
        this.autoResize = autoResize;
        if (defaultButtons) {
            staticButtons.add(getDefaultPreviousButton(this));
            staticButtons.add(getDefaultNextButton(this));
        }
    }

    public void open(Player player, int page) {
        openPage = page;

        Inventory inv;
        for (Button b : (List<Button>) buttons.clone()) {
            if (b instanceof ButtonChangePage) {
                buttons.remove(b);
            }
        }
        if (buttons.size() < maxScreenSize && staticButtons.isEmpty()) {
            inv = Bukkit.createInventory(null, getInventorySize(maxScreenSize), title);
        } else {
            inv = Bukkit.createInventory(null, getInventorySize(maxScreenSize) + 9, title);
        }
        for (int i = ((page) * maxScreenSize); i < ((page + 1) * maxScreenSize); i++) {
            if (i < buttons.size()) {
                Button b = buttons.get(i);
                inv.setItem(i - ((page) * maxScreenSize), b.getItemStack());
            }
        }
        for (Button staticButton : staticButtons) {
            inv.setItem(getLastRowSlot(Math.abs(staticButton.getSlot() % 9)), staticButton.getItemStack());
        }
        openInvs.add(inv);
        player.openInventory(inv);
    }

    public void open(Player player) {
        this.open(player, 0);
    }

    public void close() {
        for (Player pl : Bukkit.getOnlinePlayers()) {
            for (Inventory inv : openInvs) {
                if (pl.getInventory().equals(inv) || pl.getOpenInventory().equals(inv)) {
                    pl.closeInventory();
                }
            }
        }
    }

    public Button getButton(int slot, int page) {
        if (slot > maxScreenSize) {
            for (Button staticButton : staticButtons) {
                if (staticButton.getSlot() == (slot - maxScreenSize)) {
                    return staticButton;
                }
            }
        }
        int id = slot + (maxScreenSize * page);
        for (Button b : buttons) {
            if (b.getSlot() == id) {
                return b;
            }
        }
        return null;
    }

    public Button getStaticButton(int slot) {
        return staticButtons.get(slot);
    }

    public List<Inventory> getOpenInventories() {
        return openInvs;
    }

    public void removeInventory(Inventory inv) {
        openInvs.remove(inv);
    }

    public void addButton(Button button) {
        buttons.add(button);
    }

    public void addStaticButton(Button button) {
        staticButtons.add(button);
    }

    public List<Button> getButtons() {
        return buttons;
    }

    public List<Button> getStaticButtons() {
        return staticButtons;
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

    public int getPageSlot(int slot, int page) {
        return slot + (page * this.getMaxScreenSize());
    }

    public int getLastRowSlot(int slot) {
        return (maxScreenSize) + slot;
    }

    public int getOpenPage() {
        return openPage;
    }

    public static int getInventorySize(int maxSize) {
        int size = 9;
        size = ((double) maxSize < 45) ? ((int) ((maxSize - 1) / 9) + 1) * 9 : 45;
        return size;
    }

    public static Button getDefaultPreviousButton(Container c) {
        return new ButtonChangePage(c, 3, BrowseDirection.PREVIOUS);
    }

    public static Button getDefaultNextButton(Container c) {
        return new ButtonChangePage(c, 5, BrowseDirection.NEXT);
    }

    @Override
    public String toString() {
        return "Container:{Title:" + title + ",MaxSize:" + maxScreenSize + ",AutoResize:" + autoResize + "}";
    }
}
