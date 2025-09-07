package org.defaultmod.ui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;
import org.defaultmod.config.DefaultConfig;

public class DefaultVideoScreen extends Screen {
    private final Screen parent;
    public DefaultVideoScreen(Screen parent) {
        super(Text.literal("Default Video Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int x = this.width / 2 - 160;
        int y = 40;
        int w = 320; int h = 20;

        // Governor
        this.addDrawableChild(ButtonWidget.builder(toggle("Governor", DefaultConfig.adaptiveEnabled), b -> {
            DefaultConfig.adaptiveEnabled = !DefaultConfig.adaptiveEnabled;
            b.setMessage(toggle("Governor", DefaultConfig.adaptiveEnabled));
        }).dimensions(x, y, w, h).build()); y += 24;

        this.addDrawableChild(new SimpleSlider(x, y, w, h, Text.literal("Target FPS"), 20, 240, DefaultConfig.adaptiveTargetFps, v -> DefaultConfig.adaptiveTargetFps = v)); y += 24;

        // Particle management
        this.addDrawableChild(ButtonWidget.builder(toggle("Particles", DefaultConfig.particleBudgetingEnabled), b -> {
            DefaultConfig.particleBudgetingEnabled = !DefaultConfig.particleBudgetingEnabled;
            b.setMessage(toggle("Particles", DefaultConfig.particleBudgetingEnabled));
        }).dimensions(x, y, w, h).build()); y += 24;
        this.addDrawableChild(new SimpleSlider(x, y, w, h, Text.literal("Particle Budget"), 100, 4000, DefaultConfig.maxNewParticlesPerSecond, v -> DefaultConfig.maxNewParticlesPerSecond = v)); y += 24;
        this.addDrawableChild(new SimpleSlider(x, y, w, h, Text.literal("Drop Fraction"), 0, 100, (int)(DefaultConfig.dropFraction*100), v -> DefaultConfig.dropFraction = v/100.0)).active = true; y += 24;

        // Chunk budget
        this.addDrawableChild(ButtonWidget.builder(toggle("Chunk Rebuild Budget", DefaultConfig.clientChunkRebuildBudgetEnabled), b -> {
            DefaultConfig.clientChunkRebuildBudgetEnabled = !DefaultConfig.clientChunkRebuildBudgetEnabled;
            b.setMessage(toggle("Chunk Rebuild Budget", DefaultConfig.clientChunkRebuildBudgetEnabled));
        }).dimensions(x, y, w, h).build()); y += 24;
        this.addDrawableChild(new SimpleSlider(x, y, w, h, Text.literal("Rebuilds per Frame"), 1, 20, DefaultConfig.clientChunkRebuildsPerFrame, v -> DefaultConfig.clientChunkRebuildsPerFrame = v)); y += 24;

        // Vanilla game options quick controls
        var opt = MinecraftClient.getInstance().options;
        this.addDrawableChild(new SimpleSlider(x, y, w, h, Text.literal("View Distance"), 2, 32, opt.getViewDistance().getValue(), v -> opt.getViewDistance().setValue(v))); y += 24;
        this.addDrawableChild(new SimpleSlider(x, y, w, h, Text.literal("Entity Distance %"), 50, 500, (int)(opt.getEntityDistanceScaling().getValue()*100), v -> opt.getEntityDistanceScaling().setValue(v/100.0))); y += 24;
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Apply & Save"), b -> {
            DefaultConfig.save();
            MinecraftClient.getInstance().options.write();
        }).dimensions(x, y, w, h).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Done"), b -> this.client.setScreen(parent)).dimensions(x, this.height - 28, w, h).build());
    }

    private Text toggle(String name, boolean value) { return Text.literal(name + ": " + (value?"On":"Off")); }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        this.renderBackground(ctx, mouseX, mouseY, delta);
        super.render(ctx, mouseX, mouseY, delta);
        ctx.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width/2, 10, 0xFFFFFF);
    }

    private static class SimpleSlider extends SliderWidget {
        private final int min, max; private final java.util.function.IntConsumer apply;
        public SimpleSlider(int x, int y, int w, int h, Text label, int min, int max, int current, java.util.function.IntConsumer apply) {
            super(x, y, w, h, label, 0d);
            this.min=min; this.max=max; this.apply=apply; this.value=(current-min)/(double)(max-min); updateMessage();
        }
        @Override protected void updateMessage() { int v=(int)(min+value*(max-min)); setMessage(Text.literal(getMessage().getString()+": "+v)); }
        @Override protected void applyValue() { int v=(int)(min+value*(max-min)); apply.accept(v); }
    }
}
