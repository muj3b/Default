package org.defaultmod.runtime;

import org.defaultmod.config.DefaultConfig;

public final class ServerParticleFilter {
    private ServerParticleFilter() {}

    public static boolean shouldDropBurst(net.minecraft.server.world.ServerWorld world) {
        if (!DefaultConfig.serverParticlesEnabled) return false;
        double p = DefaultConfig.serverParticlesDropFraction + ServerAdaptiveTuner.getExtraDrop() + dimensionBonus(world);
        if (p <= 0) return false;
        return Math.random() < Math.min(1.0, p);
    }

    public static int scaleCount(net.minecraft.server.world.ServerWorld world, int count) {
        if (!DefaultConfig.serverParticlesEnabled) return count;
        double keep = 1.0 - Math.min(1.0, DefaultConfig.serverParticlesDropFraction + ServerAdaptiveTuner.getExtraDrop() + dimensionBonus(world));
        int scaled = (int) Math.floor(count * keep);
        if (scaled < 0) scaled = 0;
        return scaled;
    }

    private static double dimensionBonus(net.minecraft.server.world.ServerWorld world) {
        if (!DefaultConfig.serverParticlesDimensionBonuses) return 0.0;
        String path = world.getRegistryKey().getValue().getPath();
        return switch (path) {
            case "overworld" -> DefaultConfig.serverParticlesDropBonusOverworld;
            case "the_nether", "nether" -> DefaultConfig.serverParticlesDropBonusNether;
            case "the_end", "end" -> DefaultConfig.serverParticlesDropBonusEnd;
            default -> 0.0;
        };
    }
}
