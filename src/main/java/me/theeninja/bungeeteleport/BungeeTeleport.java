package me.theeninja.bungeeteleport;

import org.bukkit.plugin.java.JavaPlugin;

import me.theeninja.bungeeteleport.server.ConnectPlayerServer;
import me.theeninja.bungeeteleport.server.SignClickListenerServer;
import me.theeninja.bungeeteleport.yaml.Configuration;
import me.theeninja.bungeeteleport.yaml.ConfigurationManager;
import me.theeninja.bungeeteleport.yaml.ConfigurationValues;

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

	// Instance of plugin
	private static BungeeTeleport plugin;

	// Receives instance of configuration manager. Call all methods using
	// configurationInstance.<method>

	// To access fields in configuration, use 
	// configurationInstance.getConfig().get<Type>(<Key>)
	private ConfigurationManager configurationManager;

	private Configuration configuration;

	/**
	 * Called when plugin is enabled
	 * -Initializes plugin
	 * -Sets up default config
	 * -Registers event listeners/handlers
	 * -Registers plugin message listeners
	 */
	@Override
	public void onEnable() {

		plugin = this;
		
		configurationManager = new ConfigurationManager();
		configuration = configurationManager.getNewConfig("config.yml");
		
		registerEventListeners();
		registerPluginMessengerListeners();
		registerDefaultConfigurationValues();
	}

	/**
	 * Called when plugin is disabled
	 */
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
	private void registerEventListeners() {

		getServer().getPluginManager().registerEvents(new SignBuildListener(), this);
		getServer().getPluginManager().registerEvents(new SignClickListenerServer(), this);
	}

	/**
	 * Registers the plugin messangers' listeners.
	 */
	private void registerPluginMessengerListeners() {

		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new ConnectPlayerServer());
	}
	
	/**
	 * Registers default configuration values, following with a config save.
	 */
	private void registerDefaultConfigurationValues() {
		
		if (ConfigurationManager.isNull(configuration, ConfigurationValues.noPermissionUseMessage.getIdentifier())) 
			configuration.defaultSet("NoPermissionUseMessage", ConfigurationValues.noPermissionUseMessage);

		if (ConfigurationManager.isNull(configuration, ConfigurationValues.noPermissionBuildMessage.getIdentifier())) 
			configuration.defaultSet("NoPermissionBuildMessage", ConfigurationValues.noPermissionBuildMessage);
		
		
		configuration.saveConfig();
	}
}
