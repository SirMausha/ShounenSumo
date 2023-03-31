package net.dmjin.shounensumo;

import net.dmjin.shounensumo.commands.ArenaGenerator;
import net.dmjin.shounensumo.commands.ArenaUndo;
import net.dmjin.shounensumo.commands.manager.BlockStateManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class ShounenSumo extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        BlockStateManager blockStateManager = new BlockStateManager();

        getCommand("arenagen").setExecutor(new ArenaGenerator(blockStateManager));
        getCommand("arenaundo").setExecutor(new ArenaUndo(blockStateManager));
    }

}
