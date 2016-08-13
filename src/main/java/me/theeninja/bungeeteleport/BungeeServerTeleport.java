package me.theeninja.bungeeteleport;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class BungeeServerTeleport implements PluginMessageListener {
	private String[] serverList;
	BungeeServerTeleport(Player player, String server) {
		updateServers();
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Connect");
		out.writeUTF(server);
		player.sendMessage("Connecting to server " + server + "!");
		player.sendPluginMessage(BungeeTeleport.getInstance(), "BungeeCord", out.toByteArray());
	}
	public BungeeServerTeleport() {
	
	}
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals("BungeeCord")) {
			return;
		}
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String subchannel = in.readUTF();
		if (subchannel.equals("GetServers")) {
			serverList = in.readUTF().split(", ");
		}
	}
	protected static void updateServers() {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("GetServers");
	}
}
