package org.defaultmod.client.ui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public final class DefaultHud {
    private static boolean enabled = false;
    private static boolean graphs = true;
    private static float graphScaleFpsMax = 240f;
    private static float graphScaleTpsMax = 40f;
    private static boolean editMode = false;
    private static int editIndex = 0;
    private DefaultHud() {}
    public static void toggle() { enabled = !enabled; }
    public static boolean isEnabled() { return enabled; }

    public static void render(DrawContext ctx) {
        if (!enabled) return;
        int x = 6, y = 6;
        var mc = MinecraftClient.getInstance();
        if (mc == null || mc.textRenderer == null) return;
        double dropBase = org.defaultmod.runtime.AdaptiveTuner.getDropBase();
        long considered = org.defaultmod.runtime.Metrics.clientParticlesConsidered.get();
        long dropped = org.defaultmod.runtime.Metrics.clientParticlesDropped.get();
        String l1 = String.format("DefaultMod HUD");
        String l2 = String.format("DropBase: %.2f", dropBase);
        String l3 = String.format("Particles: %d considered, %d dropped", considered, dropped);
        int bg = 0x66000000;
        int w = Math.max(Math.max(mc.textRenderer.getWidth(l1), mc.textRenderer.getWidth(l2)), mc.textRenderer.getWidth(l3)) + 8;
        int h = 36;
        ctx.fill(x-4, y-4, x+w, y+h, bg);
        ctx.drawTextWithShadow(mc.textRenderer, l1, x, y, 0xFFFFFF);
        ctx.drawTextWithShadow(mc.textRenderer, l2, x, y+12, 0xA0FFC0);
        ctx.drawTextWithShadow(mc.textRenderer, l3, x, y+24, 0xC0E0FF);

        if (graphs) {
            drawGraph(ctx, x+w+8, y, 120, 32, org.defaultmod.runtime.PerfHistory.getFps(), 0xFF4CAF50, 15f, graphScaleFpsMax, "FPS");
            drawGraph(ctx, x+w+8, y+36, 120, 32, org.defaultmod.runtime.PerfHistory.getTps(), 0xFF2196F3, 0f, graphScaleTpsMax, "TPS");
        }

        if (editMode) {
            int ex = x; int ey = y + 72;
            int w2 = 260; int h2 = 64;
            ctx.fill(ex-4, ey-4, ex+w2, ey+h2, 0x77000000);
            String[] lines = currentEditLines();
            for (int i = 0; i < lines.length; i++) {
                int col = (i == editIndex ? 0xFFFFA0 : 0xFFFFFF);
                ctx.drawTextWithShadow(mc.textRenderer, lines[i], ex, ey + i*12, col);
            }
            ctx.drawTextWithShadow(mc.textRenderer, "Edit: Arrow Up/Down select, Left/Right adjust, F6 exit", ex, ey + h2 - 12, 0xA0FFC0);
        }
    }

    private static void drawGraph(DrawContext ctx, int gx, int gy, int gw, int gh, float[] series, int color, float min, float max, String label) {
        if (series == null) return;
        ctx.fill(gx-2, gy-2, gx+gw+2, gy+gh+2, 0x44000000);
        int n = series.length;
        for (int i = 0; i < gw; i++) {
            int idx = (n - 1 - i) % n; if (idx < 0) idx += n;
            float v = series[idx];
            float t = (Math.max(min, Math.min(max, v)) - min) / (max - min + 1e-6f);
            int barH = (int) (t * gh);
            int x = gx + gw - 1 - i;
            ctx.fill(x, gy + gh - barH, x+1, gy + gh, color);
        }
        // label
        var mc = MinecraftClient.getInstance();
        if (mc != null && mc.textRenderer != null) {
            ctx.drawTextWithShadow(mc.textRenderer, label, gx, gy - 10, 0xFFFFFF);
        }
    }

    // Edit controls
    public static void toggleEdit() { editMode = !editMode; }
    public static boolean isEditMode() { return editMode; }
    public static void editNext(int dir) {
        if (!editMode) return;
        int count = currentEditLines().length;
        editIndex = Math.floorMod(editIndex + dir, count);
    }
    public static void editAdjust(int dir) {
        if (!editMode) return;
        switch (editIndex) {
            case 0 -> org.defaultmod.config.DefaultConfig.dropFraction = clamp01(org.defaultmod.config.DefaultConfig.dropFraction + dir*0.05);
            case 1 -> org.defaultmod.config.DefaultConfig.maxNewParticlesPerSecond = clampInt(org.defaultmod.config.DefaultConfig.maxNewParticlesPerSecond + dir*100, 0, 10000);
            case 2 -> org.defaultmod.config.DefaultConfig.particleMaxDistance = clampDouble(org.defaultmod.config.DefaultConfig.particleMaxDistance + dir*4, 8, 512);
            case 3 -> org.defaultmod.config.DefaultConfig.particleOffscreenBonusDrop = clamp01(org.defaultmod.config.DefaultConfig.particleOffscreenBonusDrop + dir*0.05);
            case 4 -> org.defaultmod.config.DefaultConfig.particleDistanceBonusDrop = clamp01(org.defaultmod.config.DefaultConfig.particleDistanceBonusDrop + dir*0.05);
            case 5 -> org.defaultmod.config.DefaultConfig.adaptiveEnabled = !org.defaultmod.config.DefaultConfig.adaptiveEnabled;
        }
    }

    private static String[] currentEditLines() {
        return new String[] {
            String.format("Drop fraction: %.2f", org.defaultmod.config.DefaultConfig.dropFraction),
            String.format("Particle budget: %d/s", org.defaultmod.config.DefaultConfig.maxNewParticlesPerSecond),
            String.format("Max particle distance: %.1f", org.defaultmod.config.DefaultConfig.particleMaxDistance),
            String.format("Offscreen bonus drop: %.2f", org.defaultmod.config.DefaultConfig.particleOffscreenBonusDrop),
            String.format("Distance bonus drop: %.2f", org.defaultmod.config.DefaultConfig.particleDistanceBonusDrop),
            String.format("Adaptive FPS: %s", org.defaultmod.config.DefaultConfig.adaptiveEnabled ? "On" : "Off")
        };
    }

    private static double clampDouble(double v, double lo, double hi) { return Math.max(lo, Math.min(hi, v)); }
    private static double clamp01(double v) { return clampDouble(v, 0.0, 1.0); }
    private static int clampInt(int v, int lo, int hi) { return Math.max(lo, Math.min(hi, v)); }

    public static void adjustGraphScale(int dir) {
        if (dir > 0) {
            graphScaleFpsMax = Math.min(480f, graphScaleFpsMax + 20f);
            graphScaleTpsMax = Math.min(80f, graphScaleTpsMax + 2f);
        } else if (dir < 0) {
            graphScaleFpsMax = Math.max(60f, graphScaleFpsMax - 20f);
            graphScaleTpsMax = Math.max(20f, graphScaleTpsMax - 2f);
        }
    }
}
