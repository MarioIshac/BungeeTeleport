package me.theeninja.bungeeteleport.minigame;

import de.ftbastler.bukkitgames.api.BukkitGamesAPI;

/**
 * Minigame Integration with BukkitGames on Spigot.
 *  
 * @author TheeNinja
 *
 */

public class BukkitGamesMiniGame implements ConnectPlayerMinigame {
	BukkitGamesAPI bukkitGamesAPI = BukkitGamesAPI.getApi();
	@Override
	public boolean canConnect() {
		return true;
	}	
}
