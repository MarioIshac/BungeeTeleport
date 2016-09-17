package me.theeninja.bungeeteleport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class BungeeServerTeleport implements PluginMessageListener {
	private List<String> serverList = new ArrayList<String>();
	private String server;
	BungeeServerTeleport(Player player, String server) {
		this.server = server;
		player.sendMessage("BungeeServerTeleport instance created.");
		player.sendMessage("The server has been updated to " + this.server);
		updateServerList(player);
	}
	public BungeeServerTeleport() {
	
	}
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		player.sendMessage("The plugin message has been received.");
		if (!channel.equals("BungeeCord")) {
			return;
		}
		player.sendMessage("The plugin message is of type BungeeCord.");
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String subchannel = in.readUTF();
		if (subchannel.equals("GetServers")) {
			BungeeTeleport.getInstance().getServer().broadcastMessage("The server list has been updated.");
			serverList = Arrays.asList(in.readUTF().split(", "));
			player.sendMessage(serverList.toString());
			player.sendMessage("Calling connect, player, server string is currently: " + server);
			connectPlayer(player);
		}
	}
	private void connectPlayer(Player player) {
		if (player == null) {
			Bukkit.broadcastMessage("Player is null");
		}
		player.sendMessage("Starting to connect player.");
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		player.sendMessage(serverList.toString());
		player.sendMessage("Trying to connect to: >" + server + "<");
		if (serverList.contains(server)) {
			out.writeUTF("Connect");
			out.writeUTF(server);
			player.sendMessage("Connecting to server " + server + "!");
			player.sendPluginMessage(BungeeTeleport.getInstance(), "BungeeCord", out.toByteArray());
		}
		else {
			player.sendMessage("Invalid server " + server);
		}
	}
	private void updateServerList(Player player) {
		BungeeTeleport.getInstance().getServer().broadcastMessage("updateServerList called");
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("GetServers");
		BungeeTeleport.getInstance().getServer().broadcastMessage("serverList trying to be updated.");
		player.sendPluginMessage(BungeeTeleport.getInstance(), "BungeeCord", out.toByteArray());
	}
}
