package org.defaultmod.ui;

import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.VideoOptionsScreen;

public final class DefaultVideoOverride {
    private DefaultVideoOverride() {}

    public static void hook() {
        // Replace vanilla VideoOptionsScreen with our own
        ScreenEvents.BEFORE_INIT.register((client, screen, width, height) -> {
            if (screen instanceof VideoOptionsScreen) {
                client.setScreen(new DefaultVideoScreen(screen));
            }
        });
    }
}

