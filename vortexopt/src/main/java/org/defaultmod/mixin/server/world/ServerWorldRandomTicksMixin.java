package org.defaultmod.mixin.server.world;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.defaultmod.config.DefaultConfig;
import org.defaultmod.runtime.ServerAdaptiveTuner;

@Mixin(ServerWorld.class)
public abstract class ServerWorldRandomTicksMixin {

    @ModifyVariable(method = "tickChunk(Lnet/minecraft/world/chunk/WorldChunk;I)V", at = @At("HEAD"), ordinal = 0, argsOnly = true, require = 0)
    private int vortexopt$scaleRandomTicks(int randomTickSpeed, WorldChunk chunk, int original) {
        if (!DefaultConfig.serverRandomTickScaleEnabled) return randomTickSpeed;
        double extra = ServerAdaptiveTuner.getExtraDrop();
        double range = Math.max(0.0, DefaultConfig.serverAdaptiveMaxExtraDrop);
        double load = range == 0.0 ? 0.0 : Math.min(1.0, Math.max(0.0, extra / range));
        double minFrac = Math.min(1.0, Math.max(0.0, DefaultConfig.serverRandomTickMinFraction));
        double frac = minFrac + (1.0 - minFrac) * (1.0 - load);
        int scaled = (int) Math.round(randomTickSpeed * frac);
        return Math.max(0, scaled);
    }
}

