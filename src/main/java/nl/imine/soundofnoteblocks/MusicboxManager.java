package nl.imine.soundofnoteblocks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import nl.imine.api.util.LocationUtil;
import static nl.imine.soundofnoteblocks.Util.readFromFile;
import org.bukkit.Location;
import org.bukkit.Material;

public class MusicboxManager {

	private static final File MUSICBOX_LIST = new File(SoundOfNoteBlocks.getInstance().getDataFolder(),
			"Musicboxes.json");

	public static final double DISTANCE = Math.pow(35, 2);

	public static final Material[] RECORDS = new Material[]{Material.RECORD_10, Material.RECORD_12, Material.RECORD_3,
			Material.RECORD_4, Material.RECORD_5, Material.RECORD_6, Material.RECORD_7, Material.RECORD_8,
			Material.RECORD_9, Material.GOLD_RECORD, Material.GREEN_RECORD};

	private static final ArrayList<Musicbox> musicBoxes = new ArrayList<>();

	public static Musicbox findJukebox(Location location) {
		for (Musicbox musicbox : musicBoxes) {
			if (musicbox.getLastTrack() != null) {
				System.out.println(musicbox.getLastTrack().getId().toString());
			} else {
				System.out.println("null");
			}
			if (musicbox.getLocation().equals(location)) {
				return musicbox;
			}
		}
		Musicbox j = new Musicbox(location);
		musicBoxes.add(j);
		return j;
	}

	public static void removeJukebox(Musicbox musicbox) {
		musicBoxes.remove(musicbox);
	}

	public static List<Musicbox> getMusicboxes() {
		return musicBoxes;
	}

	public static void loadMusicboxesFromConfig() {
		String json = readFromFile(MUSICBOX_LIST);
		Gson gson = new GsonBuilder().create();
		JsonArray jsonArray = gson.fromJson(json, JsonArray.class);
		System.out.println(jsonArray);
		if (jsonArray != null) {
			jsonArray.getAsJsonArray().forEach(element -> {
				JsonObject musicbox = element.getAsJsonObject();
				JsonObject position = musicbox.getAsJsonObject("Position");
				LocationUtil.Position location = new LocationUtil.Position(
						UUID.fromString(position.get("W").getAsString()), position.get("X").getAsDouble(),
						position.get("Y").getAsDouble(), position.get("Z").getAsDouble());
				UUID lastTrack = null;
				if (musicbox.has("LastTrack")) {
					UUID.fromString(musicbox.get("LastTrack").getAsString());
				}
				musicBoxes.add(new Musicbox(location.toLocation(), musicbox.get("TagVisible").getAsBoolean(),
						musicbox.get("RadioMode").getAsBoolean(), lastTrack));
			});
		}
	}

	public static void saveMusicboxToConfig() {
		MUSICBOX_LIST.delete();
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		JsonArray vipBlocksJson = new JsonArray();
		getMusicboxes().forEach(musicbox -> {
			JsonObject musicboxJson = new JsonObject();
			musicboxJson.add("TagVisible", new JsonPrimitive(musicbox.isTagVisible()));
			musicboxJson.add("RadioMode", new JsonPrimitive(musicbox.isRadioMode()));
			if (musicbox.getLastTrack() != null) {
				musicboxJson.add("LastTrack", new JsonPrimitive(musicbox.getLastTrack().getId().toString()));
			}
			JsonObject positionJson = new JsonObject();
			positionJson.add("X", new JsonPrimitive(musicbox.getLocation().getX()));
			positionJson.add("Y", new JsonPrimitive(musicbox.getLocation().getY()));
			positionJson.add("Z", new JsonPrimitive(musicbox.getLocation().getZ()));
			positionJson.add("W", new JsonPrimitive(musicbox.getLocation().getWorld().getUID().toString()));
			musicboxJson.add("Position", positionJson);
			vipBlocksJson.add(musicboxJson);
		});
		String json = gson.toJson(vipBlocksJson);
		Util.saveToFile(MUSICBOX_LIST, new String[]{json});
	}
}
