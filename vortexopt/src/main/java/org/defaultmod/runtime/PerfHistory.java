package org.defaultmod.runtime;

public final class PerfHistory {
    private static final int N = 180; // last ~3s at 60 FPS
    private static final float[] fps = new float[N];
    private static final float[] tps = new float[N];
    private static int iF = 0, iT = 0;

    private PerfHistory() {}

    public static void pushFps(double value) {
        fps[iF] = (float) value;
        iF = (iF + 1) % N;
    }

    public static void pushTps(double value) {
        tps[iT] = (float) value;
        iT = (iT + 1) % N;
    }

    public static float[] getFps() { return fps; }
    public static float[] getTps() { return tps; }
}

