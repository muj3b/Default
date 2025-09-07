package org.defaultmod.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Inject(method = "render(Lnet/minecraft/client/gui/DrawContext;F)V", at = @At("TAIL"), require = 0)
    private void vortexopt$renderHud(DrawContext ctx, float tickDelta, CallbackInfo ci) {
        org.defaultmod.client.ui.DefaultHud.render(ctx);
    }
}

