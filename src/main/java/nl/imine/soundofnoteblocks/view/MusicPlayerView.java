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

	public static final Material[] RECORDS = new Material[]{Material.GOLD_RECORD, Material.GREEN_RECORD,
			Material.RECORD_3, Material.RECORD_4, Material.RECORD_5, Material.RECORD_6, Material.RECORD_7,
			Material.RECORD_8, Material.RECORD_9, Material.RECORD_10, Material.RECORD_12};

	public static Container getMusicPlayerConatainer(MusicPlayer mp) {
		Container ret = GuiManager.getInstance()
				.createContainer(ColorUtil.replaceColors("&dJukebox       &cChoose Track!"), 45, true, false);
		for (Track track : TrackManager.getTracks()) {
			ret.addButton(new ButtonTrack(track, mp, ret.getButtons().size()));
		}
		ret.addStaticButton(Container.getDefaultPreviousButton(ret).setSlot(0));
		ret.addStaticButton(new ButtonMusicSort(1));
		ret.addStaticButton(new ButtonReplay(mp, 2));
		ret.addStaticButton(new ButtonStop(mp, 3));
		ret.addStaticButton(new ButtomRandomTrack(mp, 4));
		if (mp instanceof Tagable) {
			ret.addStaticButton(new ButtonToggleTag(mp, 5));
		}
		if (mp instanceof Lockable) {
			ret.addStaticButton(new ButtonLock(mp, 6));
		}
		ret.addStaticButton(new ButtonRadiomode(mp, 7));
		ret.addStaticButton(Container.getDefaultNextButton(ret).setSlot(8));
		return ret;
	}

	public static Container getRadiomodeContainer(MusicPlayer mp) {
		Container ret = GuiManager.getInstance().createContainer(ColorUtil.replaceColors("&zRadio!"), 9, false, false);
		ret.addButton(new ButtonRadiomode(mp, 4));
		ret.addStaticButton(new ButtonStop(mp, 8));
		return ret;
	}

}
