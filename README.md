# Default (Fabric 1.21.8)

Default is an all‑in‑one optimization mod focused on safe, configurable performance improvements on both client and server. It includes adaptive particle culling and budgeting, FPS/TPS tuners, targeted AI/path/collision optimizations, server burst dampening, a clean in‑game settings screen, and a lightweight profiling HUD.

Highlights
- Particle budgeting and camera‑aware culling to tame spikes
- Adaptive FPS/TPS control for smoothness under load
- AI/path throttles and cooldowns to prevent spam
- Random tick scaling with a safety floor
- Projectile exclusions, item‑merge cooldowns, hopper idle‑skip
- Dimension‑aware particle policies
- In‑game settings UI and HUD

Build
```
cd vortexopt
./gradlew build -x test
```
JAR output: `vortexopt/build/libs/default-0.1.0.jar`

CI/CD
- Tag a commit like `v0.1.0` and GitHub Actions will build and attach JARs to the release automatically (see `.github/workflows/release.yml`).
- Artifacts are also uploaded to the Actions run as `default-jars`.

Modrinth/CurseForge
- This repo does not ship their metadata by default to avoid leaking tokens. To publish:
  - Create a release on GitHub (tag like `v0.1.0`).
  - Upload `default-0.1.0.jar` to Modrinth or CurseForge manually, or add a separate workflow using the Modrinth/CurseForge actions with repository secrets for tokens.
  - Suggested slug: `default-optimizer` (Modrinth) and `Default` (CurseForge). Clearly mention compatibility with Sodium and Lithium.

Legal
- This repository is original work and does not embed code from Sodium (Polyform Shield 1.0.0) or Lithium (LGPL‑3.0). Those licenses are not compatible for combining into a single derivative work. If you choose to use Sodium and/or Lithium alongside Default, install them as separate mods and follow their respective licenses.
