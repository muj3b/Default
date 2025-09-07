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
    private static KeyBinding toggleGraphs;
    private static KeyBinding incBudget;
    private static KeyBinding decBudget;
    private static KeyBinding toggleAdaptive;
    private static KeyBinding toggleMathLut;
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
        toggleGraphs = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.default.graph_toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                "category.default"
        ));
        incBudget = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.default.budget_up",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_BRACKET,
                "category.default"
        ));
        decBudget = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.default.budget_down",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT_BRACKET,
                "category.default"
        ));
        toggleAdaptive = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.default.adaptive_toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F9,
                "category.default"
        ));
        toggleMathLut = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.default.mathlut_toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F7,
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
            while (toggleGraphs.wasPressed()) {
                boolean g = org.defaultmod.client.ui.DefaultHud.isEnabled(); // call just to touch class
                // small trick: use reflection to toggle private static 'graphs'
                try {
                    var cls = org.defaultmod.client.ui.DefaultHud.class;
                    var f = cls.getDeclaredField("graphs");
                    f.setAccessible(true);
                    f.setBoolean(null, !f.getBoolean(null));
                } catch (Throwable ignored) {}
            }
            while (incBudget.wasPressed()) {
                org.defaultmod.config.DefaultConfig.maxNewParticlesPerSecond = Math.min(10000, org.defaultmod.config.DefaultConfig.maxNewParticlesPerSecond + 100);
            }
            while (decBudget.wasPressed()) {
                org.defaultmod.config.DefaultConfig.maxNewParticlesPerSecond = Math.max(0, org.defaultmod.config.DefaultConfig.maxNewParticlesPerSecond - 100);
            }
            while (toggleAdaptive.wasPressed()) {
                org.defaultmod.config.DefaultConfig.adaptiveEnabled = !org.defaultmod.config.DefaultConfig.adaptiveEnabled;
            }
            while (toggleMathLut.wasPressed()) {
                org.defaultmod.config.DefaultConfig.mathLutEnabled = !org.defaultmod.config.DefaultConfig.mathLutEnabled;
            }
        });
    }
}
