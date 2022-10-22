package com.ionnet.ionranks.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.ionnet.ionranks.Main;

public class PlayerChatListener implements Listener {
	
	private Main plugin;
	
	public PlayerChatListener(Main plugin) {
		this.plugin = plugin;
		
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		
		if (plugin.getConfig().getString("players."+e.getPlayer().getUniqueId().toString()) == null) {
			plugin.getConfig().set("players."+e.getPlayer().getUniqueId().toString(), plugin.getDefaultRank());
		}
		
		ConfigurationSection rank = plugin.getConfig().getConfigurationSection("ranks."+plugin.getConfig().getString("players."+e.getPlayer().getUniqueId().toString()));
		String prefix = ChatColor.translateAlternateColorCodes('&', rank.getString("prefix"));
		String suffix = ChatColor.translateAlternateColorCodes('&', rank.getString("suffix"));
		
		e.setFormat(prefix + " "+e.getPlayer().getName() + suffix + " "+e.getMessage());
		
	}

}
