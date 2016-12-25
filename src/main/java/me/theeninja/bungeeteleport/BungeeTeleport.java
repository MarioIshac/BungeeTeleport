package me.theeninja.bungeeteleport;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
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

    // Instance of plugin
    private static BungeeTeleport plugin;
    private boolean usePlayerJoinEvent;

    /**
     * Returns the instance of the main class.
     *
     * @return Instance of main class.
     */
    public static BungeeTeleport getInstance() {

        return plugin;
    }

    public static Player getDummyPlayer() {

        Collection<? extends Player> onlinePlayers = Bukkit.getServer().getOnlinePlayers();

        if (onlinePlayers.isEmpty()) {

            return null;
        }

        return onlinePlayers.iterator().next(); // Equivalent to getting an element
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

            manageIPServerRelationships();

            usePlayerJoinEvent = false;
        } else {

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

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        if (usePlayerJoinEvent) {

            manageIPServerRelationships();

            // Requires casting to listener due to implementing Listener and implementing Plugin at the same time
            // We only want to unregister the events from this class, not the entire plugin
            PlayerJoinEvent.getHandlerList().unregister((Listener) this);

            usePlayerJoinEvent = false;
        }
    }

    private void manageIPServerRelationships() {

        int serverListTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {

            if (SignClickListenerServer.serverList == null || SignClickListenerServer.serverList.isEmpty()) {

                ConnectPlayerServer.updateServerListWithDummyPlayer();
            }
        }, 0, 1);

        int serverToIPTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {

            boolean isServerListTaskFinished = !(SignClickListenerServer.serverList == null || SignClickListenerServer.serverList.isEmpty());

            if (isServerListTaskFinished) {

                if (SignPlayerInformationUpdateHandler.serverToIP == null ||
                        SignClickListenerServer.serverList == null || (SignPlayerInformationUpdateHandler.serverToIP.size() !=
                        SignClickListenerServer.serverList.size())) {

                    SignPlayerInformationUpdateHandler.receiveIPOfServerOnNetworks();
                }
            }
        }, 0, 1);

        int serverToMaxPlayersTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {

            boolean isServerTaskFinished = !(SignClickListenerServer.serverList == null || SignClickListenerServer.serverList.isEmpty());
            boolean isReceiveIPsofServersTaskFinished = !(SignPlayerInformationUpdateHandler.serverToIP == null ||
                    SignClickListenerServer.serverList == null ||(SignPlayerInformationUpdateHandler.serverToIP.size() !=
                    SignClickListenerServer.serverList.size()));

            if (isServerTaskFinished && isReceiveIPsofServersTaskFinished) {

                if (SignPlayerInformationUpdateHandler.serverToMaxPlayers == null || SignPlayerInformationUpdateHandler.serverToMaxPlayers.isEmpty()) {

                    SignPlayerInformationUpdateHandler.serverToMaxPlayers = SignPlayerInformationUpdateHandler.getMaxPlayersOnAllServers();
                }
            }
        }, 0, 1);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {

            boolean isServerTaskFinished = !(SignClickListenerServer.serverList == null || SignClickListenerServer.serverList.isEmpty());
            boolean isReceiveIPsofServersTaskFinished = !(SignPlayerInformationUpdateHandler.serverToIP == null ||
                    SignClickListenerServer.serverList == null || (SignPlayerInformationUpdateHandler.serverToIP.size() !=
                    SignClickListenerServer.serverList.size()));
            boolean isMaxPlayersTaskFinished = !(SignPlayerInformationUpdateHandler.serverToMaxPlayers == null || SignPlayerInformationUpdateHandler.serverToMaxPlayers.isEmpty());

            if (isServerTaskFinished && isReceiveIPsofServersTaskFinished && isMaxPlayersTaskFinished) {

                Bukkit.getScheduler().cancelTask(serverListTask);
                Bukkit.getScheduler().cancelTask(serverToIPTask);
                Bukkit.getScheduler().cancelTask(serverToMaxPlayersTask);
            }
        }, 0, 1);
    }
}
