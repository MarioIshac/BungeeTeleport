package me.theeninja.bungeeteleport;

import java.util.Arrays;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class ConnectPlayer implements PluginMessageListener {
	private String server;
	private Player player;
	ConnectPlayer() {

	}	
	public ConnectPlayer(Player player, String server) {
		this.server = server;
		this.player = player;
		player.sendMessage("BungeeServerTeleport instance created.");
		player.sendMessage("The server has been updated to " + this.server);
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
			SignClickListener.serverList = Arrays.asList(in.readUTF().split(", "));
			player.sendMessage(SignClickListener.serverList.toString());
		}
	}
	protected void connectPlayer() {
		if (player == null) {
			Bukkit.getLogger().log(Level.SEVERE, "Cannot connect player, player is null.");
		}
		player.sendMessage("Starting to connect player.");
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		player.sendMessage(SignClickListener.serverList.toString());
		player.sendMessage("Trying to connect to: >" + server + "<");
		if (SignClickListener.serverList.stream().anyMatch(serverInList -> serverInList.equals(server))) {
			out.writeUTF("Connect");
			out.writeUTF(server);
			player.sendMessage("Connecting to server " + server + "!");
			player.sendPluginMessage(BungeeTeleport.getInstance(), "BungeeCord", out.toByteArray());
		}
		else {
			Bukkit.getLogger().log(Level.SEVERE, "Cannot connect player, server is invalid.");
			player.sendMessage("Invalid server " + server);
		}
	}
	protected void updateServerList() {
		BungeeTeleport.getInstance().getServer().broadcastMessage("updateServerList called");
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("GetServers");
		BungeeTeleport.getInstance().getServer().broadcastMessage("serverList trying to be updated.");
		player.sendPluginMessage(BungeeTeleport.getInstance(), "BungeeCord", out.toByteArray());
	}
}
