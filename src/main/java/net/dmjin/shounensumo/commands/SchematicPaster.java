package net.dmjin.shounensumo.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import net.dmjin.shounensumo.ShounenSumo;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
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
        Plugin fastAsyncWorldEditPlugin = Bukkit.getPluginManager().getPlugin("FastAsyncWorldEdit");
        if (fastAsyncWorldEditPlugin == null) {
            player.sendMessage("FastAsyncWorldEdit plugin not found. Please ensure it is installed.");
            return;
        }

        File schematicsFolder = new File(fastAsyncWorldEditPlugin.getDataFolder(), "schematics");
        File file = new File(schematicsFolder, args + ".schem");

        Clipboard clipboard = loadClipboard(file);
        if (clipboard != null) {
            pasteClipboard(player, location, clipboard);
        } else {
            player.sendMessage("Failed to load clipboard.");
        }
    }

    private Clipboard loadClipboard(File file) {
        ClipboardFormat format = ClipboardFormats.findByFile(file);
        try (ClipboardReader reader = format.getReader(Files.newInputStream(file.toPath()))) {
            return reader.read();
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void pasteClipboard(Player player, Location location, Clipboard clipboard) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    // Create a new edit session
                    World world = BukkitAdapter.adapt(player.getWorld());
                    EditSessionBuilder builder = WorldEdit.getInstance().newEditSessionBuilder();

                    // Set the maximum number of blocks to change to -1 to allow unlimited
                    builder.world(world).maxBlocks(-1);

                    // Paste the clipboard
                    try (EditSession editSession = builder.build()) {
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
                } catch (Exception e) {
                    player.sendMessage("An error occurred while pasting the schematic.");
                    e.printStackTrace();
                }
            }
        }.runTask(plugin);
    }





}

