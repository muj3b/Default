package org.defaultmod.mixin.server;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.defaultmod.runtime.ServerAdaptiveTuner;
import org.defaultmod.runtime.ServerClock;

import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Inject(method = "tick(Ljava/util/function/BooleanSupplier;)V", at = @At("HEAD"), require = 0)
    private void vortexopt$onTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        ServerAdaptiveTuner.onServerTick();
        ServerClock.tick();
    }

    // Alternative signature removed to avoid remap failure when missing
}
