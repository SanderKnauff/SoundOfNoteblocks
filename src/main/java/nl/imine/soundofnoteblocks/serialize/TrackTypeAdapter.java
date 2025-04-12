package nl.imine.soundofnoteblocks.serialize;

import com.google.gson.*;
import nl.imine.soundofnoteblocks.SoundOfNoteBlocksPlugin;
import nl.imine.soundofnoteblocks.model.Track;

import java.lang.reflect.Type;

public class TrackTypeAdapter implements JsonDeserializer<Track> {

    @Override
    public Track deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Track track = null;
        if (json instanceof JsonObject trackInformation) {
            String id = trackInformation.get("id").getAsString();
            String name = trackInformation.get("name").getAsString();
            String artist = trackInformation.get("artist").getAsString();
            track = new Track(id, name, artist, SoundOfNoteBlocksPlugin.getInstance().getDataFolder().toPath().resolve(id + ".nbs"));
        }
        return track;
    }
}
