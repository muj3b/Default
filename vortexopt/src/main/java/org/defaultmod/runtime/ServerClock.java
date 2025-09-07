package org.defaultmod.runtime;

import java.util.concurrent.atomic.AtomicLong;

public final class ServerClock {
    private static final AtomicLong TICKS = new AtomicLong();
    private ServerClock() {}
    public static void tick() { TICKS.incrementAndGet(); }
    public static long now() { return TICKS.get(); }
}

