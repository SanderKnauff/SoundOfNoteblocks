/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.imine.soundofnoteblocks.controller;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

public class MusicboxCommandExecutor implements TabExecutor {

    private final TrackManager trackManager;

    public MusicboxCommandExecutor(TrackManager trackManager) {
        this.trackManager = trackManager;
    }

    @Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("jukebox")) {
            return false;
        }

        if (args.length != 1) {
            return true;
        }

		if (!args[0].equalsIgnoreCase("reload") || !sender.hasPermission("iMine.jukebox.reload")) {
            return true;
        }

        trackManager.reloadTracks();
        sender.sendMessage(ChatColor.GRAY + "Reloaded tracks from repos.");
        return true;
    }

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		return List.of("reload");
	}
}
