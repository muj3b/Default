package org.defaultmod;

import net.fabricmc.api.ClientModInitializer;
import org.defaultmod.client.DefaultKeybinds;
import org.defaultmod.config.DefaultConfig;

public class DefaultModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        DefaultConfig.load();
        DefaultKeybinds.init();
    }
}

