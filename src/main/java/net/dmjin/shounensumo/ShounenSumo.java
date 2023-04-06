package net.dmjin.shounensumo;

import net.dmjin.shounensumo.commands.SchematicPaster;
import net.dmjin.shounensumo.commands.ShounenSumoNPC;
import net.dmjin.shounensumo.commands.WorldCreator;
import net.dmjin.shounensumo.listeners.MovementListener;
import net.dmjin.shounensumo.managers.SchematicManager;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class ShounenSumo extends JavaPlugin {

    public static List<ServerPlayer> npcs = new ArrayList<>();

    public HashMap<Player, Location> schematicLocations = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("ShounenSumo has been enabled.");

        // commands
        getCommand("arenagenerator").setExecutor(new SchematicPaster(this));
        getCommand("sumonpc").setExecutor(new ShounenSumoNPC(this));
        getCommand("worldcreator").setExecutor(new WorldCreator(this));

        // listeners
        getServer().getPluginManager().registerEvents(new MovementListener(), this);

    }
    public static List<ServerPlayer> getNpcs() {
        return npcs;
    }
}