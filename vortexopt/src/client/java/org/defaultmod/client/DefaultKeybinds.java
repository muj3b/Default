package org.defaultmod.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public final class DefaultKeybinds {
    private static KeyBinding toggleHud;
    private DefaultKeybinds() {}

    public static void init() {
        toggleHud = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.default.toggle_hud",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F8,
                "category.default"
        ));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleHud.wasPressed()) {
                org.defaultmod.client.ui.DefaultHud.toggle();
            }
        });
    }
}
