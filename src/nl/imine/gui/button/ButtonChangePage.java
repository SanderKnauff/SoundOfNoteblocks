package nl.imine.gui.button;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import nl.imine.gui.Button;
import nl.imine.gui.Container;

/**
 *
 * @author Sander
 */
public class ButtonChangePage extends Button {

    private final BrowseDirection browseDirection;

    public ButtonChangePage(Container container, int slot, BrowseDirection browseDirection) {
        super(container, browseDirection.getMaterial(), browseDirection.getTitle(), slot);
        this.browseDirection = browseDirection;
    }

    public ButtonChangePage(Container container, int slot, ArrayList<String> subtext, BrowseDirection browseDirection) {
        super(container, browseDirection.getMaterial(), browseDirection.getTitle(), slot, subtext);
        this.browseDirection = browseDirection;
    }

    public ButtonChangePage(Container container, int slot, String subtext, BrowseDirection browseDirection) {
        super(container, browseDirection.getMaterial(), browseDirection.getTitle(), slot, subtext);
        this.browseDirection = browseDirection;
    }

    @Override
    public ItemStack getItemStack() {
        ItemStack is = super.getItemStack();
        if (browseDirection == BrowseDirection.PREVIOUS) {
            if (container.getOpenPage() == 0) {
                is.setType(Material.BARRIER);
            } else {
                is.setType(browseDirection.getMaterial());
            }
        } else if (browseDirection == BrowseDirection.NEXT) {
            if (container.getOpenPage() == container.getPageAmount()) {
                is.setType(Material.BARRIER);
            } else {
                is.setType(browseDirection.getMaterial());
            }
        }
        return is;
    }

    @Override
    public void doAction(Player player) {
        int amount = 0;
        if (browseDirection == BrowseDirection.PREVIOUS) {
            if (container.getOpenPage() != 0) {
                amount = browseDirection.getAmount();
            }
        } else if (browseDirection == BrowseDirection.NEXT) {
            if (container.getOpenPage() != container.getPageAmount()) {
                amount = browseDirection.getAmount();
            }
        }
        container.open(player, container.getOpenPage() + amount);
    }
}
