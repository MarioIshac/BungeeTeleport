package me.theeninja.bungeeteleport.server;

import me.theeninja.bungeeteleport.BungeeTeleport;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.List;

/**
 * Listener for when a player clicks a BungeeTeleport
 * sign. Afterwards the player is connected to the server
 * specified on the sign.
 *
 * @author TheeNinja
 */

public class SignClickListenerServer implements Listener {

    // Stores list of servers - updated when player attempts to connect to a server
    static List<String> serverList;

    @EventHandler
    public void onSignClick(PlayerInteractEvent e) {

        // Otherwise, two events are thrown as opposed to one
        if (!(e.getHand().equals(EquipmentSlot.HAND))) return;

        if (!(e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        Block block = e.getClickedBlock();

        if (!(block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST)) {
            return;
        }

        Sign sign = (Sign) block.getState();

        if (!signCheck(sign)) {
            return;
        }
        // Create a new instance in order to connect the player
        ConnectPlayerServer connectPlayer = new ConnectPlayerServer(e.getPlayer(), sign.getLine(1));

        // Updated the serverList variable in this class in order to check
        // whether the server on the second line of the sign is valid
        connectPlayer.updateServerList();

        // Connect the player 1/2 of a second later; otherwise the server
        // list would not be updated in time
        // * subject to change
        Bukkit.getScheduler().runTaskLater(BungeeTeleport.getInstance(), connectPlayer::connectPlayer, 10);
    }

    /**
     * Returns a boolean stating whether the sign object passed in as a
     * parameter is a valid BungeeTeleport sign. A valid BungeeTeleport
     * sign consists of a [BungeeTeleport] as the first line of the
     * sign
     *
     * @param sign - method tests whether sign parameter is a valid BungeeTeleport sign
     * @return true if the sign is a valid BungeeTeleport sign, otherwise false
     */
    private boolean signCheck(Sign sign) {

        return ChatColor.stripColor(sign.getLine(0)).equals("[BungeeTeleport]");
    }
}
