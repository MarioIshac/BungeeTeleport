package me.theeninja.bungeeteleport;

import me.theeninja.bungeeteleport.server.playerinformation.SignPlayerInformationUpdateHandler;
import org.bukkit.*;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;

import java.util.logging.Level;

/**
 * Event listener for when a player builds a BungeeTeleport
 * sign.
 *
 * @author TheeNinja
 */

class SignBuildListener implements Listener {

    @EventHandler
    public void onSignBuild(SignChangeEvent e) {

        // If not creating BungeeTeleport sign, do not do anything
        if (!e.getLine(0).equalsIgnoreCase("[BungeeTeleport]")) {
            return;
        }

        Player p = e.getPlayer();

        if (!p.isOp()) {
            p.sendMessage("You are not an operator; sign not created.");
            return;
        }

        Bukkit.getLogger().log(Level.INFO, "Created BungeeTeleport sign with player information option: " + e.getLine(2).equalsIgnoreCase("p"));
        Bukkit.getLogger().log(Level.INFO, "Target server sign assigned to: " + e.getLine(1));

        if (e.getLine(2).equalsIgnoreCase("p")) { // Optional server player information option

            Location signLocation = e.getBlock().getLocation();

            BungeeTeleport.getInstance().getConfig().getStringList("PLAYER_UPDATE_SIGNS").add(serializeLocation(signLocation));

            SignPlayerInformationUpdateHandler handler = new SignPlayerInformationUpdateHandler(e.getLine(1), signLocation);

            handler.registerUpdates();
        }

        // Update colors of sign to correlate with colors of valid BungeeTeleport sign
        e.setLine(0, ChatColor.DARK_PURPLE + "[BungeeTeleport]");
        e.setLine(2, ChatColor.RED + e.getLine(2));
        e.setLine(3, ChatColor.RED + e.getLine(3));
    }

    @EventHandler
    public void onSignBreak(BlockBreakEvent e) {

        if (!(e.getBlock().getType() == Material.SIGN ||
                e.getBlock().getType() == Material.SIGN_POST ||
              e.getBlock().getType() == Material.WALL_SIGN)) {
            return;
        }

        Sign sign = (Sign) e.getBlock().getState();

        // If not creating BungeeTeleport sign, do not do anything
        if (!sign.getLine(0).equalsIgnoreCase("[BungeeTeleport]")) {
            return;
        }

        Player p = e.getPlayer();

        if (!p.isOp()) {
            p.sendMessage("You are not an operator; sign not broken.");
            return;
        }
    }

    String serializeLocation(Location signLocation) {

        int signLocationX = signLocation.getBlockX();
        int signLocationY = signLocation.getBlockY();
        World signLocationWorld = signLocation.getWorld();
        int signLocationZ = signLocation.getBlockZ();

        return signLocationX + ":" + signLocationY + ":" + signLocationZ + ":" + signLocationWorld;
    }

    Location deserializeLocation(String string) {

        String[] locationElements = string.split(":");

        int signLocationX = Integer.parseInt(locationElements[0]);
        int signLocationY = Integer.parseInt(locationElements[1]);
        int signLocationZ = Integer.parseInt(locationElements[2]);
        World signLocationWorld = Bukkit.getWorld(locationElements[3]);

        return new Location(signLocationWorld, signLocationX, signLocationY, signLocationZ);
    }
}
