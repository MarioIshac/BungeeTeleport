package me.theeninja.bungeeteleport.server;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.theeninja.bungeeteleport.BungeeTeleport;
import me.theeninja.bungeeteleport.placeholder.PlaceholderManager;
import me.theeninja.bungeeteleport.server.playerinformation.SignPlayerInformationUpdateHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

/**
 * Implementation for connecting a player
 * to a non-minigame server (mocking /server (server)
 * with a sign).
 *
 * @author TheeNinja
 */

public class ConnectPlayerServer implements PluginMessageListener {

    private String server;
    private Player player;

    /**
     * Used to register the plugin message listener involved in the class.
     * Do not use for any other case.
     */
    public ConnectPlayerServer() {

    }

    /**
     * Creates an instance of ConnectPlayer. Please call connectPlayer()
     * on the instance after creating the instance.
     *
     * @param player - player to be connected
     * @param server - server to connect to
     */
    ConnectPlayerServer(Player player, String server) {
        this.server = server;
        this.player = player;
    }

    /**
     * Receives getServers call, and sets serverList variable to the server list
     * received
     *
     * @param channel (Should be BungeeCord)
     *                player
     *                message
     */
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {

        if (!channel.equals("BungeeCord")) {
            return;
        }

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();

        if (subchannel.equals("GetServers")) {

            List<String> serverList = Arrays.asList(in.readUTF().split(", "));

            Bukkit.getLogger().log(Level.INFO, "Received GetServers call, setting SignClickListenerServerList to " + serverList);
            SignClickListenerServer.serverList = serverList;
        }

        if (subchannel.equals("PlayerCount")) {

            String targetServer = in.readUTF();
            int targetServerPlayerCount = in.readInt();

            Bukkit.getLogger().log(Level.INFO, "Received PlayerCount call for server " + targetServer + ". Player count is " + targetServerPlayerCount);
            SignPlayerInformationUpdateHandler.serverPlayerCounts.put(
            /* Server name  */ targetServer,
            /* Player count */ targetServerPlayerCount);
        }

        if (subchannel.equals("ServerIP")) {

            String targetServer = in.readUTF();
            String serverIP = in.readUTF();
            short serverPort = in.readShort();

            Bukkit.getLogger().log(Level.INFO, "Received ServerIP call for server " + targetServer + ". Corresponding IP is " + serverIP + ":" + serverPort);
            SignPlayerInformationUpdateHandler.serverToIP.put(
                    targetServer, // Server name
                    serverIP + // Server IP
                            ":" +
                    serverPort); // Server port;
        }
    }


    /**
     * Connects the player declared with the instance to the server
     * declared in the instance.
     */
    void connectPlayer() {

        if (player == null) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot connect player, player is null.");
        }

        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        PlaceholderManager.Placeholder serverPlaceholder = new PlaceholderManager.Placeholder("server");
        serverPlaceholder.setPlaceholderAction(string -> string.replace(serverPlaceholder.getConfigurationRepresentation(), this.server));

        PlaceholderManager placeholderManager = new PlaceholderManager(new PlaceholderManager.Placeholder[] {serverPlaceholder});

        if (SignClickListenerServer.serverList.stream().anyMatch(serverInList -> serverInList.equals(server))) {

            String successfulConnectionMessage = BungeeTeleport.getInstance().getConfig().getString("SuccessfulConnectionMessage");
            String playerMessage = placeholderManager.replacePlaceholders(successfulConnectionMessage);
            playerMessage = ChatColor.translateAlternateColorCodes('&', playerMessage);

            player.sendMessage(playerMessage);

            out.writeUTF("Connect");
            out.writeUTF(server);
            player.sendPluginMessage(BungeeTeleport.getInstance(), "BungeeCord", out.toByteArray());
        }
        else {

            String invalidServerMessage = BungeeTeleport.getInstance().getConfig().getString("InvalidServerMessage");
            String playerMessage = placeholderManager.replacePlaceholders(invalidServerMessage);
            playerMessage = ChatColor.translateAlternateColorCodes('&', playerMessage);

            player.sendMessage(playerMessage);
        }
    }

    /**
     * Updates the list of online servers involved in the BungeeCord
     * network.
     */
    void updateServerList() {

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("GetServers");
        player.sendPluginMessage(BungeeTeleport.getInstance(), "BungeeCord", out.toByteArray());
    }

    public static void updateServerListWithDummyPlayer() {

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("GetServers");
        BungeeTeleport.getDummyPlayer().sendPluginMessage(BungeeTeleport.getInstance(), "BungeeCord", out.toByteArray());
    }
}
