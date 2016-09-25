package me.theeninja.bungeeteleport.minigame;

/**
 * Interface for all classes handling plugin-specific
 * connection conditions.
 * 
 * @author TheeNinja
 *
 */

public interface ConnectPlayerMinigame {
	
	/**
	 * Implementation for whether the player can connect
	 * to minigame server or no.
	 * 
	 * @return Player can connect to minigame server, or no.
	 * 		   Default: true
	 */
	default boolean canConnect() {
		return true;
	}
}
