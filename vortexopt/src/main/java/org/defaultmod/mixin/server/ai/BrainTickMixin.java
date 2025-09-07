package org.defaultmod.mixin.server.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.entity.ai.brain.Brain;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.defaultmod.config.DefaultConfig;
import org.defaultmod.runtime.ServerAdaptiveTuner;

@Mixin(Brain.class)
public abstract class BrainTickMixin {

    @Inject(method = "tick(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LivingEntity;)V", at = @At("HEAD"), cancellable = true, require = 0)
    private void vortexopt$maybeThrottle(ServerWorld world, LivingEntity entity, CallbackInfo ci) {
        if (!DefaultConfig.serverAiThrottleEnabled) return;
        // Compute load factor from adaptive extra drop range
        double load = Math.min(1.0, Math.max(0.0, ServerAdaptiveTuner.getExtraDrop() / Math.max(1e-9, DefaultConfig.serverAdaptiveMaxExtraDrop)));
        double p = load * DefaultConfig.serverAiThrottleProbAtFullLoad;
        if (p > 0 && Math.random() < p) {
            // Skip this brain tick. Essential tasks (movement/combat) will resume next tick.
            ci.cancel();
        }
    }
}

