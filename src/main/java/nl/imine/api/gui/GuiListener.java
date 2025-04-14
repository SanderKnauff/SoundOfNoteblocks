package nl.imine.api.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.Plugin;

public class GuiListener implements Listener {
    private final Plugin plugin;
    private final GuiManager guiManager;

    public GuiListener(Plugin plugin, GuiManager guiManager) {
        this.plugin = plugin;
        this.guiManager = guiManager;
    }

    @EventHandler
    public void onPlayerInventoryClick(InventoryClickEvent evt) {
        if (guiManager.isGuiInventory(evt.getInventory())) {
            Container container = guiManager.getInventoryContainer(evt.getInventory());
            if (container.getButton(evt.getSlot(), container.getOpenPage()) != null) {
                Button button = container.getButton(evt.getSlot(), container.getOpenPage());
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    try {
                        button.doAction((Player) evt.getWhoClicked(), container, evt.getClick());
                    } catch (Throwable thr) {
                        thr.printStackTrace();
                        evt.getWhoClicked().sendMessage(ChatColor.GRAY + thr.getMessage());
                    }
                });
            }
            evt.setCancelled(true);
        }
        if (guiManager.isGuiInventory(evt.getView().getTopInventory())) {
            if (evt.getInventory().equals(evt.getView().getBottomInventory())) {
                if (evt.getClick().isShiftClick()) {
                    evt.setCancelled(true);
                }
                if (evt.getClick().equals(ClickType.DOUBLE_CLICK)) {
                    evt.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInventoryClose(InventoryCloseEvent evt) {
        if (guiManager.isGuiInventory(evt.getInventory())) {
            Container container = guiManager.getInventoryContainer(evt.getInventory());
            container.removeInventory(evt.getInventory());
            if (container.getOpenInventories().isEmpty()) {
                guiManager.removeContainer(container);
            }
        }
    }
}
