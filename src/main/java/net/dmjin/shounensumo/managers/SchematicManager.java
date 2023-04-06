package net.dmjin.shounensumo.managers;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.EditSessionBuilder;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import net.dmjin.shounensumo.ShounenSumo;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileInputStream;

public class SchematicManager {

    private ShounenSumo plugin;

    // constructor
    public SchematicManager() {
        this.plugin = plugin;
    }

    public void pasteSchematic(com.sk89q.worldedit.world.World world, Location location, String schematicName) {
        // Get the clipboard
        Clipboard clipboard = null;
        try {
            // Load the schematic from FAWE schematic folder
            clipboard = ClipboardFormats.findByFile(new File("plugins/FastAsyncWorldEdit/schematics/" + schematicName + ".schem")).getReader(new FileInputStream(new File("plugins/FastAsyncWorldEdit/schematics/" + schematicName + ".schem"))).read();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (clipboard == null) {
            return;
        }

        // Create a new edit session
        EditSessionBuilder builder = WorldEdit.getInstance().newEditSessionBuilder();

        // Set the maximum number of blocks to change to -1 to allow unlimited
        builder.maxBlocks(-1);

        // Paste the clipboard
        pasteClipboard(world, location, clipboard);
    }

    private void pasteClipboard(World world, Location location, Clipboard clipboard) {
        new BukkitRunnable() {
            @Override
            public void run() {

                // Create a new edit session
                EditSessionBuilder builder = WorldEdit.getInstance().newEditSessionBuilder();

                // Set the maximum number of blocks to change to -1 to allow unlimited
                builder.maxBlocks(-1);

                try (EditSession editSession = builder.world(world).build()) {
                    Operation operation = new ClipboardHolder(clipboard)
                            .createPaste(editSession)
                            .to(BlockVector3.at(location.getX(), location.getY(), location.getZ()))
                            .ignoreAirBlocks(false)
                            .build();

                    // Execute the operation
                    try {
                        Operations.complete(operation);
                        plugin.getLogger().info("Schematic pasted successfully.");
                    } catch (WorldEditException e) {
                        plugin.getLogger().severe("Failed to paste schematic.");
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    plugin.getLogger().severe("An error occurred while pasting the schematic.");
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }
}
