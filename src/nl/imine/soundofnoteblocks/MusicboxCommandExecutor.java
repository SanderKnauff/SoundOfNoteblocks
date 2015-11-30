/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.imine.soundofnoteblocks;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

/**
 *
 * @author Sansko1337
 */
public class MusicboxCommandExecutor implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("jukebox")) {
            if (sender.hasPermission("iMine.records") && args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                SoundOfNoteBlocks.getTrackManager().reloadTracks();
            }
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> ret = new ArrayList<>();
        if (command.getName().equalsIgnoreCase("records")) {
            if (args.length == 1) {
                ret.add("reload");
            }
        }
        return ret;
    }
}
