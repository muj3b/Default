package org.defaultmod.client.ui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public final class DefaultHud {
    private static boolean enabled = false;
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
    }
}
