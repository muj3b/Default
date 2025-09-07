package org.defaultmod.mixin.server.block;

import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.defaultmod.config.DefaultConfig;

import java.util.List;

@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin {
    @Unique private int vortexopt$skipCheckCooldown = 0;

    @Inject(method = "serverTick(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/BlockState;Lnet/minecraft/block/entity/HopperBlockEntity;)V", at = @At("HEAD"), cancellable = true, require = 0)
    private static void vortexopt$maybeSkipTick(World world, BlockPos pos, Object state, HopperBlockEntity hopper, CallbackInfo ci) {
        if (!DefaultConfig.serverHopperIdleSkipEnabled) return;
        HopperBlockEntityMixin self = (HopperBlockEntityMixin) (Object) hopper;
        if (self.vortexopt$skipCheckCooldown > 0) {
            self.vortexopt$skipCheckCooldown--;
            ci.cancel();
            return;
        }
        // If there is no inventory above and no item entities in pickup range, skip a few ticks
        BlockPos above = pos.up();
        boolean hasItemAbove = false;
        if (!world.isClient) {
            Box box = new Box(above).expand(0.01);
            List<ItemEntity> list = world.getEntitiesByClass(ItemEntity.class, box, e -> true);
            hasItemAbove = !list.isEmpty();
        }
        boolean hasInventoryAbove = world.getBlockEntity(above) instanceof net.minecraft.inventory.Inventory;
        if (!hasInventoryAbove && !hasItemAbove) {
            self.vortexopt$skipCheckCooldown = Math.max(1, DefaultConfig.serverHopperIdleCheckIntervalTicks);
            ci.cancel();
        }
    }
}

