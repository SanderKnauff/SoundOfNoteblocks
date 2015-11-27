package nl.imine.gui;

import java.util.ArrayList;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author Sansko1337
 */
public class GuiManager {

    private static GuiManager manager;

    private final ArrayList<nl.imine.gui.Container> containers = new ArrayList<>();

    public static void init(Plugin plugin) {
        GuiManager.manager = new GuiManager();
        GuiListener.init(plugin);
    }

    public static GuiManager getInstance() {
        return manager;
    }

    public Container createContainer(String name, int size) {
        Container container = new AContainer(name, size);
        containers.add(container);
        return container;
    }

    public boolean isGuiInventory(Inventory inv) {
        for (nl.imine.gui.Container c : containers) {
            for (Inventory i : c.getOpenInventories()) {
                if (i.equals(inv)) {
                    return true;
                }
            }
        }
        return false;
    }

    public nl.imine.gui.Container getInventoryContainer(Inventory inv) {
        for (nl.imine.gui.Container c : containers) {
            for (Inventory i : c.getOpenInventories()) {
                if (i.equals(inv)) {
                    return c;
                }
            }
        }
        return null;
    }

    private GuiManager() {

    }

    private class AContainer extends Container {

        public AContainer(String title) {
            super(title);
        }

        public AContainer(String title, int maxSize) {
            super(title, maxSize);
        }

        public AContainer(String title, int maxSize, boolean autoResize) {
            super(title, maxSize, autoResize);
        }
    }
}
