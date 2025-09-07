package org.defaultmod.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public final class DefaultKeybinds {
    private static KeyBinding toggleHud;
    private static KeyBinding incDrop;
    private static KeyBinding decDrop;
    private DefaultKeybinds() {}

    public static void init() {
        toggleHud = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.default.toggle_hud",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F8,
                "category.default"
        ));
        incDrop = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.default.drop_up",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_KP_ADD,
                "category.default"
        ));
        decDrop = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.default.drop_down",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_KP_SUBTRACT,
                "category.default"
        ));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleHud.wasPressed()) {
                org.defaultmod.client.ui.DefaultHud.toggle();
            }
            while (incDrop.wasPressed()) {
                org.defaultmod.config.DefaultConfig.dropFraction = Math.min(1.0, org.defaultmod.config.DefaultConfig.dropFraction + 0.05);
            }
            while (decDrop.wasPressed()) {
                org.defaultmod.config.DefaultConfig.dropFraction = Math.max(0.0, org.defaultmod.config.DefaultConfig.dropFraction - 0.05);
            }
        });
    }
}
