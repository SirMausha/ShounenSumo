package net.dmjin.shounensumo.commands;


import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.EditSessionBuilder;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import net.dmjin.shounensumo.ShounenSumo;
import net.dmjin.shounensumo.managers.SchematicManager;
import net.minecraft.world.level.storage.WorldData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


public class SchematicPaster implements CommandExecutor {

    private final ShounenSumo plugin;

    // constructor
    public SchematicPaster(ShounenSumo plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player) || !sender.hasPermission("shounensumo.admin")) {
            sender.sendMessage("You do not have permission to use this command.");
            return true;
        }

        Player player = (Player) sender;
        Location location = player.getLocation();
        SchematicManager schematicManager = new SchematicManager();

        if (args.length == 0) {
            sender.sendMessage("Invalid arguments.");
            return true;
        }

        if (args[0].equals("create")) {
            if (args.length != 2) {
                sender.sendMessage("Invalid arguments.");
                return true;
            }
            World world = BukkitAdapter.adapt(player.getWorld());
            String schematicName = args[1];
            schematicManager.pasteSchematic(world, location, schematicName);
        }





        return true;
    }
}
