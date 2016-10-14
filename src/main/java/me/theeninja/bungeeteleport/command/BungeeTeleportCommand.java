package me.theeninja.bungeeteleport.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.theeninja.bungeeteleport.BungeeTeleport;

public class BungeeTeleportCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String commandLabel, String[] commandArgs) {
		

		BungeeTeleport.getInstance().saveConfig();
		return true;
	}	
}
