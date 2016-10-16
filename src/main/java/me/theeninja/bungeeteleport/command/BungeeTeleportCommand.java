package me.theeninja.bungeeteleport.command;

import me.theeninja.bungeeteleport.BungeeTeleport;
import me.theeninja.bungeeteleport.BungeeTeleportMessageDefaults;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * BungeeTeleport command implementation, includes implementation
 * for /bungeeteleport
 *
 * @author TheeNinja
 */
public class BungeeTeleportCommand implements CommandExecutor {

    /**
     * Handling for /bungeeteleport
     *
     * @param commandSender - command executor (such as a player or console)
     * @param command       - command object
     * @param commandLabel  - command name
     * @param commandArgs   - arguements of the command
     * @return boolean - whether the command was successfully processed
     */
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String commandLabel, String[] commandArgs) {

        if (command.getLabel().equalsIgnoreCase("bungeeteleport")) {

            if (commandArgs.length == 0) {

                commandSender.sendMessage(BungeeTeleportMessageDefaults.DEFAULT_MESSAGE_PREFIX + ChatColor.LIGHT_PURPLE +
                        "Invalid number of arguments.");
                return true;
            }

            switch (commandArgs[0].toLowerCase()) {

                case "config": {

                    if (commandArgs.length == 1) { // If this happens to be true, the one and only command argument would be "config"

                        commandSender.sendMessage(BungeeTeleportMessageDefaults.DEFAULT_MESSAGE_PREFIX + ChatColor.LIGHT_PURPLE +
                                "Invalid number of arguments.");
                        return true;
                    }

                    switch (commandArgs[1].toLowerCase()) {

                        case "save": { // Save configuration

                            BungeeTeleport.getInstance().saveConfig();
                            commandSender.sendMessage(BungeeTeleportMessageDefaults.DEFAULT_MESSAGE_PREFIX + ChatColor.LIGHT_PURPLE +
                                    "Saved configuration.");

                            return true;
                        }

                        case "reload": { // Reload configuration

                            BungeeTeleport.getInstance().reloadConfig();
                            commandSender.sendMessage(BungeeTeleportMessageDefaults.DEFAULT_MESSAGE_PREFIX + ChatColor.LIGHT_PURPLE +
                                    "Reloaded configuration.");

                            return true;
                        }

                        case "set": { // Set <key> to <value> in configuration

                            if (commandArgs.length < 4) {

                                commandSender.sendMessage(BungeeTeleportMessageDefaults.DEFAULT_MESSAGE_PREFIX + ChatColor.LIGHT_PURPLE +
                                        "Invalid number of arguments. Please use /bungeeteleport config set <key> <value>!");
                                return true;
                            }

                            StringBuilder configurationValueStringBuilder = new StringBuilder();

                            for (int valueArgCounter = 3; valueArgCounter < commandArgs.length; valueArgCounter++) {

                                configurationValueStringBuilder.append(commandArgs[valueArgCounter]).append(" ");
                            }

                            String configurationValue = configurationValueStringBuilder.toString();
                            configurationValue = configurationValue.substring(0, configurationValue.length() - 1);

                            BungeeTeleport.getInstance().getConfig().set(commandArgs[2], configurationValue);

                            commandSender.sendMessage(BungeeTeleportMessageDefaults.DEFAULT_MESSAGE_PREFIX + ChatColor.LIGHT_PURPLE +
                                    "Set " + commandArgs[2] + " to " +
                                    ChatColor.translateAlternateColorCodes('&', configurationValue));

                            BungeeTeleport.getInstance().saveConfig();
                            return true;
                        }

                        case "get": { // Send the player the value associated with <key> {

                            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    BungeeTeleport.getInstance().getConfig().getString(commandArgs[2])));

                            return true;
                        }
                    }
                }

                default: { // Runs if there is no default handling for the second sub-category of bungeeteleport (ie, config)

                    commandSender.sendMessage(BungeeTeleportMessageDefaults.DEFAULT_MESSAGE_PREFIX + ChatColor.LIGHT_PURPLE +
                            "Unrecognized parameter " + "\"" + commandArgs[0] + "\".");
                }
            }
        }
        return true;
    }
}
