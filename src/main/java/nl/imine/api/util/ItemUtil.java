package nl.imine.api.util;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.Colorable;
import org.bukkit.material.MaterialData;
import org.bukkit.material.SpawnEgg;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

public class ItemUtil {

	/**
	 * Creates an ItemStack builder for creating complex itemstacks within one
	 * line. Each child method of builder will return the builder instance back
	 * to be able to keep working on one line.
	 * 
	 * To finish building and exporting to an ItemStack instance, use the
	 * Builder#build() method.
	 * 
	 * @param material
	 *            The Material the Item will be made of.
	 * @return A builder instance for creating ItemStacks.
	 */
	public static Builder getBuilder(Material material) {
		return new Builder(material);
	}

	/**
	 * Creates an ItemStack builder for creating complex itemstacks within one
	 * line. Each child method of builder will return the builder instance back
	 * to be able to keep working on one line.
	 * 
	 * To finish building and exporting to an ItemStack instance, use the
	 * Builder#build() method.
	 * 
	 * @param material
	 *            The Material the Item will be made of.
	 * @param itemMeta
	 *            The ItemMeta which properties should be used as base.
	 * @return A builder instance for creating ItemStacks.
	 */
	public static Builder getBuilder(Material material, ItemMeta itemMeta) {
		return new Builder(material, itemMeta);
	}

	public static class Builder {

		private final ItemStack itemStack;

		private Builder(Material material) {
			itemStack = new ItemStack(material, 1);
		}

		private Builder(Material material, ItemMeta im) {
			itemStack = new ItemStack(material, 1);
			itemStack.setItemMeta(im);
		}

		/**
		 * Set the amount of Items of which ItemStack will contain.
		 * 
		 * @param amount
		 *            The amount of Items the ItemStack will contain.
		 * @return The current ItemStack Builder instance.
		 */
		public Builder setAmmount(int amount) {
			itemStack.setAmount(amount);
			return this;
		}

		/**
		 * Sets the name the ItemStack will recieve on creation.
		 * 
		 * @param name
		 *            The name the Itemstack will have.
		 * @return The current ItemStack Builder instance.
		 */
		public Builder setName(String name) {
			ItemMeta im = itemStack.getItemMeta();
			im.setDisplayName(name);
			itemStack.setItemMeta(im);
			return this;
		}

		/**
		 * Sets the Lore the ItemStack will recieve on creation.
		 * 
		 * @param lore
		 *            The lines of lore the ItemStack will have.
		 * @return The current ItemStack Builder instance.
		 */
		public Builder setLore(String... lore) {
			return setLore(Arrays.asList(lore));
		}

		/**
		 * Sets the Lore the ItemStack will recieve on creation.
		 * 
		 * @param lore
		 *            The lines of lore the ItemStack will have.
		 * @return The current ItemStack Builder instance.
		 */
		public Builder setLore(List<String> lore) {
			ItemMeta im = itemStack.getItemMeta();
			im.setLore(lore);
			itemStack.setItemMeta(im);
			return this;
		}

		/**
		 * Adds a line of lore to the ItemStack. These will be inserted after
		 * the last line of lore if this was already present.
		 * 
		 * @param lore
		 *            lines of lore to add.
		 * @return The current ItemStack Builder instance.
		 */
		public Builder addLore(String... lore) {
			return addLore(Arrays.asList(lore));
		}

		/**
		 * Adds a line of lore to the ItemStack. These will be inserted after
		 * the last line of lore if this was already present.
		 * 
		 * @param lore
		 *            lines of lore to add.
		 * @return The current ItemStack Builder instance.
		 */
		public Builder addLore(List<String> lore) {
			ItemMeta im = itemStack.getItemMeta();
			List<String> oldLore = im.getLore();
			if (oldLore != null) {
				oldLore.addAll(lore);
			} else {
				oldLore = lore;
			}
			im.setLore(oldLore);
			itemStack.setItemMeta(im);
			return this;
		}

		/**
		 * Build the ItemStack with the data provided.
		 * 
		 * If the builder has conflicting data the last one in the list will be
		 * used in the final ItemStack.
		 * 
		 * @return The ItemStack that has been build.
		 */
		public ItemStack build() {
			return itemStack;
		}
	}
}
