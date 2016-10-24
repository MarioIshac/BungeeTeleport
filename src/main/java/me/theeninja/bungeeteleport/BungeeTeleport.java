package me.theeninja.bungeeteleport;

import me.theeninja.bungeeteleport.command.BungeeTeleportCommand;
import me.theeninja.bungeeteleport.server.ConnectPlayerServer;
import me.theeninja.bungeeteleport.server.SignClickListenerServer;
import me.theeninja.bungeeteleport.yaml.ConfigurationHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.logging.Level;

/**
 * Main class for BungeeTeleport plugin. Contains onEnable()
 * and onDisable() methods, which are called when the plugin starts
 * and stops, respectively. Main class also registers events and plugin
 * message listener channels, which are essential to connecting the player
 * to various servers.
 *
 * @author TheeNinja
 */

public class BungeeTeleport extends JavaPlugin {

    // Instance of plugin
    private static BungeeTeleport plugin;

    /**
     * Returns the instance of the main class.
     *
     * @return Instance of main class.
     */
    public static BungeeTeleport getInstance() {

        return plugin;
    }

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

        registerEventListeners();
        Bukkit.getLogger().log(Level.INFO, "Registered events.");

        registerPluginMessengerListeners();
        Bukkit.getLogger().log(Level.INFO, "Registered plugin message listeners.");

        getCommand("bungeeteleport").setExecutor(new BungeeTeleportCommand());
        Bukkit.getLogger().log(Level.INFO, "Registered commands.");

        ConfigurationHandler.setUpDefaultConfig();
        Bukkit.getLogger().log(Level.INFO, "Registered configuration.");
    }

    /**
     * Called when plugin is disabled
     */
    @Override
    public void onDisable() {

        plugin = null;
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


    public static Player getDummyPlayer() {

        Collection<? extends Player> onlinePlayers = Bukkit.getServer().getOnlinePlayers();

        if (onlinePlayers.isEmpty()) {
            return null;
        }

        return onlinePlayers.iterator().next(); // Equivalent to getting an element
    }
}
