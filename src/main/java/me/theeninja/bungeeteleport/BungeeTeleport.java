package me.theeninja.bungeeteleport;

import org.bukkit.plugin.java.JavaPlugin;

public class BungeeTeleport extends JavaPlugin {
	BungeeTeleport plugin;
	@Override
	public void onEnable() {
		plugin = this;
		registerListeners();
	}
	@Override
	public void onDisable() {
		plugin = null;
	}
	public BungeeTeleport getInstance() {
		return plugin;
	}
	public void registerListeners() {
		getServer().getPluginManager().registerEvents(new SignBuildListener(), this);
	}
}
