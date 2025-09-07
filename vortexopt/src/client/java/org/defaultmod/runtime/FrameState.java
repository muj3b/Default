package org.defaultmod.runtime;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;

public final class FrameState {
    private static volatile Vec3d cameraPos = Vec3d.ZERO;
    private static volatile Vec3d lookDir = new Vec3d(0, 0, 1);

    private FrameState() {}

    public static void update(MinecraftClient mc, float tickDelta) {
        if (mc == null || mc.getCameraEntity() == null) return;
        cameraPos = mc.getCameraEntity().getPos();
        lookDir = mc.getCameraEntity().getRotationVec(tickDelta);
    }

    public static Vec3d getCameraPos() {
        return cameraPos;
    }

    public static Vec3d getLookDir() {
        return lookDir;
    }
}

