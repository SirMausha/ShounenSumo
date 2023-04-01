package net.dmjin.shounensumo.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import net.dmjin.shounensumo.ShounenSumo;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;

public class SchematicPaster implements CommandExecutor {

    private ShounenSumo plugin;

    public SchematicPaster(ShounenSumo plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player) || !sender.hasPermission("shounensumo.weditgen")) {
            sender.sendMessage("This command can only be run by a player.");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("Please provide the schematic name as an argument.");
            return true;
        }

        Player player = (Player) sender;
        Location location = player.getLocation();

        player.sendMessage("Loading schematic...");

        WorldEditPlugin worldEditPlugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
        if (worldEditPlugin == null) {
            player.sendMessage("WorldEdit plugin not found. Please ensure it is installed.");
            return true;
        }
        File worldEditDirectory = worldEditPlugin.getDataFolder().toPath().resolve("schematics").toFile();
        File file = new File(worldEditDirectory, args[0] + ".schem");


        // Load the clipboard
        ClipboardFormat format = ClipboardFormats.findByFile(file);
        Clipboard clipboard = null;
        try (ClipboardReader reader = format.getReader(Files.newInputStream(file.toPath()))) {
            clipboard = reader.read();
            player.sendMessage("Loaded schematic: " + file.getName() + "");
        } catch (IOException e) {
            player.sendMessage("Failed to load clipboard.");
        }

        // Paste the clipboard
        try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(BukkitAdapter.adapt(player.getWorld()), -1)) {
            player.sendMessage("Pasting clipboard...");
            Operation operation = new ClipboardHolder(clipboard)
                    .createPaste(editSession)
                    .to(BlockVector3.at(location.getX(), location.getY(), location.getZ()))
                    .ignoreAirBlocks(false)
                    .build();

            try {
                Operations.complete(operation);
                player.sendMessage("Schematic pasted successfully.");
            } catch (WorldEditException e) {
                player.sendMessage("Failed to paste schematic.");
            }
        }

        return true;
    }
}
