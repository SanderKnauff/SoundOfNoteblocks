package nl.imine.soundofnoteblocks.controller;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.xxmicloxx.NoteBlockAPI.Instrument;
import com.xxmicloxx.NoteBlockAPI.Layer;
import com.xxmicloxx.NoteBlockAPI.Note;
import com.xxmicloxx.NoteBlockAPI.NoteBlockPlayerMain;
import com.xxmicloxx.NoteBlockAPI.NotePitch;
import com.xxmicloxx.NoteBlockAPI.Song;
import com.xxmicloxx.NoteBlockAPI.SongPlayer;

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
	public void playTick(Player p, int tick) {
		if (targetEntity == null || !p.getWorld().getName().equals(targetEntity.getWorld().getName())) {
			// not in same world
			return;
		}
		byte playerVolume = NoteBlockPlayerMain.getPlayerVolume(p);

		for (Layer l : song.getLayerHashMap().values()) {
			Note note = l.getNote(tick);
			if (note == null) {
				continue;
			}
			p.playSound(targetEntity.getLocation(), Instrument.getInstrument(note.getInstrument()),
				(l.getVolume() * (int) volume * (int) playerVolume) / 1000000f, NotePitch.getPitch(note.getKey() - 33));
		}
	}
}
