package nl.imine.soundofnoteblocks.view;

import nl.imine.api.gui.Container;
import nl.imine.api.gui.GuiManager;
import nl.imine.api.util.ColorUtil;
import nl.imine.soundofnoteblocks.controller.TrackManager;
import nl.imine.soundofnoteblocks.model.Jukebox;
import nl.imine.soundofnoteblocks.model.MusicPlayer;
import nl.imine.soundofnoteblocks.model.Track;
import nl.imine.soundofnoteblocks.model.design.Lockable;
import nl.imine.soundofnoteblocks.view.button.ButtomRandomTrack;
import nl.imine.soundofnoteblocks.view.button.ButtonLock;
import nl.imine.soundofnoteblocks.view.button.ButtonMusicSort;
import nl.imine.soundofnoteblocks.view.button.ButtonRadiomode;
import nl.imine.soundofnoteblocks.view.button.ButtonReplay;
import nl.imine.soundofnoteblocks.view.button.ButtonStop;
import nl.imine.soundofnoteblocks.view.button.ButtonToggleTag;
import nl.imine.soundofnoteblocks.view.button.ButtonTrack;
import org.bukkit.Material;

import java.util.List;

public class MusicPlayerView {
    public static final List<Material> RECORDS = List.of(
            Material.MUSIC_DISC_5,
            Material.MUSIC_DISC_13,
            Material.MUSIC_DISC_BLOCKS,
            Material.MUSIC_DISC_CAT,
            Material.MUSIC_DISC_CHIRP,
            Material.MUSIC_DISC_CREATOR,
            Material.MUSIC_DISC_CREATOR_MUSIC_BOX,
            Material.MUSIC_DISC_FAR,
            Material.MUSIC_DISC_MALL,
            Material.MUSIC_DISC_MELLOHI,
            Material.MUSIC_DISC_OTHERSIDE,
            Material.MUSIC_DISC_PIGSTEP,
            Material.MUSIC_DISC_PRECIPICE,
            Material.MUSIC_DISC_RELIC,
            Material.MUSIC_DISC_STAL,
            Material.MUSIC_DISC_STRAD,
            Material.MUSIC_DISC_WAIT
    );

    private final GuiManager guiManager;

    public MusicPlayerView(GuiManager guiManager) {
        this.guiManager = guiManager;
    }

    public Container getMusicPlayerContainer(MusicPlayer musicPlayer, TrackManager trackManager) {
        Container container = guiManager
                .createContainer(ColorUtil.replaceColors("&dJukebox       &cChoose Track!"), 45, true, false);
        for (Track track : trackManager.getTracks()) {
            container.addButton(new ButtonTrack(track, musicPlayer, container.getButtons().size()), false);
        }
        container.addStaticButton(Container.getDefaultPreviousButton(container).setSlot(0), false);
        container.addStaticButton(new ButtonMusicSort(1), false);
        container.addStaticButton(new ButtonReplay(musicPlayer, 2), false);
        container.addStaticButton(new ButtonStop(musicPlayer, 3), false);
        container.addStaticButton(new ButtomRandomTrack(trackManager, musicPlayer, 4), false);
        if (musicPlayer instanceof Jukebox) {
            container.addStaticButton(new ButtonToggleTag(musicPlayer, 5), false);
        }
        if (musicPlayer instanceof Lockable) {
            container.addStaticButton(new ButtonLock(musicPlayer, 6), false);
        }
        container.addStaticButton(new ButtonRadiomode(musicPlayer, 7), false);
        container.addStaticButton(Container.getDefaultNextButton(container).setSlot(8), true);
        return container;
    }

    public Container getRadioModeContainer(MusicPlayer mp) {
        Container container = guiManager.createContainer(ColorUtil.replaceColors("Radio!"), 9, false, false);
        container.addButton(new ButtonRadiomode(mp, 4), false);
        container.addButton(new ButtonStop(mp, 8), true);
        return container;
    }

}
