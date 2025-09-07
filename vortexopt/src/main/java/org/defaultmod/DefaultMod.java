package org.defaultmod;

import net.fabricmc.api.ModInitializer;
import org.defaultmod.config.DefaultConfig;

public class DefaultMod implements ModInitializer {
    @Override
    public void onInitialize() {
        DefaultConfig.load();
    }
}

