package nl.imine.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author Sansko1337
 */
public class GuiListener implements Listener {

    public static void init(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(new GuiListener(), plugin);
    }

    @EventHandler
    public void onPlayerInventoryClick(InventoryClickEvent evt) {
        if (GuiManager.getInstance().isGuiInventory(evt.getClickedInventory())) {
            Container container = GuiManager.getInstance().getInventoryContainer(evt.getClickedInventory());
            if (container.getButtons().size() > container.getMaxScreenSize()) {
                if (evt.getSlot() == container.getLastRowSlot(3) || evt.getSlot() == container.getLastRowSlot(5)) {
                    Button button = container.getPageButton(evt.getSlot(), container.getOpenPage());
                    button.doAction((Player) evt.getWhoClicked());
                    evt.setCancelled(true);
                    return;
                }
            }
            if (container.getButton(evt.getSlot(), container.getOpenPage()) != null) {
                if ((container.getPageSlot(evt.getSlot(), container.getOpenPage()) >= container.getOpenPage() * container.getMaxScreenSize())
                        && evt.getSlot() + (container.getOpenPage() * container.getMaxScreenSize()) <= (((container.getOpenPage() + 1) * container.getMaxScreenSize()) - 1)) {
                    Button button = container.getButton(evt.getSlot(), container.getOpenPage());
                    button.doAction((Player) evt.getWhoClicked());
                }
            }
            evt.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInventoryClose(InventoryCloseEvent evt) {
        if (GuiManager.getInstance().isGuiInventory(evt.getInventory())) {
            GuiManager.getInstance().getInventoryContainer(evt.getInventory()).removeInventory(evt.getInventory());
        }
    }
}
