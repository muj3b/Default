package org.defaultmod.mixin;

import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.defaultmod.config.DefaultConfig;
import org.defaultmod.math.FastTrig;

/**
 * Replace MathHelper's sin/cos with a LUT when enabled.
 * If method signatures change between versions, this mixin simply won't apply.
 */
@Mixin(MathHelper.class)
public abstract class MathHelperMixin {
    @Inject(method = "sin(F)F", at = @At("HEAD"), cancellable = true, require = 0)
    private static void vortexopt$sin(float radians, CallbackInfoReturnable<Float> cir) {
        if (DefaultConfig.mathLutEnabled) {
            cir.setReturnValue(FastTrig.sin(radians));
        }
    }

    @Inject(method = "cos(F)F", at = @At("HEAD"), cancellable = true, require = 0)
    private static void vortexopt$cos(float radians, CallbackInfoReturnable<Float> cir) {
        if (DefaultConfig.mathLutEnabled) {
            cir.setReturnValue(FastTrig.cos(radians));
        }
    }
}

