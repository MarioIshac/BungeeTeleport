package me.theeninja.bungeeteleport;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignBuildListener implements Listener {
	@EventHandler
	public void onSignBuild(SignChangeEvent e) {
		if (!e.getLine(0).equalsIgnoreCase("[BungeeTeleport]")) {
			return;
		}
		Player p = e.getPlayer();
		if (!p.isOp()) {
			p.sendMessage("You are not an operator; sign not created.");
			return;
		}
		e.setLine(0, ChatColor.DARK_PURPLE + "[BungeeTeleport]");
		e.setLine(2, ChatColor.RED + e.getLine(2));
		e.setLine(3, ChatColor.RED + e.getLine(3));
	}
}
