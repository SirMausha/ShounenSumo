package net.dmjin.shounensumo.commands;

import net.dmjin.shounensumo.commands.manager.BlockStateManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ArenaUndo implements CommandExecutor {

    // Create a new instance of the BlockStateManager
    private BlockStateManager blockStateManager;

    // Default constructor
    public ArenaUndo() {
        this.blockStateManager = new BlockStateManager();
    }

    // Constructor
    public ArenaUndo(BlockStateManager blockStateManager) {
        this.blockStateManager = blockStateManager;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        System.out.println("Executing /arenaundo command...");

        // Undo the arena
        blockStateManager.restoreBlockStates();
        return true;
    }
}
