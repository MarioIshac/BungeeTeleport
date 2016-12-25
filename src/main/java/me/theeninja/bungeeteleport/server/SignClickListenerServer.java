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

import java.util.ArrayList;
import java.util.List;

/**
 * Listener for when a player clicks a BungeeTeleport
 * sign. Executes action depending on type of BungeeTeleport
 * sign and player.
 *
 * @author TheeNinja
 */
public class SignClickListenerServer implements Listener {

    /**
     * An {@link ArrayList} of {@link String}
     * representing the online servers on the BungeeCord network.
     * The {@link String} represents the name of each server.
     */
    public static List<String> serverList;

    /**
     * Handles the event where a player right clicks
     * a BungeeTeleport sign.
     * @param playerInteractEvent the event to be handled
     */
    @EventHandler
    public void onBungeeTeleportSignClick(PlayerInteractEvent playerInteractEvent) {

        // Otherwise, two events are thrown as opposed to one
        if (!(playerInteractEvent.getHand().equals(EquipmentSlot.HAND))) {

            return;
        }

        if (!(playerInteractEvent.getAction() == Action.RIGHT_CLICK_BLOCK)) {

            return;
        }

        Block block = playerInteractEvent.getClickedBlock();

        if (!(block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST)) {

            return;
        }

        Sign sign = (Sign) block.getState();

        if (!signCheck(sign)) {

            return;
        }

        // Create a new instance in order to connect the player
        ConnectPlayerServer connectPlayer = new ConnectPlayerServer(playerInteractEvent.getPlayer(), sign.getLine(1));

        // Updated the serverList variable in this class in order to check
        // whether the server on the second line of the sign is valid
        // connectPlayer.updateServerList();

        // Connect the player 1/2 of a second later; otherwise the server
        // list would not be updated in time
        // * subject to change
        Bukkit.getScheduler().runTaskLater(BungeeTeleport.getInstance(), connectPlayer::connectPlayer,
                // Configuration value represents seconds, multiplied by 20 to represent ticks
                (long) (20 * BungeeTeleport.getInstance().getConfig().getDouble("ServerConnectionDelay")));
    }

    /**
     * Returns a boolean stating whether the {@link Sign} passed in as a
     * parameter is a valid BungeeTeleport sign. A valid BungeeTeleport
     * sign consists of a [BungeeTeleport] identifier as the first line of the
     * sign
     *
     * @param sign - method tests whether sign parameter is a valid BungeeTeleport sign
     * @return       if the sign is a valid bungeeteleport sign
     */
    private boolean signCheck(Sign sign) {

        return ChatColor.stripColor(sign.getLine(0)).equals("[BungeeTeleport]");
    }
}