package org.defaultmod.runtime;

import net.minecraft.client.MinecraftClient;
import org.defaultmod.config.DefaultConfig;

public final class PerformanceGovernor {
    private static long lastAdjustNs = 0L;
    private PerformanceGovernor() {}

    public static void onFrame(MinecraftClient mc) {
        if (!DefaultConfig.adaptiveEnabled || mc == null || mc.options == null) return;
        long now = System.nanoTime();
        if (lastAdjustNs == 0L) { lastAdjustNs = now; return; }
        if ((now - lastAdjustNs) < 5_000_000_000L) return; // 5 seconds cooldown
        lastAdjustNs = now;

        // Compute recent FPS EMA using last samples
        float[] fps = PerfHistory.getFps();
        double sum = 0; int n = fps.length; for (float f : fps) sum += f; double avg = sum / Math.max(1, n);
        double target = DefaultConfig.adaptiveTargetFps;
        var opt = mc.options;

        // Adjust view distance and chunk rebuild budget based on deviation
        if (avg < target * 0.95) {
            // below target: reduce
            int vd = opt.getViewDistance().getValue();
            if (vd > 2) opt.getViewDistance().setValue(Math.max(2, vd - 1));
            if (DefaultConfig.clientChunkRebuildBudgetEnabled) {
                DefaultConfig.clientChunkRebuildsPerFrame = Math.max(1, DefaultConfig.clientChunkRebuildsPerFrame - 1);
            }
        } else if (avg > target * 1.05) {
            // above target: increase within sane limits
            int vd = opt.getViewDistance().getValue();
            if (vd < 32) opt.getViewDistance().setValue(Math.min(32, vd + 1));
            if (DefaultConfig.clientChunkRebuildBudgetEnabled) {
                DefaultConfig.clientChunkRebuildsPerFrame = Math.min(20, DefaultConfig.clientChunkRebuildsPerFrame + 1);
            }
        }
    }
}

