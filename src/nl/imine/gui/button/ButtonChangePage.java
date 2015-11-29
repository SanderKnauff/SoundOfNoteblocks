package nl.imine.gui.button;

import java.util.ArrayList;
import nl.imine.gui.Button;
import nl.imine.gui.Container;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 *
 * @author Sander
 */
public class ButtonChangePage extends Button {

    final private int currentPage;
    final private BrowseDirection browseDirection;

    public ButtonChangePage(Container container, int slot, int currentPage, BrowseDirection browseDirection) {
        super(container, browseDirection.getMaterial(), browseDirection.getTitle(), slot);
        this.currentPage = currentPage;
        this.browseDirection = browseDirection;
    }

    public ButtonChangePage(Container container, int slot, ArrayList<String> subtext, int currentPage, BrowseDirection browseDirection) {
        super(container, browseDirection.getMaterial(), browseDirection.getTitle(), slot, subtext);
        this.currentPage = currentPage;
        this.browseDirection = browseDirection;
    }

    public ButtonChangePage(Container container, int slot, String subtext, int currentPage, BrowseDirection browseDirection) {
        super(container, browseDirection.getMaterial(), browseDirection.getTitle(), slot, subtext);
        this.currentPage = currentPage;
        this.browseDirection = browseDirection;
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    @Override
    public void doAction(Player player) {
        container.setOpenPage(currentPage + browseDirection.getAmount());
        container.open(player, currentPage + browseDirection.getAmount());
    }
}
