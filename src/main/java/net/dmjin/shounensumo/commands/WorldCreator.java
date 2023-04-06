package net.dmjin.shounensumo.commands;

import net.dmjin.shounensumo.ShounenSumo;
import net.dmjin.shounensumo.managers.WorldManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class WorldCreator implements CommandExecutor {

    private final ShounenSumo plugin;

    // constructor
    public WorldCreator(ShounenSumo plugin) {
        this.plugin = plugin;
    }

    WorldManager worldManager = new WorldManager();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length == 0) {
            sender.sendMessage("Invalid arguments.");
            return true;
        }

        if (args[0].equals("create")) {

            // If world already exists, return
            worldManager.createWorld();
            String worldName = "ArenaWorld";

            if (sender instanceof Player) {
                worldManager.worldTeleport((Player) sender, worldName);
            }

        } else if (args[0].equals("delete")) {
            if (args.length != 2) {
                sender.sendMessage("Invalid arguments.");
                return true;
            }
            System.out.println("Deleting world: " + args[1]);
            worldManager.deleteWorld(args[1]);
        }
        return true;
    }

}
