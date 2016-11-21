package me.theeninja.bungeeteleport.server.playerinformation;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.theeninja.bungeeteleport.BungeeTeleport;
import me.theeninja.bungeeteleport.server.SignClickListenerServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class SignPlayerInformationUpdateHandler {

    public static Map<String, Integer> serverPlayerCounts = new HashMap<>();
    public static Map<String, Integer> ipToMaxPlayers = new HashMap<>();
    public static Map<String, String> serverToIP = new HashMap<>();
    public static Map<String, Integer> serverToMaxPlayers = new HashMap<>();

    private String format;
    private String server;
    private Location location;

    public SignPlayerInformationUpdateHandler(String server, Location location) {

        this.location = location;
        this.server = ChatColor.stripColor(server);

        this.format = BungeeTeleport.getInstance().getConfig().getString("CurrentPlayersOutOfMaxFormat");
    }

    public void registerUpdates() {

        Bukkit.getScheduler().scheduleSyncRepeatingTask(BungeeTeleport.getInstance(), this::update, 0, 10);
    };

    private void update() {

        Player player = BungeeTeleport.getDummyPlayer();

        if (player == null) { // No players on server

            Bukkit.getLogger().log(Level.INFO, "No players on server, will not update sign.");
            return;
        }

        ByteArrayDataOutput pluginMessage = ByteStreams.newDataOutput();

        pluginMessage.writeUTF("PlayerCount");
        pluginMessage.writeUTF(server);

        player.sendPluginMessage(BungeeTeleport.getInstance(), "BungeeCord", pluginMessage.toByteArray());

        if (serverToMaxPlayers.isEmpty()) {

            Bukkit.getLogger().log(Level.SEVERE, "Could not find any associations between servers and max players.");
        }

        if (serverToMaxPlayers.get(server) == null) {

            Bukkit.getLogger().log(Level.SEVERE, "Could not find association between server: " + server + " and max player count.");
        }

        int maxPlayers = serverToMaxPlayers.get(server);

        Bukkit.getScheduler().runTaskLater(BungeeTeleport.getInstance(), () -> {

            int currentNumberOfPlayers = serverPlayerCounts.get(server);

            Sign sign = (Sign) location.getBlock().getState();

            sign.setLine(2, currentNumberOfPlayers + " / " + maxPlayers);
            sign.update();

            Bukkit.getLogger().log(Level.INFO, "Updated sign player information to " + currentNumberOfPlayers + " / " + maxPlayers);
        }, 5);
    }

    public static HashMap<String, Integer> getMaxPlayersOnAllServers() {

        Bukkit.getLogger().log(Level.INFO, "Getting max players on all servers.");

        HashMap<String, Integer> ipToMaxPlayers = new HashMap<>();

        if (serverToIP.isEmpty()) {

            Bukkit.getLogger().log(Level.SEVERE, "No server/IP relationships found. Please contact developer.");
        }

        // printing out server to ip
        for (String server : serverToIP.keySet()) {

            Bukkit.getLogger().log(Level.INFO, "The server: " + server + " is registered with the following IP: " + serverToIP.get(server));
        }

        for (String completeIP : serverToIP.values()) {

            Bukkit.getLogger().log(Level.INFO, "Retrieving max player count of IP: " + completeIP);

            try {
                String ipWithoutPort = completeIP.split(":")[0];
                int port = Integer.parseInt(completeIP.split(":")[1]);

                Socket socket = new Socket(ipWithoutPort, port);

                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                DataInputStream in = new DataInputStream(socket.getInputStream());

                out.write(254);

                int b;
                StringBuilder str = new StringBuilder();
                while ((b = in.read()) != -1) {
                    if (b != 0 && b > 16 && b != 255 && b != 23 && b != 24) {
                        // Not sure what use the two characters are so I omit them
                        str.append((char) b);
                        // System.out.println(b + ":" + ((char) b));
                    }
                }

                socket.close();
                String[] data = str.toString().split("§");

                Bukkit.getLogger().log(Level.INFO, "Correlating "  + completeIP + " with the max player count: " + Integer.parseInt(data[2]));

                ipToMaxPlayers.put(completeIP, Integer.parseInt(data[2]) /* Max players on server that has given ip */);
            } catch (IOException e) {

                e.printStackTrace();
            }
        }

        HashMap<String, Integer> serverToMaxPlayers = new HashMap<>();

        Map<String, String> ipToServer = serverToIP.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));

        for (String ip : ipToMaxPlayers.keySet()) {

            int maxPlayersForIP = ipToMaxPlayers.get(ip);
            String server = ipToServer.get(ip);

            serverToMaxPlayers.put(server, maxPlayersForIP);
        }

        return serverToMaxPlayers;
    }

    public static void receiveIPOfServerOnNetworks() {

        for (String server : SignClickListenerServer.serverList) {

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("ServerIP");
            out.writeUTF(server);

            BungeeTeleport.getDummyPlayer().sendPluginMessage(BungeeTeleport.getInstance(), "BungeeCord", out.toByteArray());
        }
    }
}
