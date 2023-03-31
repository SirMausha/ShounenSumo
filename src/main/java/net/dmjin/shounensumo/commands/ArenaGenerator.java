package net.dmjin.shounensumo.commands;

import net.dmjin.shounensumo.commands.manager.BlockStateManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Stack;


public class ArenaGenerator implements CommandExecutor {

    // Create a new instance of the BlockStateManager
    private BlockStateManager blockStateManager;
    public ArenaGenerator(BlockStateManager blockStateManager) {
        this.blockStateManager = blockStateManager;
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
        int x, y, z;
        Material material = Material.STONE;

        // Parse the radius argument if one was provided
        if (args.length >= 4) {
            try {
                radius = Integer.parseInt(args[0]);
                x = Integer.parseInt(args[1]);
                y = Integer.parseInt(args[2]);
                z = Integer.parseInt(args[3]);
            } catch (NumberFormatException e) { // If the arguments are invalid, notify the player and return
                player.sendMessage(ChatColor.RED + "Invalid arguments!");
                return true;
            }
        } else {
            player.sendMessage(ChatColor.RED + "Invalid arguments!");
            return true;
        }


        // Generate the arena
        generateArena(player, material, radius, x, y, z);

        // Notify the player and add modified block states to the stack
        sender.sendMessage(ChatColor.GREEN + "Arena generated!");
        blockStateManager.restoreBlockStates();
        blockStateManager = new BlockStateManager();


        return true;
    }

    private void generateArena(Player player, Material material, int radius, int x, int y, int z) {
        // Generate the arena
        World world = player.getWorld();

        // Generate the floor

        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                BlockState state = world.getBlockAt(x + i, y, z + j).getState();
                state.setType(material);
                blockStateManager.addBlockState(state);
            }
        }
    }
};
