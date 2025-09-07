package org.defaultmod.runtime.poi;

import it.unimi.dsi.fastutil.longs.Long2BooleanOpenHashMap;
import net.minecraft.util.math.ChunkSectionPos;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Very small per-world cache for whether a section contains any POIs.
 * If a section has never seen a POI, searches can fast-exit.
 * This is a heuristic and degrades gracefully if mappings shift.
 */
public final class POICache {
    private static final Map<Object, Long2BooleanOpenHashMap> WORLD_MAP = new WeakHashMap<>();

    private POICache() {}

    private static Long2BooleanOpenHashMap map(Object worldKey) {
        return WORLD_MAP.computeIfAbsent(worldKey, k -> new Long2BooleanOpenHashMap());
    }

    public static void markHasPoi(Object worldKey, int sx, int sy, int sz) {
        map(worldKey).put(ChunkSectionPos.asLong(sx, sy, sz), true);
    }

    public static void clearHasPoi(Object worldKey, int sx, int sy, int sz) {
        map(worldKey).remove(ChunkSectionPos.asLong(sx, sy, sz));
    }

    public static boolean knownEmpty(Object worldKey, int sx, int sy, int sz) {
        // If not present in map, consider unknown (not empty). We only skip when explicitly false.
        Long2BooleanOpenHashMap m = map(worldKey);
        long key = ChunkSectionPos.asLong(sx, sy, sz);
        if (!m.containsKey(key)) return false;
        return !m.get(key);
    }
}

