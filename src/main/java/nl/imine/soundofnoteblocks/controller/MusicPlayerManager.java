package nl.imine.soundofnoteblocks.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import nl.imine.soundofnoteblocks.SoundOfNoteBlocksPlugin;
import nl.imine.soundofnoteblocks.model.MusicPlayer;
import nl.imine.soundofnoteblocks.model.Gettoblaster;
import nl.imine.soundofnoteblocks.model.Jukebox;
import nl.imine.soundofnoteblocks.model.Walkman;

public class MusicPlayerManager {

	private static List<MusicPlayer> musicPlayers = new ArrayList<>();
	private static final Path SAVE_FILE = Paths.get(SoundOfNoteBlocksPlugin.getInstance().getDataFolder().getPath(),
			"musicplayers.json");

	public static List<MusicPlayer> getAllMusicPlayers() {
		return musicPlayers;
	}

	public static Collection<Jukebox> getJukeboxes() {
		List<Jukebox> ret = new ArrayList<>();
		musicPlayers.stream().filter(player -> player instanceof Jukebox)
				.forEach(jukebox -> ret.add((Jukebox) jukebox));
		return ret;
	}

	public static Jukebox getJukebox(Location loc) {
		Jukebox jb = null;
		for (MusicPlayer mp : musicPlayers) {
			if (mp instanceof Jukebox && ((Jukebox) mp).getLocation().equals(loc)) {
				jb = (Jukebox) mp;
				break;
			}
		}
		if (jb == null) {
			jb = new Jukebox(loc);
			musicPlayers.add(jb);
		}
		return jb;
	}

	public static void removeJukebox(Location loc) {
		Iterator<MusicPlayer> mpit = musicPlayers.iterator();
		while (mpit.hasNext()) {
			MusicPlayer mp = mpit.next();
			if (mp instanceof Jukebox && ((Jukebox) mp).getLocation().equals(loc)) {
				mp.setRadioMode(false);
				mp.stopPlaying();
				mpit.remove();
				break;
			}
		}
	}

	public static Collection<Walkman> getWalkmans() {
		List<Walkman> ret = new ArrayList<>();
		musicPlayers.stream().filter(player -> player instanceof Walkman)
				.forEach(walkman -> ret.add((Walkman) walkman));
		return ret;
	}

	public static Walkman getWalkman(Player pl) {
		Walkman wm = null;
		for (MusicPlayer mp : musicPlayers) {
			if (mp instanceof Walkman && ((Walkman) mp).getPlayer() == pl) {
				wm = (Walkman) mp;
				break;
			}
		}
		if (wm == null) {
			wm = new Walkman(pl);
			musicPlayers.add(wm);
		}
		return wm;
	}

	public static void removeWalkman(Player pl) {
		Iterator<MusicPlayer> mpit = musicPlayers.iterator();
		while (mpit.hasNext()) {
			MusicPlayer mp = mpit.next();
			if (mp instanceof Walkman && ((Walkman) mp).getPlayer() == pl) {
				mp.setRadioMode(false);
				mp.stopPlaying();
				mpit.remove();
				break;
			}
		}
	}

	public static Collection<Gettoblaster> getGettoblaster() {
		List<Gettoblaster> ret = new ArrayList<>();
		musicPlayers.stream().filter(player -> player instanceof Gettoblaster)
				.forEach(gettoblaster -> ret.add((Gettoblaster) gettoblaster));
		return ret;
	}

	public static Gettoblaster getGettoblaster(Entity entity) {
		Gettoblaster gb = null;
		for (MusicPlayer mp : musicPlayers) {
			if (mp instanceof Gettoblaster && ((Gettoblaster) mp).getCenterdEntity() == entity) {
				gb = (Gettoblaster) mp;
				break;
			}
		}
		if (gb == null) {
			gb = new Gettoblaster(entity);
			musicPlayers.add(gb);
		}
		return gb;
	}

	public static void removeGettoblaster(Entity entity) {
		Iterator<MusicPlayer> mpit = musicPlayers.iterator();
		while (mpit.hasNext()) {
			MusicPlayer mp = mpit.next();
			if (mp instanceof Gettoblaster && ((Gettoblaster) mp).getCenterdEntity() == entity) {
				mp.setRadioMode(false);
				mp.stopPlaying();
				mpit.remove();
				break;
			}
		}
	}

	public static void save() {
		try {
			SoundOfNoteBlocksPlugin.getGson().toJson(musicPlayers.toArray(new MusicPlayer[musicPlayers.size()]),
				MusicPlayer[].class, Files.newBufferedWriter(SAVE_FILE));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void load() {
		try {
			musicPlayers = new ArrayList<>(
					Arrays.asList(SoundOfNoteBlocksPlugin.getGson().fromJson(Files.newBufferedReader(SAVE_FILE), MusicPlayer[].class)));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
