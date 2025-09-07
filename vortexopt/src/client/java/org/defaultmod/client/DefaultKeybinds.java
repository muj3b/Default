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
    private static KeyBinding editToggle;
    private static KeyBinding editUp;
    private static KeyBinding editDown;
    private static KeyBinding editLeft;
    private static KeyBinding editRight;
    private static KeyBinding saveConfig;
    private static KeyBinding reloadConfig;
    private static KeyBinding scaleUp;
    private static KeyBinding scaleDown;
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
        editToggle = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.default.edit_toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F6,
                "category.default"
        ));
        editUp = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.default.edit_up",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UP,
                "category.default"
        ));
        editDown = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.default.edit_down",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_DOWN,
                "category.default"
        ));
        editLeft = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.default.edit_left",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT,
                "category.default"
        ));
        editRight = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.default.edit_right",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT,
                "category.default"
        ));
        saveConfig = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.default.config_save",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F5,
                "category.default"
        ));
        reloadConfig = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.default.config_reload",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F10,
                "category.default"
        ));
        scaleUp = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.default.graph_scale_up",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_PAGE_UP,
                "category.default"
        ));
        scaleDown = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.default.graph_scale_down",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_PAGE_DOWN,
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
            while (editToggle.wasPressed()) {
                org.defaultmod.client.ui.DefaultHud.toggleEdit();
            }
            if (org.defaultmod.client.ui.DefaultHud.isEditMode()) {
                while (editUp.wasPressed()) org.defaultmod.client.ui.DefaultHud.editNext(-1);
                while (editDown.wasPressed()) org.defaultmod.client.ui.DefaultHud.editNext(1);
                while (editLeft.wasPressed()) org.defaultmod.client.ui.DefaultHud.editAdjust(-1);
                while (editRight.wasPressed()) org.defaultmod.client.ui.DefaultHud.editAdjust(1);
            }
            while (saveConfig.wasPressed()) {
                org.defaultmod.config.DefaultConfig.save();
                try {
                    var mc = net.minecraft.client.MinecraftClient.getInstance();
                    if (mc != null) {
                        net.minecraft.client.toast.SystemToast.add(mc.getToastManager(), net.minecraft.client.toast.SystemToast.Type.NARRATOR_TOGGLE, net.minecraft.text.Text.literal("Default: Saved config"), net.minecraft.text.Text.literal("default.properties written"));
                    }
                } catch (Throwable ignored) {}
            }
            while (reloadConfig.wasPressed()) {
                org.defaultmod.config.DefaultConfig.load();
                try {
                    var mc = net.minecraft.client.MinecraftClient.getInstance();
                    if (mc != null) {
                        net.minecraft.client.toast.SystemToast.add(mc.getToastManager(), net.minecraft.client.toast.SystemToast.Type.NARRATOR_TOGGLE, net.minecraft.text.Text.literal("Default: Reloaded config"), net.minecraft.text.Text.literal("default.properties loaded"));
                    }
                } catch (Throwable ignored) {}
            }
            while (scaleUp.wasPressed()) {
                org.defaultmod.client.ui.DefaultHud.adjustGraphScale(1);
            }
            while (scaleDown.wasPressed()) {
                org.defaultmod.client.ui.DefaultHud.adjustGraphScale(-1);
            }
        });
    }
}
