package org.defaultmod.mixin.server.block;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * When inventories change (markDirty), nudge nearby hoppers to wake sooner.
 * Cheap and conservative; guarded by require=0.
 */
@Mixin(BlockEntity.class)
public abstract class BlockEntityMarkDirtyMixin {
    @Inject(method = "markDirty()V", at = @At("TAIL"), require = 0)
    private void default$wakeNearbyHoppers(CallbackInfo ci) {
        BlockEntity self = (BlockEntity) (Object) this;
        World w = self.getWorld();
        if (w == null || w.isClient()) return;
        BlockPos pos = self.getPos();
        // Check a small cube for hoppers
        int r = 1;
        for (BlockPos p : BlockPos.iterate(pos.add(-r, -r, -r), pos.add(r, r, r))) {
            var be = w.getBlockEntity(p);
            if (be instanceof net.minecraft.block.entity.HopperBlockEntity hopper) {
                if (hopper instanceof HopperDuck duck) {
                    duck.default$wake();
                }
            }
        }
    }
}

