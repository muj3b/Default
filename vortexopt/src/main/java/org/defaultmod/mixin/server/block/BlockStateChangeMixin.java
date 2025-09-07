package org.defaultmod.mixin.server.block;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Wake hoppers when an inventory-holding block above them changes state.
 * Mapping signatures vary; guarded with require=0.
 */
@Mixin(targets = "net.minecraft.block.Block", remap = true)
public abstract class BlockStateChangeMixin {
    @Inject(method = "onStateReplaced(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Z)V", at = @At("TAIL"), require = 0)
    private void default$wakeHopperOnReplace(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved, CallbackInfo ci) {
        if (world.isClient()) return;
        BlockPos below = pos.down();
        var be = world.getBlockEntity(below);
        if (be instanceof net.minecraft.block.entity.HopperBlockEntity hopper && hopper instanceof HopperDuck duck) {
            duck.default$wake();
        }
    }
}

