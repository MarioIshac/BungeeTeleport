package me.theeninja.bungeeteleport.server;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.theeninja.bungeeteleport.BungeeTeleport;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class SignPlayerInformationUpdateHandler {

    static Map<String, Integer> serverPlayerCounts = new HashMap<>();

    public static void updateServerPlayerCounts() {


    }

    private Sign sign;
    private String format;
    private String server;
    private Location location;
    private int playerInformationLineNumber;

    SignPlayerInformationUpdateHandler(Sign sign, Location location, int playerInformationLineNumber) {

        this.sign = sign;
        this.location = location;
        this.server = sign.getLine(1);
        this.playerInformationLineNumber = playerInformationLineNumber;

        this.format = BungeeTeleport.getInstance().getConfig().getString("CurrentPlayersOutOfMaxFormat");
    }

    public void update() {

        Player player = BungeeTeleport.getDummyPlayer();

        if (player == null) { // No players on server

            return; // No need to update signs if there are no players
        }

        ByteArrayDataOutput pluginMessage = ByteStreams.newDataOutput();

        pluginMessage.writeUTF("GetServers");

        player.sendPluginMessage(BungeeTeleport.getInstance(), "BungeeCord", pluginMessage.toByteArray());

        String currentNumberOfPlayers = String.valueOf(serverPlayerCounts.get(server));
    }
}
