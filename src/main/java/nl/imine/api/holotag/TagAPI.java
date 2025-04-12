package nl.imine.api.holotag;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import nl.imine.soundofnoteblocks.SoundOfNoteBlocksPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import nl.imine.api.event.PlayerInteractTagEvent;

public class TagAPI {

	private static final UUID sessionId = UUID.randomUUID();
	private static final List<String> currentTags = new ArrayList<>();

	private static final Collection<ITag> tags = new HashSet<>();

	public static void init() {
		Bukkit.getServer().getPluginManager().registerEvents(new TagAPIListener(), SoundOfNoteBlocksPlugin.getInstance());
		for (World w : Bukkit.getWorlds()) {
			w.getEntities().stream().filter(e -> e instanceof ArmorStand).filter(e -> needsRemoval(e.getUniqueId()))
					.forEach(e -> {
						e.remove();
						unregisterLine(e.getUniqueId());
					});
		}
	}

	private static ITag registerTag(ITag tag) {
		tags.add(tag);
		return tag;
	}

	public static ITag createTag(Location location) {
		return registerTag(new GenericTag(location));
	}

	public static ITag createTag(Location location, double lineDistance) {
		return registerTag(new GenericTag(location, lineDistance));
	}

	public static boolean needsRemoval(UUID id) {
		ConfigurationSection sessions = loadSessions();
		for (String sessionKey : sessions.getKeys(true)) {
			if (!sessionKey.equals(sessionId.toString())) {
				List<String> session = (List<String>) sessions.get(sessionKey);
				for (String savedUUID : session) {
					if (id.toString().equals(savedUUID)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static void registerLine(ILine line) {
		if (line.getUniqueId() != null) {
			currentTags.add(line.getUniqueId().toString());
			saveSessions();
		}
	}

	public static void unregisterLine(ILine line) {
		unregisterLine(line.getUniqueId());
	}

	public static void unregisterLine(UUID id) {
		if (id == null) {
			return;
		}
		currentTags.remove(id.toString());
		ConfigurationSection sessions = loadSessions();
		for (String sessionKey : sessions.getKeys(true)) {
			if (!sessionKey.equals(sessionId.toString())) {
				List<String> session = (List<String>) sessions.get(sessionKey);
				session.removeIf(savedUUID -> id.toString().equals(savedUUID));
				sessions.set(sessionKey, session.isEmpty() ? null : session);
			}
		}
		saveSessions(sessions);
	}

	private static ConfigurationSection loadSessions() {
		if (!SoundOfNoteBlocksPlugin.getInstance().getDataFolder().exists()) {
			SoundOfNoteBlocksPlugin.getInstance().getDataFolder().mkdir();
		}
		File fcfg = new File(SoundOfNoteBlocksPlugin.getInstance().getDataFolder(), "lines.yml");
		if (!fcfg.exists()) {
			try {
				fcfg.createNewFile();
			} catch (FileNotFoundException fnfe) {
				System.err.println("FileNotFoundException: " + fnfe.getMessage());
			} catch (IOException ioe) {
				System.err.println("IOException: " + ioe.getMessage());
			}
		}
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(fcfg);
		ConfigurationSection sessions = cfg.getConfigurationSection("sessions");
		if (sessions == null) {
			sessions = cfg.createSection("sessions");
		}
		return sessions;
	}

	private static void saveSessions() {
		if (!SoundOfNoteBlocksPlugin.getInstance().getDataFolder().exists()) {
			SoundOfNoteBlocksPlugin.getInstance().getDataFolder().mkdir();
		}
		File fcfg = new File(SoundOfNoteBlocksPlugin.getInstance().getDataFolder(), "lines.yml");
		if (!fcfg.exists()) {
			try {
				fcfg.createNewFile();
			} catch (FileNotFoundException fnfe) {
				System.err.println("FileNotFoundException: " + fnfe.getMessage());
			} catch (IOException ioe) {
				System.err.println("IOException: " + ioe.getMessage());
			}
		}
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(fcfg);
		ConfigurationSection sessions = cfg.getConfigurationSection("sessions");
		if (sessions == null) {
			sessions = cfg.createSection("sessions");
		}
		sessions.set(sessionId.toString(), currentTags.toArray(new String[currentTags.size()]));
		try {
			cfg.save(fcfg);
		} catch (FileNotFoundException fnfe) {
			System.err.println("FileNotFoundException: " + fnfe.getMessage());
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	private static void saveSessions(ConfigurationSection sessions) {
		if (!SoundOfNoteBlocksPlugin.getInstance().getDataFolder().exists()) {
			SoundOfNoteBlocksPlugin.getInstance().getDataFolder().mkdir();
		}
		File fcfg = new File(SoundOfNoteBlocksPlugin.getInstance().getDataFolder(), "lines.yml");
		if (!fcfg.exists()) {
			try {
				fcfg.createNewFile();
			} catch (FileNotFoundException fnfe) {
				System.err.println("FileNotFoundException: " + fnfe.getMessage());
			} catch (IOException ioe) {
				System.err.println("IOException: " + ioe.getMessage());
			}
		}
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(fcfg);
		if (sessions == null) {
			sessions = cfg.createSection("sessions");
		}
		cfg.set("sessions", sessions.getValues(true));
		try {
			cfg.save(fcfg);
		} catch (FileNotFoundException fnfe) {
			System.err.println("FileNotFoundException: " + fnfe.getMessage());
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	private static class TagAPIListener implements Listener {

		@EventHandler
		public void onChunkLoad(ChunkLoadEvent evt) {
			Entity[] entities = evt.getChunk().getEntities();
			for (Entity e : entities) {
				if (e instanceof ArmorStand) {
					if (needsRemoval(e.getUniqueId())) {
						e.remove();
						unregisterLine(e.getUniqueId());
					}
				}
			}
		}

		@EventHandler
		public void onArmorStandInteract(PlayerInteractAtEntityEvent evt) {
			if (evt.getRightClicked().getType().equals(EntityType.ARMOR_STAND)) {
				for (ITag tag : tags) {
					for (ILine l : tag.getLines()) {
						if (l != null) {
							if (evt.getRightClicked().getUniqueId().equals(l.getUniqueId())) {
								Bukkit.getServer().getPluginManager().callEvent(
									new PlayerInteractTagEvent(evt.getPlayer(), tag, ActionType.RICHT_CLICK));
								evt.setCancelled(true);
								return;
							}
						}
					}
				}
			}
		}

		@EventHandler
		public void onArmorStandAttack(EntityDamageByEntityEvent evt) {
			if (evt.getDamager() instanceof Player && evt.getEntity() instanceof ArmorStand) {
				for (ITag tag : tags) {
					for (ILine l : tag.getLines()) {
						if (l != null) {
							if (evt.getEntity().getUniqueId().equals(l.getUniqueId())) {
								Bukkit.getServer().getPluginManager().callEvent(
									new PlayerInteractTagEvent((Player) evt.getDamager(), tag, ActionType.LEFT_CLICK));
								evt.setCancelled(true);
								return;
							}
						}
					}
				}
			}
		}
	}

	private static class GenericTag extends Tag {

		public GenericTag(Location location) {
			super(location);
		}

		public GenericTag(Location location, double lineDistance) {
			super(location, lineDistance);
		}
	}
}
