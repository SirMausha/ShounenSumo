package net.dmjin.shounensumo;

import net.dmjin.shounensumo.commands.ArenaGenerator;
import net.dmjin.shounensumo.commands.ArenaUndo;
import org.bukkit.plugin.java.JavaPlugin;

public final class ShounenSumo extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("arenagen").setExecutor(new ArenaGenerator(this));
        getCommand("arenaundo").setExecutor(new ArenaUndo());
    }
}