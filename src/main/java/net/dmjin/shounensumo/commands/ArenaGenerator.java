package net.dmjin.shounensumo.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ArenaGenerator implements CommandExecutor {

    private final Plugin plugin; // Add plugin variable to store the plugin instance

    // Constructor
    public ArenaGenerator(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {


        //Check if the sender is a player and if they have the permission
        if (!(sender instanceof Player) || !sender.hasPermission("shounensumo.arenagen")) {
            System.out.println("Command failed!");
            return true;
        }

        Player player = (Player) sender;

        // Default radius, declare x, y and z and material
        int radius = 10;
        int centerX, centerY, centerZ;
        Material material = Material.STONE;
        int resolution = 360;

        // Parse the radius argument if one was provided
        if (args.length >= 4) {
            try {
                radius = Integer.parseInt(args[0]);
                centerX = Integer.parseInt(args[1]);
                centerY = Integer.parseInt(args[2]);
                centerZ = Integer.parseInt(args[3]);
            } catch (NumberFormatException e) { // If the arguments are invalid, notify the player and return
                player.sendMessage(ChatColor.RED + "Invalid arguments!");
                return true;
            }
        } else {
            player.sendMessage(ChatColor.RED + "Invalid arguments!");
            return true;
        }

        player.sendMessage(ChatColor.GREEN + "Arena generation started...");

        // Run the generation in a separate thread
        int finalRadius = radius;

        generateArena(player, material, finalRadius, centerX, centerY, centerZ, resolution);

        player.sendMessage(ChatColor.GREEN + "Arena generated!");

        return true;
    }

    private void generateArena(Player player, Material material, int radius, int centerX, int centerY, int centerZ, int resolution) {
        // Get the world the player is in
        World world = player.getWorld();

        // Initialize variables for the circle drawing algorithm
        int x = radius;
        int y = 0;
        int decisionOver2 = 1 - x;

        // Loop over each point in the circle
        while (y <= x) {
            // Draw blocks along the top and bottom of the circle
            for (int i = -x; i <= x; i++) {
                world.getBlockAt(centerX + i, centerY, centerZ + y).setType(material);
                world.getBlockAt(centerX + i, centerY, centerZ - y).setType(material);
            }

            // Draw blocks along the left and right sides of the circle
            for (int i = -y; i <= y; i++) {
                world.getBlockAt(centerX + i, centerY, centerZ + x).setType(material);
                world.getBlockAt(centerX + i, centerY, centerZ - x).setType(material);
            }

            // Increment the current Y-coordinate
            y++;

            // Update the decision variable for the circle drawing algorithm
            if (decisionOver2 <= 0) {
                decisionOver2 += 2 * y + 1;
            } else {
                x--;
                decisionOver2 += 2 * (y - x) + 1;
            }
        }
    }
};
