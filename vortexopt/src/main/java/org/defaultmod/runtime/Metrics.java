package org.defaultmod.runtime;

import java.util.concurrent.atomic.AtomicLong;

public final class Metrics {
    // Client
    public static final AtomicLong clientParticlesConsidered = new AtomicLong();
    public static final AtomicLong clientParticlesDropped = new AtomicLong();

    // Server
    public static final AtomicLong serverParticleBurstsDropped = new AtomicLong();
    public static final AtomicLong serverParticlesScaled = new AtomicLong();
    public static final AtomicLong serverItemMergeSkips = new AtomicLong();

    private Metrics() {}
}

