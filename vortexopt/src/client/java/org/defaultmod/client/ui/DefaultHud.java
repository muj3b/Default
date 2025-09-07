package org.defaultmod.client.ui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public final class DefaultHud {
    private static boolean enabled = false;
    private static boolean graphs = true;
    private static float graphScaleFpsMax = 240f;
    private static float graphScaleTpsMax = 40f;
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
}
