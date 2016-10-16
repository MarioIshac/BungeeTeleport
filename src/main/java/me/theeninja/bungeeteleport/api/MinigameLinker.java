package me.theeninja.bungeeteleport.api;

import org.bukkit.plugin.Plugin;

/**
 * Application User Interface for registering (API)
 * private "minigame" plugins.
 *
 * @author TheeNinja
 */

class MinigameLinker {
    private static MinigameLinker instance = new MinigameLinker();

    public static MinigameLinker getAPI() {
        return instance;
    }

    public void registerLink(Plugin plugin) {

    }
}





