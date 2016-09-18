package me.theeninja.bungeeteleport;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class SignClickListener implements Listener {
	public static List<String> serverList; 
	@EventHandler
	public void onSignClick(PlayerInteractEvent e) {
		if (!(e.getHand().equals(EquipmentSlot.HAND))) return;
		e.getPlayer().sendMessage("Event has been called.");
		if (!(e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			return;
		}
		Block block = e.getClickedBlock();
		if (!(block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST)) {
			return;
		}
		Sign sign = (Sign) block.getState();
		if (!signCheck(sign)) {
			e.getPlayer().sendMessage("Sign Check has failed.");
			return;
		}
		e.getPlayer().sendMessage("Event successfully processed.");
		e.getPlayer().sendMessage("Server pre-instance creation is: " + sign.getLine(1));
		ConnectPlayer connectPlayer = new ConnectPlayer(e.getPlayer(), sign.getLine(1));
		connectPlayer.updateServerList();
		Bukkit.getScheduler().runTaskLater(BungeeTeleport.getInstance(), new Runnable() {
			@Override
			public void run() {
				connectPlayer.connectPlayer();
			}			
		}, 10);
	}
	private boolean signCheck(Sign sign) {
		return ChatColor.stripColor(sign.getLine(0)).equals("[BungeeTeleport]");
	}
}
