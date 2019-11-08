package nl.imine.soundofnoteblocks.controller;

import com.xxmicloxx.NoteBlockAPI.NoteBlockAPI;
import com.xxmicloxx.NoteBlockAPI.model.*;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.CompatibilityUtils;
import com.xxmicloxx.NoteBlockAPI.utils.InstrumentUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class EntitySongPlayer extends SongPlayer {

    private Entity targetEntity;

    public EntitySongPlayer(Song song) {
        super(song);
    }

    public Entity getTargetEntity() {
        return targetEntity;
    }

    public void setTargetEntity(Entity targetEntity) {
        this.targetEntity = targetEntity;
    }

    @Override
    public void playTick(Player player, int tick) {
        if (targetEntity == null || !player.getWorld().getName().equals(targetEntity.getWorld().getName())) {
            // not in same world
            return;
        }
        byte playerVolume = NoteBlockAPI.getPlayerVolume(player);

        for (Layer l : song.getLayerHashMap().values()) {
            Note note = l.getNote(tick);
            if (note == null) {
                continue;
            }

            float pitch = NotePitch.getPitch(note.getKey() - 33);

            if (InstrumentUtils.isCustomInstrument(note.getInstrument())) {
                CustomInstrument instrument = song.getCustomInstruments()
                        [note.getInstrument() - InstrumentUtils.getCustomInstrumentFirstIndex()];

                if (instrument.getSound() != null) {
                    CompatibilityUtils.playSound(player, player.getLocation(), instrument.getSound(),
                            this.soundCategory, playerVolume, pitch, false);
                } else {
                    CompatibilityUtils.playSound(player, player.getLocation(), instrument.getSoundFileName(),
                            this.soundCategory, playerVolume, pitch, false);
                }
            } else {
                CompatibilityUtils.playSound(player, player.getLocation(),
                        InstrumentUtils.getInstrument(note.getInstrument()), this.soundCategory,
                        playerVolume, pitch, false);
            }
        }
    }
}
