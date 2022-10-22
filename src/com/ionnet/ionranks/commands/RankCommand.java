package com.ionnet.ionranks.commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import com.ionnet.ionranks.Main;

public class RankCommand implements CommandExecutor {
	
	private Main plugin;
	
	public RankCommand(Main plugin) {
		this.plugin = plugin;
		
		Bukkit.getPluginCommand("rank").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String str, String[] args) {
		if (!sender.hasPermission("ionranks.manage")) {
			sender.sendMessage(ChatColor.RED+"Sorry, you do not the required permission to manage ranks!");
			return false;
		}
		
		ArrayList<String> ranks = new ArrayList<String>();
		
		ConfigurationSection r = plugin.getConfig().getConfigurationSection("ranks");
		for (String rank : r.getKeys(true)) {
			ranks.add(rank);
		}
		ArrayList<String> rs = new ArrayList<String>();
		for (int i = 0; i < ranks.size(); i=i+4) {
			rs.add(ranks.get(i));
		}
		ranks = rs;
		
		try {
			if (args[0].equalsIgnoreCase("create")) {
				try {
					if (!ranks.contains(args[1].toLowerCase())) {
						if (args[1] != null && args[2] != null && args[3] != null && args[4] != null) {
							if (Boolean.parseBoolean(args[4])) {
								plugin.getConfig().set("ranks."+plugin.getDefaultRank()+".default", false);
							}
							createNewRank(args[1].toLowerCase(), args[2], args[3], args[4]);
							sender.sendMessage(ChatColor.GREEN+"Succesfully Created Rank "+args[1] );
						}
					} else {
						sender.sendMessage(ChatColor.RED+"This Rank Already Exists!");
					}
				} catch (Exception e) {
					sender.sendMessage(ChatColor.RED+"Invalid Syntax! /rank create <rank> <prefix> <suffix> <default>");
				}
			} if (args[0].equalsIgnoreCase("remove")) {
				try {
					if (ranks.contains(args[1])) {
						if (args[1].equalsIgnoreCase(plugin.getDefaultRank())) {
							sender.sendMessage(ChatColor.RED+"Cannot delete the default rank!");
						} else {
							plugin.getConfig().set("ranks."+args[1], null);
							sender.sendMessage(ChatColor.GREEN+"Successfully deleted rank "+args[1]);
						}
					} else {
						sender.sendMessage(ChatColor.RED+"This rank does not exist!");
					}
				} catch (Exception e) {
					sender.sendMessage(ChatColor.RED+"Invalid Syntax! /rank remove <rank>");
				}
			} if (args[0].equalsIgnoreCase("set")) {
				try {
					if (ranks.contains(args[2].toLowerCase()) && Bukkit.getPlayer(args[1]).hasPlayedBefore()) {
						plugin.getConfig().set("players."+Bukkit.getPlayer(args[1]).getUniqueId().toString(), args[2]);
						sender.sendMessage(ChatColor.GREEN+"Succesfully Set "+args[1]+" as "+args[2]);
					} if (!Bukkit.getPlayer(args[1]).hasPlayedBefore()) {
						sender.sendMessage(ChatColor.RED+"This player has never joined before!");
					} if (!ranks.contains(args[2])) {
						sender.sendMessage(ChatColor.RED+"This rank does not exist!");
					}
				} catch (Exception e) {
					sender.sendMessage(ChatColor.RED+"Invalid Syntax! /rank set <username> <rank>");
				}
			} if (args[0].equalsIgnoreCase("list")) {
				sender.sendMessage("Ranks");
				sender.sendMessage("-----------------------------");
				for (int i = 0; i < ranks.size(); i++) {
					String prefix = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("ranks."+ranks.get(i)+".prefix"));
					String suffix = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("ranks."+ranks.get(i)+".suffix"));
					
					sender.sendMessage(ranks.get(i) +" - "+prefix + " TestPlayer" + suffix + " Test Message");
				}
				sender.sendMessage("-----------------------------");
			} if (args[0].equalsIgnoreCase("help")) {
				sender.sendMessage("Commands:");
				sender.sendMessage("----------------------------------------");
				sender.sendMessage("/rank create <rank> <prefix> <suffix> <isdefault> - Create new rank");
				sender.sendMessage("/rank remove <rank> - Delete a rank");
				sender.sendMessage("/rank set <username> <rank> - Set rank");
				sender.sendMessage("/rank list - List all ranks");
				sender.sendMessage("/rank reload - Reload ranks configuration");
				sender.sendMessage("----------------------------------------");
			} if (args[0].equalsIgnoreCase("reload")) {
				plugin.reloadConfig();
				sender.sendMessage(ChatColor.GREEN+"Successfully Reloaded Configuration!");
			} else {
				sender.sendMessage(ChatColor.RED+"Invalid Arguments! Do /rank help to see all available arguments!");
			}
		} catch (Exception e) {
			sender.sendMessage(ChatColor.RED+"Invalid Arguments! Do /rank help to see all available arguments!");
		}
		
		plugin.saveConfig();
		
		return false;
	}
	
	public void createNewRank(String rankName, String prefix, String suffix, String isdefault) {
		plugin.getConfig().set("ranks."+rankName, "");
		plugin.getConfig().set("ranks."+rankName+".prefix", prefix);
		plugin.getConfig().set("ranks."+rankName+".suffix", suffix);
		plugin.getConfig().set("ranks."+rankName+".default", Boolean.parseBoolean(isdefault));
	}

}
