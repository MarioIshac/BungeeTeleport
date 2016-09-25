package me.theeninja.bungeeteleport.api;

import org.bukkit.plugin.Plugin;

/**
 * Application User Interface for registering (API)
 * private "minigame" plugins.
 * 
 * @author TheeNinja
 *
 */

public class MinigameLinker {
	private static MinigameLinker instance = new MinigameLinker(); 
	public void registerLink(Plugin plugin) {

	}
	public static MinigameLinker getAPI() {
		return instance;
	}
}





