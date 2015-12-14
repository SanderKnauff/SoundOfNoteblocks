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
    
    public ArrayList<nl.imine.gui.Container> getContainers() {
        return containers;
    }

    public Container createContainer(String name) {
        Container container = new GenericContainer(name);
        containers.add(container);
        return container;
    }

    public Container createContainer(String name, int size) {
        Container container = new GenericContainer(name, size);
        containers.add(container);
        return container;
    }

    public Container createContainer(String name, int size, boolean autoResize) {
        Container container = new GenericContainer(name, size, autoResize);
        containers.add(container);
        return container;
    }

    public Container createContainer(String name, int size, boolean autoResize, boolean defaultButtons) {
        Container container = new GenericContainer(name, size, autoResize, defaultButtons);
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

    private class GenericContainer extends Container {

        public GenericContainer(String title) {
            super(title);
        }

        public GenericContainer(String title, int maxSize) {
            super(title, maxSize);
        }

        public GenericContainer(String title, int maxSize, boolean autoResize) {
            super(title, maxSize, autoResize);
        }

        public GenericContainer(String title, int maxSize, boolean autoResize, boolean defaultButtons) {
            super(title, maxSize, autoResize, defaultButtons);
        }
    }
}
