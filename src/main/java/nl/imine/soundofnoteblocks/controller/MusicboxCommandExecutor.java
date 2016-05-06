/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.imine.soundofnoteblocks.controller;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import nl.imine.api.util.ColorUtil;

public class MusicboxCommandExecutor implements TabExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("jukebox")) {
			if (sender.hasPermission("iMine.jukebox.reload") && args.length == 1
					&& args[0].equalsIgnoreCase("reload")) {
				TrackManager.reloadTracks();
				sender.sendMessage(
					ColorUtil.replaceColors("&7Reloaded tracks from repo's.", TrackManager.getTracks().size()));
			}
			return true;
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> ret = new ArrayList<>();
		ret.add("reload");
		return ret;
	}
}
