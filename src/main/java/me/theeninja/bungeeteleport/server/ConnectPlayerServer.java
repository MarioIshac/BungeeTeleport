package me.theeninja.bungeeteleport.server;

import java.util.Arrays;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import me.theeninja.bungeeteleport.BungeeTeleport;

/**
 * Implementation for connecting a player 
 * to a non-minigame server (mocking /server (server)
 * with a sign).
 * 
 * @author TheeNinja
 *
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
	public ConnectPlayerServer(Player player, String server) {
		this.server = server;
		this.player = player;
	}
	
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		
		if (!channel.equals("BungeeCord")) {
			return;
		}
		
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String subchannel = in.readUTF();
		
		if (subchannel.equals("GetServers")) {
			SignClickListenerServer.serverList = Arrays.asList(in.readUTF().split(", "));		
		}
	}
	
	/**
	 * Connects the player declared with the instance to the server
	 * declared in the instance.
	 */
	public void connectPlayer() {
		
		if (player == null) {
			Bukkit.getLogger().log(Level.SEVERE, "Cannot connect player, player is null.");
		}
		
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		
		if (SignClickListenerServer.serverList.stream().anyMatch(serverInList -> serverInList.equals(server))) {
			out.writeUTF("Connect");
			out.writeUTF(server);
			player.sendPluginMessage(BungeeTeleport.getInstance(), "BungeeCord", out.toByteArray());
		}
		
		else {
			
		}
	}
	
	/**
	 * Updates the list of online servers involved in the BungeeCord
	 * network. 
	 */
	public void updateServerList() {

		ByteArrayDataOutput out = ByteStreams.newDataOutput();	
		out.writeUTF("GetServers");
		player.sendPluginMessage(BungeeTeleport.getInstance(), "BungeeCord", out.toByteArray());
	}
}
