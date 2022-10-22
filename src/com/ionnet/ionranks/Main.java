package com.ionnet.ionranks;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import com.ionnet.ionranks.commands.RankCommand;
import com.ionnet.ionranks.listeners.PlayerChatListener;
import com.ionnet.ionranks.listeners.PlayerJoinListener;

public class Main extends JavaPlugin {
	
	
	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		RankCommand rankCommand = new RankCommand(this);
		if (getDefaultRank() == null) {
			rankCommand.createNewRank("default", "&f", ":", "true");
		}
		new PlayerJoinListener(this);
		new PlayerChatListener(this);
	}
	
	public String getDefaultRank() {
		ConfigurationSection ranks = this.getConfig().getConfigurationSection("ranks");
		for (String rank : ranks.getKeys(true)) {
			if (this.getConfig().getBoolean("ranks."+rank+".default")) {
				return rank;
			}
		}
		return null;
	}
	
}
