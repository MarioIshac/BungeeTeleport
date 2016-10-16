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

	/**
	 * Specifies whether the player can connect
	 * to the hosted BukkitGames server
	 *
	 * @return If player can connect or no
	 *
	 */
	@Override
	public boolean canConnect() {

        return true;
	}
}
