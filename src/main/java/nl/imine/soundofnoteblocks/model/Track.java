package nl.imine.soundofnoteblocks.model;

import com.xxmicloxx.NoteBlockAPI.model.Song;

import java.util.UUID;

public record Track(
        UUID id,
        String name,
        String artist,
        Song song
) {
}
