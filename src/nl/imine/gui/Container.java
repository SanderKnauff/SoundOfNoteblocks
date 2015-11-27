package nl.imine.gui;

import java.util.ArrayList;
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
        Inventory inv;
        if (buttons.size() <= maxScreenSize) {
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
        openInvs.add(inv);
        player.openInventory(inv);
    }

    public void open(Player player) {
        this.open(player, 0);
    }

    public void update() {
        for (Inventory i : openInvs) {
            i.clear();
            for (Button b : buttons) {
                i.setItem(b.getSlot(), b.getItemStack());
            }
        }
    }

    public Button getButton(int slot) {
        for (Button b : buttons) {
            if (b.getSlot() == slot) {
                return b;
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

    public static int getInventorySize(int maxSize) {
        int size = 9;
        size = ((double) maxSize < 45) ? ((int) ((maxSize - 1) / 9) + 1) * 9 : 45;
        return size;
    }
    
    public static int getLastRow(int maxSize) {
        int size = 9;
        size = ((double) maxSize < 45) ? ((int) ((maxSize - 1) / 9) + 1) * 9 : 45;
        return size;
    }

    @Override
    public String toString(){
        return "Container:{Title:" + title + ",MaxSize:" + maxScreenSize + ",AutoResize:" + autoResize + "}";
    }
}
