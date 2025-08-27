# Base Instruction Set: Collaborative Minecraft Mod Development Workflow

## 1. Project Setup Guidelines
- **Mod Loader**: Fabric.
- **Minecraft Version**: Starting Version 1.19.2 updating to 1.19.4; ensure dependencies match.
- **Build System**: Gradle-based buildscript. Include `build.gradle` and `settings.gradle` in code dump if customizing.
- **Directory Structure**:
  - src/main/java/[package]/: Core mod classes.
  - src/main/resources/: Assets, models, textures, and fabric.mod.json.
- **Dependencies**: Auto-include Minecraft, Fabric loader libs; add external like JEI for dev tools if needed.

## 2. Code Priming and Editing Protocol
- **User Input Format**: Prime with markdown blocks like:
  ### path/to/file.java
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
  - Main class implementing ModInitializer.
  - Register events via Fabric's event system (e.g., RegisterCommandsCallback).
- **Registry Handling**:
  - Use RegistryKey and RegistryEntry for blocks/items/entities to avoid load order issues.
  - Example: Register a block in a dedicated initializer class.
- **Feature Implementation**:
  - Items/Blocks: Extend Item/Block; override methods for behaviors.
  - Events: Use Fabric's API events (e.g., UseBlockCallback).
  - Networking: Use ServerPlayNetworking or ClientPlayNetworking for custom packets if multiplayer.
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
- For external refs (e.g., Fabric docs), note URLs but focus on self-contained edits.
- For API details, look up online documentation (e.g., via web search or official Fabric wiki/API docs) as the version is newer than the AI's training data.
- Refine this set via user feedback—e.g., add sections for data gen or recipes.

## 6. Code Dump
- Place initial or provided code files here in the specified format (e.g., ## path/to/file.java followed by code block).
- This section serves as the starting point for iterations, with files covering setup details like versions.
