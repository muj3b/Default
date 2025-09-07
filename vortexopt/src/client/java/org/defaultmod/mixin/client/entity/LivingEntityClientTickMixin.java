package org.defaultmod.mixin.client.entity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityClientTickMixin {
    @Inject(method = "tick()V", at = @At("HEAD"), cancellable = true, require = 0)
    private void default$maybeSkipClientTick(CallbackInfo ci) {
        if (!org.defaultmod.config.DefaultConfig.clientEntityTickThrottleEnabled) return;
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc == null || mc.isPaused() || mc.getCameraEntity() == null || mc.world == null) return;
        LivingEntity e = (LivingEntity) (Object) this;
        // Skip players and camera entity itself
        if (e == mc.player || e == mc.getCameraEntity()) return;

        Vec3d camPos = mc.getCameraEntity().getPos();
        Vec3d look = mc.getCameraEntity().getRotationVec(0f);
        Vec3d pos = e.getPos();
        double dx = pos.x - camPos.x, dy = pos.y - camPos.y, dz = pos.z - camPos.z;
        double dist2 = dx*dx + dy*dy + dz*dz;
        boolean behind = (dx*look.x + dy*look.y + dz*look.z) < 0.0;

        if (behind && Math.random() < org.defaultmod.config.DefaultConfig.clientEntityOffscreenSkipChance) {
            ci.cancel();
            return;
        }
        double fd = org.defaultmod.config.DefaultConfig.clientEntityFarDistance;
        if (dist2 > fd*fd && Math.random() < org.defaultmod.config.DefaultConfig.clientEntityFarSkipChance) {
            ci.cancel();
        }
    }
}

