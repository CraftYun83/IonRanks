package com.ionnet.ionranks.listeners;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.ionnet.ionranks.Main;

public class PlayerJoinListener implements Listener {
	
	private Main plugin;
	
	public PlayerJoinListener(Main plugin) {
		this.plugin = plugin;
		
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void PlayerJoinEvent(PlayerJoinEvent e) {
		ConfigurationSection players = plugin.getConfig().getConfigurationSection("players");
		Set<String> existingPlayers = players.getKeys(true);
		
		if (!existingPlayers.contains(e.getPlayer().getUniqueId().toString())) {
			plugin.getConfig().set("players."+e.getPlayer().getUniqueId().toString(), plugin.getDefaultRank());
		}
		plugin.saveConfig();
		
		ConfigurationSection rank = plugin.getConfig().getConfigurationSection("ranks."+plugin.getConfig().getString("players."+e.getPlayer().getUniqueId().toString()));
		
		String prefix = ChatColor.translateAlternateColorCodes('&', rank.getString("prefix"));
		
		e.getPlayer().setPlayerListName(prefix + e.getPlayer().getName());
	}

}
