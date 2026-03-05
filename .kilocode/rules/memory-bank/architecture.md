# Architecture

## Current State

Project is in initial setup phase. Only template code exists.

## Planned Architecture

### Core Components

1. **Constants** - Shared game constants (window size, physics, colors)
2. **GamePanel** - Main JPanel: game loop, rendering, input, state management
3. **Bird** - Player entity with gravity/flap physics
4. **Pipe** - Single pipe pair (top + bottom) with gap
5. **PipeManager** - Spawns, updates, and removes pipes

### Source Code Paths

```
src/
├── Main.java              # Entry point, creates JFrame and GamePanel
├── Constants.java         # All shared constants (planned)
├── GamePanel.java         # Game loop, rendering, input, state (planned)
├── Bird.java              # Bird entity with physics (planned)
├── Pipe.java              # Single pipe obstacle (planned)
└── PipeManager.java       # Manages pipe collection (planned)
```

### Design Patterns

- **Game Loop Pattern** - javax.swing.Timer firing every 16ms (~60 FPS)
- **Entity Pattern** - Bird and Pipes as game entities with update/render
- **State Pattern** - GameState enum: MENU, PLAYING, GAME_OVER
- **Manager Pattern** - PipeManager owns List<Pipe>, handles spawn/remove

### Key Technical Decisions

- Graphics: Java Swing (JFrame + JPanel with paintComponent)
- Rendering: Double buffering via JPanel default
- Input: KeyListener on GamePanel (setFocusable + requestFocusInWindow)
- Physics: Fixed timestep (deltaTime = 0.016s)
- Hitboxes: Rectangle-based with 3px padding for forgiveness
- Score: Checked before collision in loop order

### Component Relationships

```
Main
  └── JFrame
        └── GamePanel (extends JPanel, implements KeyListener)
              ├── Bird (physics: gravity=1200, flap=-400)
              ├── PipeManager
              │     └── List<Pipe> (width=60, gap=150, speed=200)
              ├── ScoreManager (score tracking, passed flag on pipes)
              └── Timer (16ms game loop)
```

### Game Loop Order

1. Update bird physics
2. Update pipes (move, spawn, remove)
3. Update score (check pipe.passed)
4. Check collisions (bird vs pipes, ground, ceiling)
5. Repaint

### Collision Boundaries

- Ground: y = HEIGHT - 50 (550px)
- Ceiling: y = 0
- Pipe gap center range: 100 to 425

## Implementation Priority

1. Phase 1: Foundation (Constants, GamePanel, window, game loop)
2. Phase 2: Bird (physics, rendering, input)
3. Phase 3: Pipes (Pipe, PipeManager, ground rendering)
4. Phase 4: Collision and Score
5. Phase 5: Game States (menu, game over, reset)
6. Phase 6: Polish (text outline, tuning, high score)
