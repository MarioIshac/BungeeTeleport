package me.theeninja.bungeeteleport;

import org.bukkit.plugin.java.JavaPlugin;

import me.theeninja.bungeeteleport.yaml.ConfigurationHandler;

public class BungeeTeleport extends JavaPlugin {
	private static BungeeTeleport plugin;
	@Override
	public void onEnable() {
		plugin = this;
		ConfigurationHandler.saveDefaultConfig();
		registerListeners();
		registerPluginMessenger();
	}
	@Override
	public void onDisable() {
		plugin = null;
	}
	public static BungeeTeleport getInstance() {
		return plugin;
	}
	public void registerListeners() {
		getServer().getPluginManager().registerEvents(new SignBuildListener(), this);
		getServer().getPluginManager().registerEvents(new SignClickListener(), this);
	}
	public void registerPluginMessenger() {
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new ConnectPlayer());
	}
}
