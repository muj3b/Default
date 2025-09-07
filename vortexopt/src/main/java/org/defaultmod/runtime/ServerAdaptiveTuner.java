package org.defaultmod.runtime;

import org.defaultmod.config.DefaultConfig;

public final class ServerAdaptiveTuner {
    private static volatile long lastTickNs = 0L;
    private static volatile double extraDrop = 0.0; // additional server particle drop
    private static volatile long lastLogNs = 0L;

    private ServerAdaptiveTuner() {}

    public static void onServerTick() {
        if (!DefaultConfig.serverAdaptiveEnabled) return;
        long now = System.nanoTime();
        long prev = lastTickNs;
        lastTickNs = now;
        if (prev == 0L) return;
        double dt = (now - prev) / 1_000_000_000.0;
        if (dt <= 0) return;
        double tps = 1.0 / dt;
        double err = DefaultConfig.serverAdaptiveTargetTps - tps; // positive if below target
        double norm = err / Math.max(1.0, DefaultConfig.serverAdaptiveTargetTps);
        double range = Math.max(0.0, DefaultConfig.serverAdaptiveMaxExtraDrop - DefaultConfig.serverAdaptiveMinExtraDrop);
        double delta = DefaultConfig.serverAdaptiveResponse * norm * range;
        double next = clamp(extraDrop + delta, DefaultConfig.serverAdaptiveMinExtraDrop, DefaultConfig.serverAdaptiveMaxExtraDrop);
        double alpha = 0.25;
        extraDrop = alpha * next + (1 - alpha) * extraDrop;

        if (DefaultConfig.metricsEnabled) {
            if (lastLogNs == 0L) lastLogNs = now;
            if ((now - lastLogNs) / 1_000_000_000.0 >= DefaultConfig.metricsServerLogSeconds) {
                lastLogNs = now;
                long bursts = org.defaultmod.runtime.Metrics.serverParticleBurstsDropped.getAndSet(0);
                long scaled = org.defaultmod.runtime.Metrics.serverParticlesScaled.getAndSet(0);
                long mergeSkips = org.defaultmod.runtime.Metrics.serverItemMergeSkips.getAndSet(0);
                System.out.printf("[DefaultMod] Server TPS=%.2f extraDrop=%.2f droppedBursts=%d scaled=%d mergeSkips=%d\n", tps, getExtraDrop(), bursts, scaled, mergeSkips);
            }
        }
    }

    public static double getExtraDrop() {
        return DefaultConfig.serverAdaptiveEnabled ? clamp(extraDrop, DefaultConfig.serverAdaptiveMinExtraDrop, DefaultConfig.serverAdaptiveMaxExtraDrop) : 0.0;
    }

    private static double clamp(double v, double lo, double hi) {
        return Math.max(lo, Math.min(hi, v));
    }
}
