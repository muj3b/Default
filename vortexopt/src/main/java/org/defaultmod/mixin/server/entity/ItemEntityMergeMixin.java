package org.defaultmod.mixin.server.entity;

import net.minecraft.entity.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.defaultmod.config.DefaultConfig;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMergeMixin {
    @Unique private int vortexopt$mergeCooldown = 0;

    @Inject(method = "tryMerge()Z", at = @At("HEAD"), cancellable = true, require = 0)
    private void vortexopt$cooldownMergeHead(CallbackInfoReturnable<Boolean> cir) {
        if (!DefaultConfig.serverItemMergeCooldownEnabled) return;
        if (vortexopt$mergeCooldown > 0) {
            vortexopt$mergeCooldown--;
            cir.setReturnValue(false);
            org.defaultmod.runtime.Metrics.serverItemMergeSkips.incrementAndGet();
        } else {
            vortexopt$mergeCooldown = DefaultConfig.serverItemMergeCooldownTicks;
        }
    }
}
