package me.theeninja.bungeeteleport;

import me.theeninja.bungeeteleport.command.BungeeTeleportCommand;
import me.theeninja.bungeeteleport.server.ConnectPlayerServer;
import me.theeninja.bungeeteleport.server.SignClickListenerServer;
import me.theeninja.bungeeteleport.server.playerinformation.SignPlayerInformationUpdateHandler;
import me.theeninja.bungeeteleport.yaml.ConfigurationHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
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

public class BungeeTeleport extends JavaPlugin implements Listener {

    private boolean usePlayerJoinEvent;

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

        if (getDummyPlayer() != null) {

            manageServerIPMaxPlayerCountRelationships();

            usePlayerJoinEvent = false;
        }

        else {

            usePlayerJoinEvent = true;
        }
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
        getServer().getPluginManager().registerEvents(this, this);
    }

    /**
     * Registers the plugin messengers' listeners.
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

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        if (usePlayerJoinEvent) {

            Bukkit.getScheduler().scheduleSyncDelayedTask(this, this::manageServerIPMaxPlayerCountRelationships, 20);

            usePlayerJoinEvent = false;
        }
    }

    private void manageServerIPMaxPlayerCountRelationships() {

        ConnectPlayerServer.updateServerListWithDummyPlayer();

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {

            SignPlayerInformationUpdateHandler.receiveIPOfServerOnNetworks();

            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {

                SignPlayerInformationUpdateHandler.serverToMaxPlayers =
                        SignPlayerInformationUpdateHandler.getMaxPlayersOnAllServers();
            }, 10);
        }, 10);
    }
}
