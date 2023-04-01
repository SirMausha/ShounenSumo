package net.dmjin.shounensumo;

import net.dmjin.shounensumo.commands.ArenaUndo;
import net.dmjin.shounensumo.commands.SchematicPaster;
import org.bukkit.plugin.java.JavaPlugin;

public final class ShounenSumo extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        getCommand("arenaundo").setExecutor(new ArenaUndo());
        getCommand("weditgenerator").setExecutor(new SchematicPaster(this));
    }
}