package nl.imine.soundofnoteblocks.serialize;

import com.google.gson.*;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import nl.imine.soundofnoteblocks.SoundOfNoteBlocksPlugin;
import nl.imine.soundofnoteblocks.model.Track;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class TrackTypeAdapter implements JsonDeserializer<Track> {
    @Override
    public Track deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Track track = null;
        if (json instanceof JsonObject trackInformation) {
            String id = trackInformation.get("id").getAsString();
            String name = trackInformation.get("name").getAsString();
            String artist = trackInformation.get("artist").getAsString();
            Path songPath = SoundOfNoteBlocksPlugin.getInstance().getDataFolder().toPath().resolve(id + ".nbs");
            try {
                track = new Track(
                        UUID.fromString(id),
                        name,
                        artist,
                        NBSDecoder.parse(Files.newInputStream(songPath))

                );
            } catch (IOException e) {
                throw new JsonParseException(e);
            }
        }
        return track;
    }
}
