package nl.imine.soundofnoteblocks.view;

import org.bukkit.Material;

import nl.imine.api.gui.Container;
import nl.imine.api.gui.GuiManager;
import nl.imine.api.util.ColorUtil;
import nl.imine.soundofnoteblocks.controller.TrackManager;
import nl.imine.soundofnoteblocks.model.MusicPlayer;
import nl.imine.soundofnoteblocks.model.Track;
import nl.imine.soundofnoteblocks.model.design.Lockable;
import nl.imine.soundofnoteblocks.model.design.Tagable;
import nl.imine.soundofnoteblocks.view.button.ButtomRandomTrack;
import nl.imine.soundofnoteblocks.view.button.ButtonLock;
import nl.imine.soundofnoteblocks.view.button.ButtonMusicSort;
import nl.imine.soundofnoteblocks.view.button.ButtonRadiomode;
import nl.imine.soundofnoteblocks.view.button.ButtonReplay;
import nl.imine.soundofnoteblocks.view.button.ButtonStop;
import nl.imine.soundofnoteblocks.view.button.ButtonToggleTag;
import nl.imine.soundofnoteblocks.view.button.ButtonTrack;

public class MusicPlayerView {

    public static final Material[] RECORDS = new Material[]{Material.MUSIC_DISC_11, Material.MUSIC_DISC_13,
            Material.MUSIC_DISC_BLOCKS, Material.MUSIC_DISC_CAT, Material.MUSIC_DISC_CHIRP, Material.MUSIC_DISC_FAR, Material.MUSIC_DISC_MALL,
            Material.MUSIC_DISC_MELLOHI, Material.MUSIC_DISC_STAL, Material.MUSIC_DISC_STRAD, Material.MUSIC_DISC_WAIT};

    public static Container getMusicPlayerContainer(MusicPlayer musicPlayer) {
        Container container = GuiManager.getInstance()
                .createContainer(ColorUtil.replaceColors("&dJukebox       &cChoose Track!"), 45, true, false);
        for (Track track : TrackManager.getTracks()) {
            container.addButton(new ButtonTrack(track, musicPlayer, container.getButtons().size()), false);
        }
        container.addStaticButton(Container.getDefaultPreviousButton(container).setSlot(0), false);
        container.addStaticButton(new ButtonMusicSort(1), false);
        container.addStaticButton(new ButtonReplay(musicPlayer, 2), false);
        container.addStaticButton(new ButtonStop(musicPlayer, 3), false);
        container.addStaticButton(new ButtomRandomTrack(musicPlayer, 4), false);
        if (musicPlayer instanceof Tagable) {
            container.addStaticButton(new ButtonToggleTag(musicPlayer, 5), false);
        }
        if (musicPlayer instanceof Lockable) {
            container.addStaticButton(new ButtonLock(musicPlayer, 6), false);
        }
        container.addStaticButton(new ButtonRadiomode(musicPlayer, 7), false);
        container.addStaticButton(Container.getDefaultNextButton(container).setSlot(8), true);
        return container;
    }

    public static Container getRadioModeContainer(MusicPlayer mp) {
        Container container = GuiManager.getInstance().createContainer(ColorUtil.replaceColors("Radio!"), 9, false, false);
        container.addButton(new ButtonRadiomode(mp, 4), false);
        container.addButton(new ButtonStop(mp, 8), true);
        return container;
    }

}
