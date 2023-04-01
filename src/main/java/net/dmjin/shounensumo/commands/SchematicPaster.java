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
import org.bukkit.scheduler.BukkitRunnable;

public class SchematicPaster implements CommandExecutor {

    private ShounenSumo plugin;

    public SchematicPaster(ShounenSumo plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player) || !sender.hasPermission("shounensumo.weditgen")) {
            sender.sendMessage("You do not have permission to use this command.");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("Please provide the schematic name as an argument.");
            return true;
        }


        Player player = (Player) sender;
        Location location = player.getLocation();

        // Paste method
        pasteSchematic(player, location, args[0]);

        return true;
    }

    public void pasteSchematic(Player player, Location location, String args) {

        // Check if WorldEdit is installed
        WorldEditPlugin worldEditPlugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
        if (worldEditPlugin == null) {
            player.sendMessage("WorldEdit plugin not found. Please ensure it is installed.");
            return;
        }

        // Get the schematic file
        File worldEditDirectory = worldEditPlugin.getDataFolder().toPath().resolve("schematics").toFile();
        File file = new File(worldEditDirectory, args + ".schem");


        // Load the clipboard
        loadClipboardAsync(player, location, file);
    }

    private void loadClipboardAsync(Player player, Location location, File file) {
        new BukkitRunnable() {
            @Override
            public void run() {
                // Load the clipboard
                ClipboardFormat format = ClipboardFormats.findByFile(file);
                Clipboard clipboard = null;
                try (ClipboardReader reader = format.getReader(Files.newInputStream(file.toPath()))) {
                    clipboard = reader.read();
                    player.sendMessage("Loaded schematic: " + file.getName() + "");
                } catch (NullPointerException | IOException e) {
                    player.sendMessage("Failed to load clipboard.");
                }

                // Paste the clipboard
                pasteClipboard(player, location, clipboard);
            }
        }.runTaskAsynchronously(plugin);
    }

    private void pasteClipboard(Player player, Location location, Clipboard clipboard) {
        new BukkitRunnable() {
            @Override
            public void run() {
                // Paste the clipboard
                try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(BukkitAdapter.adapt(player.getWorld()), -1)) {
                    Operation operation = new ClipboardHolder(clipboard)
                            .createPaste(editSession)
                            .to(BlockVector3.at(location.getX(), location.getY(), location.getZ()))
                            .ignoreAirBlocks(false)
                            .build();

                    // Execute the operation
                    try {
                        Operations.complete(operation);
                        player.sendMessage("Schematic pasted successfully.");
                    } catch (WorldEditException e) {
                        player.sendMessage("Failed to paste schematic.");
                    }
                }
            }
        }.runTask(plugin);
    }


}
