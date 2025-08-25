# Base Instruction Set: Collaborative Minecraft Mod Development Workflow

## 1. Project Setup Guidelines
- **Mod Loader**: Use Forge or Fabric (specify in provided files; default to Forge for broader compatibility).
- **Build System**: Gradle-based buildscript. Include `build.gradle` and `settings.gradle` in code dump if customizing.
- **Directory Structure**:
  - src/main/java/[package]/: Core mod classes.
  - src/main/resources/: Assets, models, textures, and META-INF/mods.toml (Forge) or fabric.mod.json (Fabric).
- **Dependencies**: Auto-include Minecraft, mod loader libs; add external like JEI for dev tools if needed.

## 2. Code Priming and Editing Protocol
- **User Input Format**: Prime with markdown blocks like:
  ## path/to/file.java
  ```
  // Existing Java code here
  ```
- **AI Response Protocol**:
  - Analyze provided code for errors, optimizations, or Minecraft-specific issues (e.g., registry conflicts).
  - Output edited/new files in similar markdown format.
  - Suggest additions (e.g., new classes for items/blocks) as separate blocks.
  - Include inline comments in code for explanations.
- **Iteration Cycle**:
  1. User primes with code or describes feature (e.g., "Add a custom sword item").
  2. AI refines: Edit code, add registrations, handle events.
  3. User reviews and provides feedback for next iteration.

## 3. Core Mod Development Patterns
- **Mod Initialization**:
  - Main class annotated with @Mod (Forge) or implementing ModInitializer (Fabric).
  - Register events via MinecraftForge.EVENT_BUS (Forge) or equivalent.
- **Registry Handling**:
  - Use DeferredRegister for blocks/items/entities to avoid load order issues.
  - Example: Register a block in a dedicated RegistryEvents class.
- **Feature Implementation**:
  - Items/Blocks: Extend Item/Block; override methods for behaviors.
  - Events: Subscribe to Forge events (e.g., PlayerInteractEvent).
  - Networking: Use SimpleChannel for custom packets if multiplayer.
- **Testing and Debugging**:
  - Run via Gradle tasks (runClient/runServer).
  - Check logs for crashes; suggest fixes like null checks or proper sidedness (client/server).

## 4. Best Practices and Standards
- **Code Style**: Follow Java conventions; use Lombok for boilerplate if agreed.
- **Error Handling**: Wrap registries in try-catch; log via LOGGER.info/warn.
- **Version Control**: Assume Git repo; suggest commits per feature.
- **Adaptability**: Support modpacks via configs.

## 5. Escalation and Tools
- If code execution/testing needed, describe in natural language (no direct tool calls here, but simulate outputs).
- For external refs (e.g., Forge docs), note URLs but focus on self-contained edits.
- Refine this set via user feedback—e.g., add sections for data gen or recipes.

## 6. Code Dump
- Place initial or provided code files here in the specified format (e.g., ## path/to/file.java followed by code block).
- This section serves as the starting point for iterations, with files covering setup details like versions.
