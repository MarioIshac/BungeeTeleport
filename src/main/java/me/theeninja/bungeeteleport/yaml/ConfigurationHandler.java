package me.theeninja.bungeeteleport.yaml;

import me.theeninja.bungeeteleport.BungeeTeleport;

public class ConfigurationHandler {

    public static void setUpDefaultConfig() {

        BungeeTeleport.getInstance().saveDefaultConfig();
    }
}
