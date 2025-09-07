package org.defaultmod.mixin.server;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.defaultmod.config.DefaultConfig;

@Mixin(LivingEntity.class)
public abstract class LivingEntitySprintingParticlesMixin {
    @Inject(method = "spawnSprintingParticles()V", at = @At("HEAD"), cancellable = true, require = 0)
    private void vortexopt$disableSprintingParticles(CallbackInfo ci) {
        if (DefaultConfig.serverDisableSprintingParticles) {
            ci.cancel();
        }
    }
}

