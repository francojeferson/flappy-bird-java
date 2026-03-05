# Architecture

## Current State

Project is in initial setup phase. Only template code exists.

## Planned Architecture

### Core Components

1. **Game Window** - Main JFrame/JPanel for rendering
2. **Bird** - Player-controlled entity with physics
3. **Pipes** - Obstacles that scroll from right to left
4. **GameLoop** - Timer-based update and render cycle
5. **CollisionDetector** - Checks bird vs pipes and ground
6. **ScoreManager** - Tracks and displays score

### Source Code Paths

```
src/
├── Main.java              # Entry point, creates game window
├── GamePanel.java         # Main game rendering and loop (planned)
├── Bird.java              # Bird entity (planned)
├── Pipe.java              # Pipe obstacle (planned)
└── GameState.java         # Game state management (planned)
```

### Design Patterns

- **Game Loop Pattern** - Fixed timestep update cycle
- **Entity Pattern** - Bird and Pipes as game entities
- **State Pattern** - Menu, Playing, GameOver states

### Key Technical Decisions

- Graphics library: TBD (Swing vs JavaFX)
- Rendering approach: Double buffering for smooth animation
- Input handling: KeyListener or KeyBindings

### Component Relationships

```
Main
  └── GamePanel (JPanel)
        ├── Bird
        ├── List<Pipe>
        ├── ScoreManager
        └── GameLoop (Timer)
```

## Implementation Priority

1. Window and basic rendering
2. Bird with gravity and flap
3. Scrolling pipes
4. Collision detection
5. Scoring system
6. Game states (menu, game over)
