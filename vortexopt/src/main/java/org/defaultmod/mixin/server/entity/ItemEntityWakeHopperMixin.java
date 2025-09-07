package org.defaultmod.mixin.server.entity;

import net.minecraft.entity.ItemEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.defaultmod.mixin.server.block.HopperDuck;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityWakeHopperMixin {
    @Inject(method = "tick()V", at = @At("HEAD"), require = 0)
    private void default$wakeNearbyHopper(CallbackInfo ci) {
        ItemEntity self = (ItemEntity) (Object) this;
        World w = self.getWorld();
        if (w == null || w.isClient()) return;
        BlockPos below = self.getBlockPos().down();
        var be = w.getBlockEntity(below);
        if (be instanceof net.minecraft.block.entity.HopperBlockEntity hopper && hopper instanceof HopperDuck duck) {
            duck.default$wake();
        }
    }
}

