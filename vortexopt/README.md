Default (Fabric 1.21.8)
=======================

Default is an all‑in‑one optimization mod focused on safe, configurable client and server performance. It adds particle budgeting and camera‑aware culling, adaptive FPS/TPS tuners, conservative collision and AI/path tweaks, server‑side burst dampening, and a clean in‑game settings UI with a profiling HUD.

Why Default
- Single mod with practical wins for spikes and big bases.
- Feature‑gated mixins with sane defaults and graceful fallbacks.
- Designed to pair well with other mods; no gameplay changes by default.

Features
- Particle budgeting: caps new particle rate to prevent CPU/GC spikes.
- Camera‑aware culling: drop offscreen/distant particles with adaptive bias.
- Math LUT: fast sin/cos via `MathHelper` mixin.
- Entity collision micro‑opts: inside‑wall cache and projectile/ projectile exclusion.
- Adaptive FPS/TPS tuners: smooth frame times and stabilize tick rate.
- Server particle filter: drop/scale particle bursts and disable sprinting particles.
- AI/path tweaks: probabilistic brain tick throttle and path‑recalc cooldowns.
- Random tick scaling: reduces random tick load under TPS pressure (with floor).
- Item merge cooldown: avoids merge hot loops.
- Hopper idle‑skip: skip ticks when no source inventory and no nearby items.
- Dimension policies: per‑dimension particle drop bonuses.
- UI & HUD: Video Settings → “Default Settings”; F8 toggles a live HUD.

Install
1. Build or download the JAR: `build/libs/default-0.1.0.jar`.
2. Drop into `.minecraft/mods` for Fabric 1.21.8.
3. First run writes `.minecraft/config/default.properties` (or `vortexopt.properties` if upgrading from earlier builds).
4. Video Settings → “Default Settings” to tune; press F8 in‑game for the HUD.

Config (defaults)
```
particles.enabled=true
particles.max_new_per_second=800
particles.drop_fraction=0.5
particles.max_distance=64.0
particles.offscreen_bonus_drop=0.5
particles.distance_bonus_drop=0.5

math.lut.enabled=true
entity.collision.cache_inside_wall=true

adaptive.enabled=true
adaptive.target_fps=60
adaptive.min_extra_drop=0.0
adaptive.max_extra_drop=0.4
adaptive.response=0.1

# Server
server.particles.enabled=true
server.particles.drop_fraction=0.25
server.particles.disable_sprinting=true
server.adaptive.enabled=true
server.adaptive.target_tps=20
server.adaptive.min_extra_drop=0.0
server.adaptive.max_extra_drop=0.5
server.adaptive.response=0.15

# AI / path / random ticks
server.ai.throttle.enabled=false
server.ai.throttle.prob_full_load=0.35
server.path.cooldown.enabled=true
server.path.cooldown.ticks=10
server.randomticks.scale.enabled=true
server.randomticks.min_fraction=0.5

# Items / hopper
server.items.merge.cooldown.enabled=true
server.items.merge.cooldown.ticks=5
server.hopper.idle_skip.enabled=true
server.hopper.idle_skip.interval_ticks=8

# Dimension particle bonuses
server.particles.dimension_bonuses.enabled=true
server.particles.dimension_bonuses.overworld=0.0
server.particles.dimension_bonuses.nether=0.1
server.particles.dimension_bonuses.end=0.05

# Metrics
metrics.enabled=true
metrics.client.log_seconds=5
metrics.server.log_seconds=5
```

Compatibility & Positioning
- Default is independent of Sodium/Lithium and does not copy their code. It focuses on adaptive throttles, particle/AI/collision heuristics, and usability (HUD/GUI). You can run it alongside them.
- A full renderer replacement like Sodium is out of scope here. Sodium still wins for raw chunk rendering. For logic breadth, Lithium remains deeper. The best results often come from pairing Default with those mods, but Default stands alone fine if you prefer its approach and controls.
- Mixins use `require=0` on brittle targets to degrade gracefully across mappings (warnings expected).

Build
```
./gradlew build -x test
```
JARs are written to `build/libs/`.

Notes
- If your pack is particle‑heavy, reduce `particles.max_new_per_second` or increase `particles.drop_fraction`.
- Turn on the HUD (F8) and metrics to quickly gauge impact while tuning.

License & Attribution
- This project is original work and does not include code from Sodium (Polyform Shield 1.0.0) or Lithium (LGPL‑3.0). If you ship or integrate against those codebases, ensure compliance with their licenses.
