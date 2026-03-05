# Context

## Current Focus

Ready to begin implementation phase. No code written yet.

## Recent Changes

- Completed all three planning passes in plans/plan.md
- Identified and resolved 10 integration issues during planning
- Created 6-phase implementation checklist
- Decided on multi-file class structure

## Project Status

- Phase: Pre-implementation (no game code exists)
- Plan file: plans/plan.md (complete, all checklist items unchecked)
- Main.java contains only IntelliJ template code (Hello World)
- No game-related source files created yet
- Last reviewed: 2026-03-05

## Next Steps

1. Begin Phase 1: Foundation
2. Create Constants.java with shared values
3. Create GamePanel.java with window setup and game loop
4. Update Main.java to launch the game window

## Key Decisions Made

- Graphics library: Java Swing (JFrame + JPanel)
- Multi-file structure (not implicit class)
- Rectangle hitboxes with 3px padding for forgiveness
- Fixed timestep game loop (16ms / ~60 FPS)
- Score check before collision in loop order
- Initial spawnTimer = -1.0 for delayed first pipe

## File Structure (Current vs Planned)

Current:
- src/Main.java (template code only)

Planned additions:
- src/Constants.java
- src/GamePanel.java
- src/Bird.java
- src/Pipe.java
- src/PipeManager.java
