package nl.imine.soundofnoteblocks.view.button;

import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import nl.imine.api.gui.Container;
import nl.imine.api.util.ColorUtil;
import nl.imine.api.util.ItemUtil;
import nl.imine.soundofnoteblocks.model.MusicPlayer;
import nl.imine.soundofnoteblocks.model.design.Lockable;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class ButtonLock extends ButtonMusicPlayer {

	public ButtonLock(MusicPlayer mp, int slot) {
		super(ItemUtil.getBuilder(Material.REDSTONE_TORCH).setName(ColorUtil.replaceColors("&cLock")).build(), mp,
				slot);
	}

	@Override
	public ItemStack getItemStack() {
		ItemStack is = super.getItemStack();
		SongPlayer songPlayer = getMusicPlayer().getSongPlayer();
		is.setType(
			(songPlayer != null && songPlayer.isPlaying()) ? Material.REDSTONE_TORCH : Material.REDSTONE_TORCH);
		return is;
	}

	@Override
	public void doAction(Player player, Container container, ClickType clickType) {
		SongPlayer songPlayer = getMusicPlayer().getSongPlayer();
		if (songPlayer != null && songPlayer.isPlaying()) {
			if (getMusicPlayer() instanceof Lockable) {
				Lockable lock = (Lockable) getMusicPlayer();
				lock.setLocked(true);
			}
			player.closeInventory();
			player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
		}
	}

}
