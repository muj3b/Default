package org.defaultmod.runtime;

import org.defaultmod.config.DefaultConfig;

public final class ParticleBudget {
    private static final int BUCKET_MS = 50; // 20 buckets/second
    private static final int NUM_BUCKETS = 20;
    private static final int[] buckets = new int[NUM_BUCKETS];
    private static volatile long lastTickMs = System.currentTimeMillis();
    private static volatile int currentBucket = 0;

    private ParticleBudget() {}

    public static void onNewParticle() {
        roll();
        buckets[currentBucket]++;
    }

    public static boolean shouldDrop() {
        if (!DefaultConfig.particleBudgetingEnabled) return false;
        roll();
        int total = 0;
        for (int b : buckets) total += b;
        return total > DefaultConfig.maxNewParticlesPerSecond && Math.random() < DefaultConfig.dropFraction;
    }

    private static void roll() {
        long now = System.currentTimeMillis();
        long elapsed = now - lastTickMs;
        if (elapsed <= 0) return;
        int steps = (int) (elapsed / BUCKET_MS);
        if (steps > 0) {
            for (int i = 0; i < Math.min(steps, NUM_BUCKETS); i++) {
                currentBucket = (currentBucket + 1) % NUM_BUCKETS;
                buckets[currentBucket] = 0;
            }
            lastTickMs = now - (elapsed % BUCKET_MS);
        }
    }
}

