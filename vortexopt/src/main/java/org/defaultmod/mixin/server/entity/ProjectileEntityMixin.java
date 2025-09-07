package org.defaultmod.mixin.server.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ProjectileEntity.class)
public abstract class ProjectileEntityMixin {
    @Inject(method = "canHit(Lnet/minecraft/entity/Entity;)Z", at = @At("HEAD"), cancellable = true, require = 0)
    private void vortexopt$skipProjectileProjectile(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        // Short-circuit projectile-projectile collisions to reduce pairwise checks in stacks
        if (entity instanceof net.minecraft.entity.projectile.ProjectileEntity) {
            cir.setReturnValue(false);
        }
    }
}

