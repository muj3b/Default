package org.defaultmod.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class DefaultConfig {
    private static final String FILE_NAME = "default.properties";

    public static boolean particleBudgetingEnabled = true;
    public static int maxNewParticlesPerSecond = 800; // global budget across all types
    public static double dropFraction = 0.5; // when over budget, drop this fraction of new particles
    public static double particleMaxDistance = 64.0; // blocks; beyond this, extra drop bias may apply
    public static double particleOffscreenBonusDrop = 0.5; // add'l drop probability when offscreen
    public static double particleDistanceBonusDrop = 0.5; // add'l drop probability when beyond max distance

    public static boolean mathLutEnabled = true; // enable FastTrig via mixin
    public static boolean entityCollisionCacheEnabled = true; // cache trivial inside-wall checks per tick

    public static boolean adaptiveEnabled = true;
    public static int adaptiveTargetFps = 60;
    public static double adaptiveMinExtraDrop = 0.0;
    public static double adaptiveMaxExtraDrop = 0.4;
    public static double adaptiveResponse = 0.1; // tuning factor

    // Server-side knobs
    public static boolean serverParticlesEnabled = true;
    public static double serverParticlesDropFraction = 0.25; // base drop for server-sent particles
    public static boolean serverDisableSprintingParticles = true; // visually identical purpose, lower network/CPU

    public static boolean serverAdaptiveEnabled = true;
    public static int serverAdaptiveTargetTps = 20;
    public static double serverAdaptiveMinExtraDrop = 0.0;
    public static double serverAdaptiveMaxExtraDrop = 0.5;
    public static double serverAdaptiveResponse = 0.15;

    // AI/Pathfinding/randomevents
    public static boolean serverAiThrottleEnabled = false;
    public static double serverAiThrottleProbAtFullLoad = 0.35; // skip brain tick with this prob at full load
    public static boolean serverPathRecalcCooldownEnabled = true;
    public static int serverPathRecalcCooldownTicks = 10;
    public static boolean serverRandomTickScaleEnabled = true;
    public static double serverRandomTickMinFraction = 0.5; // never scale below this fraction

    // Item merge tweaks
    public static boolean serverItemMergeCooldownEnabled = true;
    public static int serverItemMergeCooldownTicks = 5;

    // Dimension-specific particle bonuses
    public static boolean serverParticlesDimensionBonuses = true;
    public static double serverParticlesDropBonusOverworld = 0.0;
    public static double serverParticlesDropBonusNether = 0.1;
    public static double serverParticlesDropBonusEnd = 0.05;

    // Metrics
    public static boolean metricsEnabled = true;
    public static int metricsClientLogSeconds = 5;
    public static int metricsServerLogSeconds = 5;

    // Hopper idle skip
    public static boolean serverHopperIdleSkipEnabled = true;
    public static int serverHopperIdleCheckIntervalTicks = 8;

    private DefaultConfig() {}

    public static void load() {
        try {
            Path configDir = getConfigDir();
            Files.createDirectories(configDir);
            Path file = configDir.resolve(FILE_NAME);
            Properties props = new Properties();
            if (Files.exists(file)) {
                try (FileInputStream fis = new FileInputStream(file.toFile())) {
                    props.load(fis);
                }
            }

            particleBudgetingEnabled = Boolean.parseBoolean(props.getProperty("particles.enabled", String.valueOf(particleBudgetingEnabled)));
            maxNewParticlesPerSecond = parseInt(props.getProperty("particles.max_new_per_second"), maxNewParticlesPerSecond);
            dropFraction = parseDouble(props.getProperty("particles.drop_fraction"), dropFraction);
            particleMaxDistance = parseDouble(props.getProperty("particles.max_distance"), particleMaxDistance);
            particleOffscreenBonusDrop = parseDouble(props.getProperty("particles.offscreen_bonus_drop"), particleOffscreenBonusDrop);
            particleDistanceBonusDrop = parseDouble(props.getProperty("particles.distance_bonus_drop"), particleDistanceBonusDrop);

            mathLutEnabled = Boolean.parseBoolean(props.getProperty("math.lut.enabled", String.valueOf(mathLutEnabled)));
            entityCollisionCacheEnabled = Boolean.parseBoolean(props.getProperty("entity.collision.cache_inside_wall", String.valueOf(entityCollisionCacheEnabled)));

            adaptiveEnabled = Boolean.parseBoolean(props.getProperty("adaptive.enabled", String.valueOf(adaptiveEnabled)));
            adaptiveTargetFps = parseInt(props.getProperty("adaptive.target_fps"), adaptiveTargetFps);
            adaptiveMinExtraDrop = parseDouble(props.getProperty("adaptive.min_extra_drop"), adaptiveMinExtraDrop);
            adaptiveMaxExtraDrop = parseDouble(props.getProperty("adaptive.max_extra_drop"), adaptiveMaxExtraDrop);
            adaptiveResponse = parseDouble(props.getProperty("adaptive.response"), adaptiveResponse);

            // write back to ensure defaults are present
            props.setProperty("particles.enabled", String.valueOf(particleBudgetingEnabled));
            props.setProperty("particles.max_new_per_second", String.valueOf(maxNewParticlesPerSecond));
            props.setProperty("particles.drop_fraction", String.valueOf(dropFraction));
            props.setProperty("particles.max_distance", String.valueOf(particleMaxDistance));
            props.setProperty("particles.offscreen_bonus_drop", String.valueOf(particleOffscreenBonusDrop));
            props.setProperty("particles.distance_bonus_drop", String.valueOf(particleDistanceBonusDrop));

            props.setProperty("math.lut.enabled", String.valueOf(mathLutEnabled));
            props.setProperty("entity.collision.cache_inside_wall", String.valueOf(entityCollisionCacheEnabled));

            props.setProperty("adaptive.enabled", String.valueOf(adaptiveEnabled));
            props.setProperty("adaptive.target_fps", String.valueOf(adaptiveTargetFps));
            props.setProperty("adaptive.min_extra_drop", String.valueOf(adaptiveMinExtraDrop));
            props.setProperty("adaptive.max_extra_drop", String.valueOf(adaptiveMaxExtraDrop));
            props.setProperty("adaptive.response", String.valueOf(adaptiveResponse));

            // Server-side
            serverParticlesEnabled = Boolean.parseBoolean(props.getProperty("server.particles.enabled", String.valueOf(serverParticlesEnabled)));
            serverParticlesDropFraction = parseDouble(props.getProperty("server.particles.drop_fraction", String.valueOf(serverParticlesDropFraction)), serverParticlesDropFraction);
            serverDisableSprintingParticles = Boolean.parseBoolean(props.getProperty("server.particles.disable_sprinting", String.valueOf(serverDisableSprintingParticles)));

            serverAdaptiveEnabled = Boolean.parseBoolean(props.getProperty("server.adaptive.enabled", String.valueOf(serverAdaptiveEnabled)));
            serverAdaptiveTargetTps = parseInt(props.getProperty("server.adaptive.target_tps"), serverAdaptiveTargetTps);
            serverAdaptiveMinExtraDrop = parseDouble(props.getProperty("server.adaptive.min_extra_drop"), serverAdaptiveMinExtraDrop);
            serverAdaptiveMaxExtraDrop = parseDouble(props.getProperty("server.adaptive.max_extra_drop"), serverAdaptiveMaxExtraDrop);
            serverAdaptiveResponse = parseDouble(props.getProperty("server.adaptive.response"), serverAdaptiveResponse);

            props.setProperty("server.particles.enabled", String.valueOf(serverParticlesEnabled));
            props.setProperty("server.particles.drop_fraction", String.valueOf(serverParticlesDropFraction));
            props.setProperty("server.particles.disable_sprinting", String.valueOf(serverDisableSprintingParticles));
            props.setProperty("server.adaptive.enabled", String.valueOf(serverAdaptiveEnabled));
            props.setProperty("server.adaptive.target_tps", String.valueOf(serverAdaptiveTargetTps));
            props.setProperty("server.adaptive.min_extra_drop", String.valueOf(serverAdaptiveMinExtraDrop));
            props.setProperty("server.adaptive.max_extra_drop", String.valueOf(serverAdaptiveMaxExtraDrop));
            props.setProperty("server.adaptive.response", String.valueOf(serverAdaptiveResponse));

            // AI/Pathfinding/random
            serverAiThrottleEnabled = Boolean.parseBoolean(props.getProperty("server.ai.throttle.enabled", String.valueOf(serverAiThrottleEnabled)));
            serverAiThrottleProbAtFullLoad = parseDouble(props.getProperty("server.ai.throttle.prob_full_load"), serverAiThrottleProbAtFullLoad);
            serverPathRecalcCooldownEnabled = Boolean.parseBoolean(props.getProperty("server.path.cooldown.enabled", String.valueOf(serverPathRecalcCooldownEnabled)));
            serverPathRecalcCooldownTicks = parseInt(props.getProperty("server.path.cooldown.ticks"), serverPathRecalcCooldownTicks);
            serverRandomTickScaleEnabled = Boolean.parseBoolean(props.getProperty("server.randomticks.scale.enabled", String.valueOf(serverRandomTickScaleEnabled)));
            serverRandomTickMinFraction = parseDouble(props.getProperty("server.randomticks.min_fraction"), serverRandomTickMinFraction);

            props.setProperty("server.ai.throttle.enabled", String.valueOf(serverAiThrottleEnabled));
            props.setProperty("server.ai.throttle.prob_full_load", String.valueOf(serverAiThrottleProbAtFullLoad));
            props.setProperty("server.path.cooldown.enabled", String.valueOf(serverPathRecalcCooldownEnabled));
            props.setProperty("server.path.cooldown.ticks", String.valueOf(serverPathRecalcCooldownTicks));
            props.setProperty("server.randomticks.scale.enabled", String.valueOf(serverRandomTickScaleEnabled));
            props.setProperty("server.randomticks.min_fraction", String.valueOf(serverRandomTickMinFraction));

            // Item merge
            serverItemMergeCooldownEnabled = Boolean.parseBoolean(props.getProperty("server.items.merge.cooldown.enabled", String.valueOf(serverItemMergeCooldownEnabled)));
            serverItemMergeCooldownTicks = parseInt(props.getProperty("server.items.merge.cooldown.ticks"), serverItemMergeCooldownTicks);
            props.setProperty("server.items.merge.cooldown.enabled", String.valueOf(serverItemMergeCooldownEnabled));
            props.setProperty("server.items.merge.cooldown.ticks", String.valueOf(serverItemMergeCooldownTicks));

            // Dimension particle bonuses
            serverParticlesDimensionBonuses = Boolean.parseBoolean(props.getProperty("server.particles.dimension_bonuses.enabled", String.valueOf(serverParticlesDimensionBonuses)));
            serverParticlesDropBonusOverworld = parseDouble(props.getProperty("server.particles.dimension_bonuses.overworld"), serverParticlesDropBonusOverworld);
            serverParticlesDropBonusNether = parseDouble(props.getProperty("server.particles.dimension_bonuses.nether"), serverParticlesDropBonusNether);
            serverParticlesDropBonusEnd = parseDouble(props.getProperty("server.particles.dimension_bonuses.end"), serverParticlesDropBonusEnd);
            props.setProperty("server.particles.dimension_bonuses.enabled", String.valueOf(serverParticlesDimensionBonuses));
            props.setProperty("server.particles.dimension_bonuses.overworld", String.valueOf(serverParticlesDropBonusOverworld));
            props.setProperty("server.particles.dimension_bonuses.nether", String.valueOf(serverParticlesDropBonusNether));
            props.setProperty("server.particles.dimension_bonuses.end", String.valueOf(serverParticlesDropBonusEnd));

            // Metrics
            metricsEnabled = Boolean.parseBoolean(props.getProperty("metrics.enabled", String.valueOf(metricsEnabled)));
            metricsClientLogSeconds = parseInt(props.getProperty("metrics.client.log_seconds"), metricsClientLogSeconds);
            metricsServerLogSeconds = parseInt(props.getProperty("metrics.server.log_seconds"), metricsServerLogSeconds);
            props.setProperty("metrics.enabled", String.valueOf(metricsEnabled));
            props.setProperty("metrics.client.log_seconds", String.valueOf(metricsClientLogSeconds));
            props.setProperty("metrics.server.log_seconds", String.valueOf(metricsServerLogSeconds));

            // Hopper
            serverHopperIdleSkipEnabled = Boolean.parseBoolean(props.getProperty("server.hopper.idle_skip.enabled", String.valueOf(serverHopperIdleSkipEnabled)));
            serverHopperIdleCheckIntervalTicks = parseInt(props.getProperty("server.hopper.idle_skip.interval_ticks"), serverHopperIdleCheckIntervalTicks);
            props.setProperty("server.hopper.idle_skip.enabled", String.valueOf(serverHopperIdleSkipEnabled));
            props.setProperty("server.hopper.idle_skip.interval_ticks", String.valueOf(serverHopperIdleCheckIntervalTicks));
            try (FileOutputStream fos = new FileOutputStream(file.toFile())) {
                props.store(fos, "DefaultMod configuration");
            }
        } catch (IOException e) {
            // Non-fatal: keep defaults on failure
        }
    }

    public static void save() {
        try {
            Path configDir = getConfigDir();
            Files.createDirectories(configDir);
            Path file = configDir.resolve(FILE_NAME);
            Properties props = new Properties();
            // mirror of fields to props
            props.setProperty("particles.enabled", String.valueOf(particleBudgetingEnabled));
            props.setProperty("particles.max_new_per_second", String.valueOf(maxNewParticlesPerSecond));
            props.setProperty("particles.drop_fraction", String.valueOf(dropFraction));
            props.setProperty("particles.max_distance", String.valueOf(particleMaxDistance));
            props.setProperty("particles.offscreen_bonus_drop", String.valueOf(particleOffscreenBonusDrop));
            props.setProperty("particles.distance_bonus_drop", String.valueOf(particleDistanceBonusDrop));

            props.setProperty("math.lut.enabled", String.valueOf(mathLutEnabled));
            props.setProperty("entity.collision.cache_inside_wall", String.valueOf(entityCollisionCacheEnabled));

            props.setProperty("adaptive.enabled", String.valueOf(adaptiveEnabled));
            props.setProperty("adaptive.target_fps", String.valueOf(adaptiveTargetFps));
            props.setProperty("adaptive.min_extra_drop", String.valueOf(adaptiveMinExtraDrop));
            props.setProperty("adaptive.max_extra_drop", String.valueOf(adaptiveMaxExtraDrop));
            props.setProperty("adaptive.response", String.valueOf(adaptiveResponse));

            props.setProperty("server.particles.enabled", String.valueOf(serverParticlesEnabled));
            props.setProperty("server.particles.drop_fraction", String.valueOf(serverParticlesDropFraction));
            props.setProperty("server.particles.disable_sprinting", String.valueOf(serverDisableSprintingParticles));

            props.setProperty("server.adaptive.enabled", String.valueOf(serverAdaptiveEnabled));
            props.setProperty("server.adaptive.target_tps", String.valueOf(serverAdaptiveTargetTps));
            props.setProperty("server.adaptive.min_extra_drop", String.valueOf(serverAdaptiveMinExtraDrop));
            props.setProperty("server.adaptive.max_extra_drop", String.valueOf(serverAdaptiveMaxExtraDrop));
            props.setProperty("server.adaptive.response", String.valueOf(serverAdaptiveResponse));

            props.setProperty("server.ai.throttle.enabled", String.valueOf(serverAiThrottleEnabled));
            props.setProperty("server.ai.throttle.prob_full_load", String.valueOf(serverAiThrottleProbAtFullLoad));
            props.setProperty("server.path.cooldown.enabled", String.valueOf(serverPathRecalcCooldownEnabled));
            props.setProperty("server.path.cooldown.ticks", String.valueOf(serverPathRecalcCooldownTicks));
            props.setProperty("server.randomticks.scale.enabled", String.valueOf(serverRandomTickScaleEnabled));
            props.setProperty("server.randomticks.min_fraction", String.valueOf(serverRandomTickMinFraction));

            props.setProperty("server.items.merge.cooldown.enabled", String.valueOf(serverItemMergeCooldownEnabled));
            props.setProperty("server.items.merge.cooldown.ticks", String.valueOf(serverItemMergeCooldownTicks));

            props.setProperty("server.particles.dimension_bonuses.enabled", String.valueOf(serverParticlesDimensionBonuses));
            props.setProperty("server.particles.dimension_bonuses.overworld", String.valueOf(serverParticlesDropBonusOverworld));
            props.setProperty("server.particles.dimension_bonuses.nether", String.valueOf(serverParticlesDropBonusNether));
            props.setProperty("server.particles.dimension_bonuses.end", String.valueOf(serverParticlesDropBonusEnd));

            props.setProperty("metrics.enabled", String.valueOf(metricsEnabled));
            props.setProperty("metrics.client.log_seconds", String.valueOf(metricsClientLogSeconds));
            props.setProperty("metrics.server.log_seconds", String.valueOf(metricsServerLogSeconds));

            props.setProperty("server.hopper.idle_skip.enabled", String.valueOf(serverHopperIdleSkipEnabled));
            props.setProperty("server.hopper.idle_skip.interval_ticks", String.valueOf(serverHopperIdleCheckIntervalTicks));

            try (FileOutputStream fos = new FileOutputStream(file.toFile())) {
                props.store(fos, "DefaultMod configuration");
            }
        } catch (IOException ignored) { }
    }

    private static int parseInt(String v, int def) {
        try { return Integer.parseInt(v); } catch (Exception e) { return def; }
    }
    private static double parseDouble(String v, double def) {
        try { return Double.parseDouble(v); } catch (Exception e) { return def; }
    }

    private static Path getConfigDir() {
        String userHome = System.getProperty("user.home");
        // Fallback to .minecraft/config for simplicity; Fabric loader also exposes a config dir, but using MC path avoids extra deps
        return Path.of(userHome, ".minecraft", "config");
    }
}
