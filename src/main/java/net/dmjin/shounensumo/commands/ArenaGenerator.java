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
        new BukkitRunnable() {
            @Override
            public void run() {
                // Generate the arena
                generateArena(player, material, finalRadius, centerX, centerY, centerZ);

                // Notify the player that the arena generation has finished
                player.sendMessage(ChatColor.GREEN + "Arena generated!");
            }
        }.runTaskAsynchronously(plugin);

        return true;
    }

    private void generateArena(Player player, Material material, int radius, int centerX, int centerY, int centerZ) {
        // Generate the arena
        World world = player.getWorld();

        // Generate the floor

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (x * x + z * z <= radius * radius) {
                    int blockX = centerX + x;
                    int blockY = centerY;
                    int blockZ = centerZ + z;

                    Block block = world.getBlockAt(blockX, blockY, blockZ);
                    block.setType(material);
                }
            }
        }
    }
};
