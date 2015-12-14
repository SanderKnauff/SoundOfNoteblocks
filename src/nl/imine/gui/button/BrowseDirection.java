package nl.imine.gui.button;

import org.bukkit.Material;

/**
 *
 * @author Sander
 */
public enum BrowseDirection {

    PREVIOUS("Previous", -1, Material.STONE_BUTTON),
    NEXT("Next", 1, Material.STONE_BUTTON);

    final String title;
    final int amount;
    final Material material;

    BrowseDirection(String title, int amount, Material material) {
        this.title = title;
        this.amount = amount;
        this.material = material;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }    
    
    /**
     * @return the amount
     */
    public int getAmount() {
        return amount;
    }

    /**
     * @return the material
     */
    public Material getMaterial() {
        return material;
    }
}
