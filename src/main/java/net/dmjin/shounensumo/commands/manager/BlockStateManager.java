package net.dmjin.shounensumo.commands.manager;

import org.bukkit.block.BlockState;

import java.util.Stack;

public class BlockStateManager {
    private Stack<BlockState> blockStates = new Stack<>();

    public void addBlockState(BlockState state) {
        // Debug message
        System.out.println("Adding block state: " + state.toString());

        blockStates.push(state);
    }

    public void restoreBlockStates() {
        while (!blockStates.empty()) {
            BlockState state = blockStates.pop();
            state.update(true, false);
        }
    }
}
