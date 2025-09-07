package org.defaultmod.mixin.chunk;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Attempt to rate-limit chunk rebuild scheduling. Mapping names vary; guarded with require=0.
 */
@Mixin(targets = "net.minecraft.client.render.chunk.ChunkBuilder", remap = true)
public abstract class ChunkBuilderMixin {
    @Inject(method = "scheduleRebuild(Lnet/minecraft/client/render/chunk/ChunkBuilder$BuiltChunk;)V", at = @At("HEAD"), cancellable = true, require = 0)
    private void default$limitRebuild(Object builtChunk, CallbackInfo ci) {
        if (!org.defaultmod.runtime.ChunkBudget.tryAcquire()) {
            ci.cancel();
        }
    }
}

