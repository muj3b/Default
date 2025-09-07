package org.defaultmod.runtime;

import org.defaultmod.config.DefaultConfig;

public final class ChunkBudget {
    private static int tokens = 0;
    private ChunkBudget() {}

    public static void onFrame() {
        if (!DefaultConfig.clientChunkRebuildBudgetEnabled) return;
        tokens = DefaultConfig.clientChunkRebuildsPerFrame;
    }

    public static boolean tryAcquire() {
        if (!DefaultConfig.clientChunkRebuildBudgetEnabled) return true;
        if (tokens > 0) { tokens--; return true; }
        return false;
    }
}

