

| Step | From Version | To Version | Key Reasons for This Jump | Major Changes to Handle |
|------|--------------|------------|---------------------------|-------------------------|
| 7 | 1.21.4 | 1.21.5 | Focuses on dynamic registries and villager trades; a small but breaking step. | - Dynamic registry JSONs namespaced (e.g., `data/test/example/...`).<br>- `BiomeModificationContext#addSpawn` adds weight param.<br>- Trades use `RegistryKey`; deprecate old villager helpers.<br>- Update to Loom 1.10. |
| 8 | 1.21.5 | 1.21.8 | Final step covering HUD rewrite and module cleanups; 1.21.6 introduces the biggest changes here, but 1.21.7/8 are minor patches. | - HUD API rewritten via `HudElementRegistry`.<br>- Remove old modules (e.g., `fabric-command-api-v1`).<br>- Material API removed from Rendering.<br>- `BlockRenderLayerMap` import/method updates.<br>- Update Fabric Loader to 0.16.10+. |



Minecraft fabric mod updating from 1.21.5 to 1.21.8
```
with the following errors/warnings to be fixed
```
