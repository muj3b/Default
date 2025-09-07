package org.defaultmod.mixin.server.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.mob.ShulkerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityCollisionMixin {
    @Inject(method = "collidesWith(Lnet/minecraft/entity/Entity;)Z", at = @At("HEAD"), cancellable = true, require = 0)
    private void default$lightweightExclusions(Entity other, CallbackInfoReturnable<Boolean> cir) {
        Entity self = (Entity) (Object) this;
        // Skip item-item collisions and armor stand-armor stand to reduce pair checks
        if ((self instanceof ItemEntity && other instanceof ItemEntity) ||
            (self instanceof ArmorStandEntity && other instanceof ArmorStandEntity)) {
            cir.setReturnValue(false);
            return;
        }
        // Boats and shulkers remain hard colliders, keep vanilla for others
    }
}

