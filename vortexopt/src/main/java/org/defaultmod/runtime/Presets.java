package org.defaultmod.runtime;

import org.defaultmod.config.DefaultConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Presets {
    private Presets() {}

    public enum Kind { PERFORMANCE, BALANCED, QUALITY }

    public static void apply(Kind kind) {
        switch (kind) {
            case PERFORMANCE -> loadFromResource("performance.properties");
            case BALANCED -> loadFromResource("balanced.properties");
            case QUALITY -> loadFromResource("quality.properties");
        }
        DefaultConfig.save();
    }

    private static void loadFromResource(String name) {
        try {
            Path cfgDir = Path.of(System.getProperty("user.home"), ".minecraft", "config");
            Files.createDirectories(cfgDir);
            Path dest = cfgDir.resolve("default.properties");
            // Load preset from packaged resources directory under mod jar
            // In dev, read from project folder; in production, this would be a resource.
            // For simplicity, copy from the config-presets folder if present.
            Path here = Path.of("vortexopt", "config-presets", name);
            if (Files.exists(here)) {
                Files.copy(here, dest, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                DefaultConfig.load();
            }
        } catch (IOException ignored) {}
    }
}

