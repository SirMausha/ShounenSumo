package net.dmjin.shounensumo.managers;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import net.dmjin.shounensumo.ShounenSumo;
import net.dmjin.shounensumo.commands.SchematicPaster;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;

public class WorldManager {

    MultiverseCore core = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");

    MVWorldManager worldManager = core.getMVWorldManager();

    ShounenSumo plugin = (ShounenSumo) Bukkit.getServer().getPluginManager().getPlugin("ShounenSumo");
    SchematicPaster schematicPaster = new SchematicPaster(plugin);


    // Method to create a Multiverse World
    public void createWorld() {

        String worldName = "ArenaWorld";

        if (core == null) {
            System.out.println("Multiverse core plugin not found.");
            return;
        }

        if (Bukkit.getWorld(worldName) != null) {
            System.out.println("World " + worldName + " already exists.");
            return;
        }

        System.out.println("Creating world");

        worldManager.addWorld(
                worldName,
                World.Environment.NORMAL,
                null,
                WorldType.NORMAL,
                false,
                "VoidGen"
        );

        World world = Bukkit.getWorld(worldName);
        Location spawnLocation = world.getSpawnLocation();
        com.sk89q.worldedit.world.World worldEditWorld = BukkitAdapter.adapt(world);
    }

    public void deleteWorld(String worldName) {

        if (core == null) {
            System.out.println("Multiverse core plugin not found.");
            return;
        }
        System.out.println("Deleting world");
        worldManager.deleteWorld(worldName);
    }

    public void worldTeleport(Player player, String worldName) {
        MultiverseCore core = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
        if (core == null) {
            player.sendMessage("Multiverse core plugin not found.");
            return;
        }

        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            player.sendMessage("World not found.");
            return;
        }

        Location spawnLocation = world.getSpawnLocation();
        player.teleport(spawnLocation);
        player.sendMessage("Teleported to " + worldName);
    }



}
