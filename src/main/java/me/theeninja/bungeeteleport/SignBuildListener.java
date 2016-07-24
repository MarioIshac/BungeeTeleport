package me.theeninja.bungeeteleport;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignBuildListener implements Listener {
	@EventHandler
	public void onSignBuild(SignChangeEvent e) {
		Sign s = (Sign) e.getBlock().getState();
		e.getPlayer().sendMessage("waddup");
		if (s.getLine(0).equalsIgnoreCase("[Bungee]")) {
			e.getPlayer().sendMessage("3");
			return;
		}
		Player p = e.getPlayer();
		if (!p.isOp()) {
			p.sendMessage("No permission.");
		}
		s.setLine(0, ChatColor.DARK_PURPLE + "[BungeeTeleport");
	}
}
