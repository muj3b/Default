package org.defaultmod.mixin.server;

import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.defaultmod.runtime.ServerParticleFilter;

@Mixin(ServerWorld.class)
public abstract class ServerWorldParticlesMixin {
    @Inject(method = "spawnParticles(Lnet/minecraft/particle/ParticleEffect;DDDDDDDD)Z", at = @At("HEAD"), cancellable = true, require = 0)
    private void vortexopt$maybeDrop(ParticleEffect effect, double x, double y, double z, int count, double deltaX, double deltaY, double deltaZ, double speed, CallbackInfoReturnable<Boolean> cir) {
        if (ServerParticleFilter.shouldDropBurst((ServerWorld)(Object)this)) {
            cir.setReturnValue(false);
            org.defaultmod.runtime.Metrics.serverParticleBurstsDropped.incrementAndGet();
        }
    }

    @ModifyVariable(method = "spawnParticles(Lnet/minecraft/particle/ParticleEffect;DDDDDDDD)Z", at = @At("HEAD"), ordinal = 0, argsOnly = true, require = 0)
    private int vortexopt$scaleCount(int count) {
        int scaled = ServerParticleFilter.scaleCount((ServerWorld)(Object)this, count);
        if (scaled < count) org.defaultmod.runtime.Metrics.serverParticlesScaled.addAndGet(count - scaled);
        return scaled;
    }

    @Inject(method = "spawnParticles(Lnet/minecraft/particle/ParticleEffect;ZDDDDDDDD)Z", at = @At("HEAD"), cancellable = true, require = 0)
    private void vortexopt$maybeDropForced(ParticleEffect effect, boolean force, double x, double y, double z, int count, double deltaX, double deltaY, double deltaZ, double speed, CallbackInfoReturnable<Boolean> cir) {
        if (ServerParticleFilter.shouldDropBurst((ServerWorld)(Object)this)) {
            cir.setReturnValue(false);
            org.defaultmod.runtime.Metrics.serverParticleBurstsDropped.incrementAndGet();
        }
    }

    @ModifyVariable(method = "spawnParticles(Lnet/minecraft/particle/ParticleEffect;ZDDDDDDDD)Z", at = @At("HEAD"), ordinal = 0, argsOnly = true, require = 0)
    private int vortexopt$scaleCountForced(int count) {
        int scaled = ServerParticleFilter.scaleCount((ServerWorld)(Object)this, count);
        if (scaled < count) org.defaultmod.runtime.Metrics.serverParticlesScaled.addAndGet(count - scaled);
        return scaled;
    }
}
