package org.defaultmod.math;

/**
 * Lightweight sine/cosine lookup. Angles in radians.
 * Double variants use float table precision to avoid excessive memory.
 */
public final class FastTrig {
    private static final int TABLE_SIZE = 8192; // power of two for fast masking
    private static final int TABLE_MASK = TABLE_SIZE - 1;
    private static final float TWO_PI = (float) (Math.PI * 2.0);
    private static final float INV_TWO_PI = 1.0f / TWO_PI;

    private static final float[] SIN = new float[TABLE_SIZE];
    private static final float[] COS = new float[TABLE_SIZE];

    static {
        for (int i = 0; i < TABLE_SIZE; i++) {
            float angle = (i / (float) TABLE_SIZE) * TWO_PI;
            SIN[i] = (float) Math.sin(angle);
            COS[i] = (float) Math.cos(angle);
        }
    }

    private FastTrig() {}

    private static int idx(float radians) {
        // Wrap into [0, 2pi) and map to table index
        float wrapped = radians - (float) Math.floor(radians * INV_TWO_PI) * TWO_PI;
        return (int) (wrapped * (TABLE_SIZE / TWO_PI)) & TABLE_MASK;
    }

    public static float sin(float radians) {
        return SIN[idx(radians)];
    }

    public static float cos(float radians) {
        return COS[idx(radians)];
    }

    public static double sin(double radians) {
        return SIN[idx((float) radians)];
    }

    public static double cos(double radians) {
        return COS[idx((float) radians)];
    }
}

