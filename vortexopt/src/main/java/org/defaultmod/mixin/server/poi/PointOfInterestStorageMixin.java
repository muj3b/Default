package org.defaultmod.mixin.server.poi;

import net.minecraft.util.math.ChunkSectionPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Heuristic POI presence cache hook-ins. Method names/desc are mapping-dependent; guarded with require=0.
 */
@Mixin(targets = "net.minecraft.world.poi.PointOfInterestStorage", remap = true)
public abstract class PointOfInterestStorageMixin {
    @Unique private Object default$worldKey() { return this; }

    @Inject(method = "add(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/poi/PointOfInterestType;)V", at = @At("TAIL"), require = 0)
    private void default$onAddPoi(net.minecraft.util.math.BlockPos pos, Object type, CallbackInfo ci) {
        ChunkSectionPos csp = ChunkSectionPos.from(pos);
        org.defaultmod.runtime.poi.POICache.markHasPoi(default$worldKey(), csp.getX(), csp.getY(), csp.getZ());
    }

    @Inject(method = "remove(Lnet/minecraft/util/math/BlockPos;)V", at = @At("TAIL"), require = 0)
    private void default$onRemovePoi(net.minecraft.util.math.BlockPos pos, CallbackInfo ci) {
        ChunkSectionPos csp = ChunkSectionPos.from(pos);
        // Mark unknown by clearing; avoids incorrect negative caching
        org.defaultmod.runtime.poi.POICache.clearHasPoi(default$worldKey(), csp.getX(), csp.getY(), csp.getZ());
    }

    @Inject(method = "hasAny(Lnet/minecraft/util/math/BlockPos;)Z", at = @At("HEAD"), cancellable = true, require = 0)
    private void default$fastKnownEmpty(net.minecraft.util.math.BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        ChunkSectionPos csp = ChunkSectionPos.from(pos);
        if (org.defaultmod.runtime.poi.POICache.knownEmpty(default$worldKey(), csp.getX(), csp.getY(), csp.getZ())) {
            cir.setReturnValue(false);
        }
    }
}

