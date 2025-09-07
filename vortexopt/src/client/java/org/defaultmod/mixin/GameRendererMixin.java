package org.defaultmod.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.defaultmod.runtime.AdaptiveTuner;
import org.defaultmod.runtime.FrameState;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Inject(method = "render(FJZ)V", at = @At("HEAD"), require = 0)
    private void vortexopt$onRenderHead(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        FrameState.update(MinecraftClient.getInstance(), tickDelta);
        AdaptiveTuner.onFrame(tickDelta);
        try { org.defaultmod.runtime.ChunkBudget.onFrame(); } catch (Throwable ignored) {}
    }

    @Inject(method = "render(FZ)V", at = @At("HEAD"), require = 0)
    private void vortexopt$onRenderHeadAlt(float tickDelta, boolean tick, CallbackInfo ci) {
        FrameState.update(MinecraftClient.getInstance(), tickDelta);
        AdaptiveTuner.onFrame(tickDelta);
        try { org.defaultmod.runtime.ChunkBudget.onFrame(); } catch (Throwable ignored) {}
    }
}
