package org.defaultmod.mixin.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.defaultmod.config.DefaultConfig;

@Mixin(Entity.class)
public abstract class EntityInsideWallMixin {
    @Unique private Box vortexopt$lastBox;
    @Unique private boolean vortexopt$lastInsideWall;
    @Unique private int vortexopt$lastInsideWallFrame;

    @Inject(method = "isInsideWall()Z", at = @At("HEAD"), cancellable = true, require = 0)
    private void vortexopt$cacheInsideWall(CallbackInfoReturnable<Boolean> cir) {
        if (!DefaultConfig.entityCollisionCacheEnabled) return;
        Entity self = (Entity) (Object) this;
        Box box = self.getBoundingBox();

        // Use client render frame counter if available via onRenderTick, but we don't have it here.
        // Instead, skip repeat computation only if bounding box hasn't changed since last invocation.
        if (vortexopt$lastBox != null && box.equals(vortexopt$lastBox)) {
            cir.setReturnValue(vortexopt$lastInsideWall);
        } else {
            // Store box; the actual result will be captured via a tail injection if needed, but we can also store on return path.
            vortexopt$lastBox = box;
        }
    }

    @Inject(method = "isInsideWall()Z", at = @At("RETURN"), cancellable = false, require = 0)
    private void vortexopt$recordInsideWall(CallbackInfoReturnable<Boolean> cir) {
        if (!DefaultConfig.entityCollisionCacheEnabled) return;
        Entity self = (Entity) (Object) this;
        vortexopt$lastBox = self.getBoundingBox();
        vortexopt$lastInsideWall = cir.getReturnValueZ();
    }
}

