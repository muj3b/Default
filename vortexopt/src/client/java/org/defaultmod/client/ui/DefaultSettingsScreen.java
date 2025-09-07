package org.defaultmod.client.ui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;
import org.defaultmod.config.DefaultConfig;

public class DefaultSettingsScreen extends Screen {
    private final Screen parent;
    private float fade = 0f;

    public DefaultSettingsScreen(Screen parent) {
        super(Text.literal("Default Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int y = 40;
        int x = this.width / 2 - 150;
        int w = 300;
        int h = 20;

        this.addDrawableChild(ButtonWidget.builder(toggleLabel("Particles", DefaultConfig.particleBudgetingEnabled), b -> {
            DefaultConfig.particleBudgetingEnabled = !DefaultConfig.particleBudgetingEnabled;
            b.setMessage(toggleLabel("Particles", DefaultConfig.particleBudgetingEnabled));
        }).dimensions(x, y, w, h).build());
        y += 24;

        this.addDrawableChild(new SimpleSlider(x, y, w, h, Text.literal("Particle Budget"), 100, 4000, DefaultConfig.maxNewParticlesPerSecond, v -> {
            DefaultConfig.maxNewParticlesPerSecond = (int) v;
        }));
        y += 24;

        this.addDrawableChild(new SimpleSlider(x, y, w, h, Text.literal("Drop Fraction"), 0, 100, (int) (DefaultConfig.dropFraction * 100), v -> {
            DefaultConfig.dropFraction = v / 100.0;
        }));
        y += 24;

        this.addDrawableChild(ButtonWidget.builder(toggleLabel("Math LUT", DefaultConfig.mathLutEnabled), b -> {
            DefaultConfig.mathLutEnabled = !DefaultConfig.mathLutEnabled;
            b.setMessage(toggleLabel("Math LUT", DefaultConfig.mathLutEnabled));
        }).dimensions(x, y, w, h).build());
        y += 24;

        this.addDrawableChild(ButtonWidget.builder(toggleLabel("Adaptive FPS", DefaultConfig.adaptiveEnabled), b -> {
            DefaultConfig.adaptiveEnabled = !DefaultConfig.adaptiveEnabled;
            b.setMessage(toggleLabel("Adaptive FPS", DefaultConfig.adaptiveEnabled));
        }).dimensions(x, y, w, h).build());
        y += 24;

        this.addDrawableChild(new SimpleSlider(x, y, w, h, Text.literal("Target FPS"), 20, 240, DefaultConfig.adaptiveTargetFps, v -> {
            DefaultConfig.adaptiveTargetFps = (int) v;
        }));
        y += 24;

        // More particle heuristics
        this.addDrawableChild(new SimpleSlider(x, y, w, h, Text.literal("Max Particle Distance"), 16, 256, (int) DefaultConfig.particleMaxDistance, v -> {
            DefaultConfig.particleMaxDistance = v;
        }));
        y += 24;
        this.addDrawableChild(new SimpleSlider(x, y, w, h, Text.literal("Offscreen Bonus Drop (% )"), 0, 100, (int) (DefaultConfig.particleOffscreenBonusDrop * 100), v -> {
            DefaultConfig.particleOffscreenBonusDrop = v / 100.0;
        }));
        y += 24;
        this.addDrawableChild(new SimpleSlider(x, y, w, h, Text.literal("Distance Bonus Drop (% )"), 0, 100, (int) (DefaultConfig.particleDistanceBonusDrop * 100), v -> {
            DefaultConfig.particleDistanceBonusDrop = v / 100.0;
        }));
        y += 24;

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Done"), b -> {
            DefaultConfig.save();
            this.client.setScreen(parent);
        }).dimensions(x, this.height - 30, w, h).build());
    }

    private Text toggleLabel(String name, boolean on) {
        return Text.literal(name + ": " + (on ? "On" : "Off"));
    }

    private void save() {}

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (fade < 1f) fade = Math.min(1f, fade + delta * 0.08f);
        int bg = (int) (fade * 160) << 24; // fade alpha
        context.fill(0, 0, this.width, this.height, 0x101010 | bg);
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 12, 0xFFFFFF);
    }

    private static class SimpleSlider extends SliderWidget {
        private final int min, max;
        private final java.util.function.Consumer<Integer> consumer;
        private final String label;
        public SimpleSlider(int x, int y, int w, int h, Text label, int min, int max, int current, java.util.function.Consumer<Integer> consumer) {
            super(x, y, w, h, label, 0d);
            this.min = min; this.max = max; this.consumer = consumer; this.label = label.getString();
            this.value = (current - min) / (double) (max - min);
            updateMessage();
        }
        @Override protected void updateMessage() {
            int v = (int) (min + value * (max - min));
            this.setMessage(Text.literal(label + ": " + v));
        }
        @Override protected void applyValue() {
            int v = (int) (min + value * (max - min));
            consumer.accept(v);
        }
    }
}
