package org.defaultmod.mixin;

import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.defaultmod.runtime.ParticleBudget;
import org.defaultmod.runtime.FrameState;
import org.defaultmod.runtime.AdaptiveTuner;

@Mixin(ParticleManager.class)
public class ParticleManagerMixin {
    @Inject(method = "addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V", at = @At("HEAD"), cancellable = true)
    private void vortexopt$maybeDrop(ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, CallbackInfo ci) {
        org.defaultmod.runtime.Metrics.clientParticlesConsidered.incrementAndGet();
        ParticleBudget.onNewParticle();
        if (shouldCullFromContext(x, y, z) || ParticleBudget.shouldDrop()) {
            org.defaultmod.runtime.Metrics.clientParticlesDropped.incrementAndGet();
            ci.cancel();
        }
    }

    @Inject(method = "addParticle(Lnet/minecraft/particle/ParticleEffect;ZDDDDDD)V", at = @At("HEAD"), cancellable = true)
    private void vortexopt$maybeDropForced(ParticleEffect parameters, boolean alwaysRender, double x, double y, double z, double velocityX, double velocityY, double velocityZ, CallbackInfo ci) {
        org.defaultmod.runtime.Metrics.clientParticlesConsidered.incrementAndGet();
        ParticleBudget.onNewParticle();
        if (shouldCullFromContext(x, y, z) || ParticleBudget.shouldDrop()) {
            org.defaultmod.runtime.Metrics.clientParticlesDropped.incrementAndGet();
            ci.cancel();
        }
    }

    @Inject(method = "addParticle(Lnet/minecraft/client/particle/Particle;)V", at = @At("HEAD"), cancellable = true)
    private void vortexopt$maybeDropDirect(net.minecraft.client.particle.Particle particle, CallbackInfo ci) {
        org.defaultmod.runtime.Metrics.clientParticlesConsidered.incrementAndGet();
        ParticleBudget.onNewParticle();
        // We don't know its spawn pos directly; particle has its coords.
        if (ParticleBudget.shouldDrop()) {
            org.defaultmod.runtime.Metrics.clientParticlesDropped.incrementAndGet();
            ci.cancel();
        }
    }

    private static boolean shouldCullFromContext(double x, double y, double z) {
        // Camera info
        Vec3d camPos = FrameState.getCameraPos();
        Vec3d look = FrameState.getLookDir();
        double dx = x - camPos.x;
        double dy = y - camPos.y;
        double dz = z - camPos.z;

        double dist2 = dx*dx + dy*dy + dz*dz;
        double p = AdaptiveTuner.getDropBase();

        // Behind camera approx: dot(position vector) vs. facing vector
        boolean behind = (dx*look.x + dy*look.y + dz*look.z) < 0.0;
        if (behind) {
            p += org.defaultmod.config.DefaultConfig.particleOffscreenBonusDrop;
        }
        if (dist2 > org.defaultmod.config.DefaultConfig.particleMaxDistance * org.defaultmod.config.DefaultConfig.particleMaxDistance) {
            p += org.defaultmod.config.DefaultConfig.particleDistanceBonusDrop;
        }
        return Math.random() < Math.min(1.0, p);
    }
}
