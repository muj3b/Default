package org.defaultmod.mixin.server.pathing;

import net.minecraft.entity.ai.pathing.MobNavigation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.defaultmod.config.DefaultConfig;
import org.defaultmod.runtime.ServerClock;

@Mixin(MobNavigation.class)
public abstract class MobNavigationMixin {
    @Unique private long vortexopt$lastRecalcTick = Long.MIN_VALUE;

    @Inject(method = "recalculatePath()V", at = @At("HEAD"), cancellable = true, require = 0)
    private void vortexopt$cooldownRecalc(CallbackInfo ci) {
        if (!DefaultConfig.serverPathRecalcCooldownEnabled) return;
        long now = ServerClock.now();
        if (vortexopt$lastRecalcTick + DefaultConfig.serverPathRecalcCooldownTicks > now) {
            ci.cancel();
        } else {
            vortexopt$lastRecalcTick = now;
        }
    }
}

