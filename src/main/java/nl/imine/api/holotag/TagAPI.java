package nl.imine.api.holotag;

import nl.imine.soundofnoteblocks.SoundOfNoteBlocksPlugin;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.UUID;

public class TagAPI implements Listener {
	private static final NamespacedKey TAG_API_SESSION_KEY = NamespacedKey.fromString("soundofnoteblocks:tagapi");

	private final UUID sessionId = UUID.randomUUID();

	public void init(SoundOfNoteBlocksPlugin plugin) {
		Bukkit
				.getServer()
				.getPluginManager()
				.registerEvents(new TagAPI(), plugin);

		Bukkit
				.getWorlds()
				.stream()
				.flatMap(world -> world.getEntities().stream())
				.filter(TextDisplay.class::isInstance)
				.filter(this::shouldRemoveEntity)
				.forEach(Entity::remove);
	}

	private boolean shouldRemoveEntity(Entity entity) {
		final var container = entity.getPersistentDataContainer();
        if (!entity.getPersistentDataContainer().has(TAG_API_SESSION_KEY)) {
            return false;
        }
		final var storedSessionId = container.get(TAG_API_SESSION_KEY, PersistentDataType.STRING);
		return !sessionId.toString().equals(storedSessionId);
	}

	public void writeSessionKey(Entity entity) {
		entity.getPersistentDataContainer().set(TAG_API_SESSION_KEY, PersistentDataType.STRING, sessionId.toString());
	}

	@EventHandler
	public void onChunkLoad(ChunkLoadEvent evt) {
		Arrays
				.stream(evt.getChunk().getEntities())
				.filter(this::shouldRemoveEntity)
				.forEach(Entity::remove);
	}
}
