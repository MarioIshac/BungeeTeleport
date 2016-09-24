package me.theeninja.bungeeteleport;

import org.bukkit.plugin.java.JavaPlugin;

import me.theeninja.bungeeteleport.server.ConnectPlayerServer;
import me.theeninja.bungeeteleport.server.SignClickListenerServer;
import me.theeninja.bungeeteleport.yaml.Configuration;

/**
 * Main class for BungeeTeleport plugin. Contains onEnable()
 * and onDisable() methods, which are called when the plugin starts
 * and stops, respectively. Main class also registers events and plugin
 * message listener channels, which are essential to connecting the player
 * to various servers.
 * 
 * @author TheeNinja
 *
 */

public class BungeeTeleport extends JavaPlugin {
	
	private static BungeeTeleport plugin;
	
	// Receives instance of configuration manager. Call all methods using
	// configurationInstance.<method>
	
	// To access fields in configuration, use 
	// configurationInstance.getConfig().get<Type>(<Key>)
	private Configuration configurationInstance = Configuration.getInstance();
	
	@Override
	public void onEnable() {
		
		plugin = this;
		
		configurationInstance.saveDefaultConfig();
		
		registerEventListeners();
		registerPluginMessengerListeners();
	}
	
	@Override
	public void onDisable() {
		
		plugin = null;
	}
	
	/**
	 * Returns the instance of the main class.
	 * 
	 * @return Instance of main class.
	 */
	public static BungeeTeleport getInstance() {
		
		return plugin;
	}
	
	/**
	 * Registers the event handlers' listeners.
	 */
	public void registerEventListeners() {
		
		getServer().getPluginManager().registerEvents(new SignBuildListener(), this);
		getServer().getPluginManager().registerEvents(new SignClickListenerServer(), this);
	}
	
	/**
	 * Registers the plugin messangers' listeners.
	 */
	public void registerPluginMessengerListeners() {
		
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new ConnectPlayerServer());
	}
}
