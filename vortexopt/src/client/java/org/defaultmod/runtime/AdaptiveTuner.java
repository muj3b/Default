package org.defaultmod.runtime;

import org.defaultmod.config.DefaultConfig;

public final class AdaptiveTuner {
    private static volatile long lastFrameNs = 0L;
    private static volatile double extraDrop = 0.0; // additional particle drop [min,max]
    private static volatile long lastLogNs = 0L;

    private AdaptiveTuner() {}

    public static void onFrame(float tickDelta) {
        if (!DefaultConfig.adaptiveEnabled) return;
        long now = System.nanoTime();
        long prev = lastFrameNs;
        lastFrameNs = now;
        if (prev == 0L) return;
        double dt = (now - prev) / 1_000_000_000.0;
        if (dt <= 0) return;
        double fps = 1.0 / dt;
        org.defaultmod.runtime.PerfHistory.pushFps(fps);
        double err = DefaultConfig.adaptiveTargetFps - fps; // positive if below target

        // Normalize error and apply a simple I-controller style adjustment
        double norm = err / Math.max(1.0, DefaultConfig.adaptiveTargetFps);
        double range = Math.max(0.0, DefaultConfig.adaptiveMaxExtraDrop - DefaultConfig.adaptiveMinExtraDrop);
        double delta = DefaultConfig.adaptiveResponse * norm * range;

        double next = clamp(extraDrop + delta, DefaultConfig.adaptiveMinExtraDrop, DefaultConfig.adaptiveMaxExtraDrop);
        // Smooth
        double alpha = 0.25; // EMA smoothing factor for stability
        extraDrop = alpha * next + (1 - alpha) * extraDrop;

        // Metrics logging
        if (DefaultConfig.metricsEnabled) {
            if (lastLogNs == 0L) lastLogNs = now;
            if ((now - lastLogNs) / 1_000_000_000.0 >= DefaultConfig.metricsClientLogSeconds) {
                lastLogNs = now;
                long considered = org.defaultmod.runtime.Metrics.clientParticlesConsidered.getAndSet(0);
                long dropped = org.defaultmod.runtime.Metrics.clientParticlesDropped.getAndSet(0);
                System.out.printf("[DefaultMod] Client FPS=%.1f dropBase=%.2f considered=%d dropped=%d\n", fps, getDropBase(), considered, dropped);
            }
        }
    }

    public static double getExtraDrop() {
        return DefaultConfig.adaptiveEnabled ? clamp(extraDrop, DefaultConfig.adaptiveMinExtraDrop, DefaultConfig.adaptiveMaxExtraDrop) : 0.0;
    }

    public static double getDropBase() {
        return clamp(DefaultConfig.dropFraction + getExtraDrop(), 0.0, 1.0);
    }

    private static double clamp(double v, double lo, double hi) {
        return Math.max(lo, Math.min(hi, v));
    }
}
