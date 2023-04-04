package net.dmjin.shounensumo;

import net.dmjin.shounensumo.commands.SchematicPaster;
import net.dmjin.shounensumo.commands.ShounenSumoNPC;
import net.dmjin.shounensumo.listeners.MovementListener;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class ShounenSumo extends JavaPlugin {

    public static List<ServerPlayer> npcs = new ArrayList<>();

    @Override
    public void onEnable() {
        // Plugin startup logic

        // commands
        getCommand("arenagenerator").setExecutor(new SchematicPaster(this));
        getCommand("sumonpc").setExecutor(new ShounenSumoNPC(this));

        // listeners
        getServer().getPluginManager().registerEvents(new MovementListener(), this);

    }

    public static List<ServerPlayer> getNpcs() {
        return npcs;
    }

}