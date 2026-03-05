# Technology Stack

## Languages

- Java 25 (using modern features like implicit classes)

## Development Environment

- IDE: IntelliJ IDEA
- Build: IntelliJ native project (no Maven/Gradle)
- JDK: ms-25 (Microsoft OpenJDK 25)

## Project Structure

```
flappy-bird-java/
├── src/
│   └── Main.java          # Entry point (currently template code)
├── plans/
│   └── plan.md            # Iterative game plan
├── .idea/                  # IntelliJ configuration
├── .kilocode/              # Memory bank and rules
├── flappy-bird-java.iml   # IntelliJ module file
└── .gitignore             # Standard Java gitignore
```

## Technical Constraints

- Pure Java implementation (no external game engines)
- Uses Java Swing for graphics (confirmed choice)
- Single-threaded game loop with Timer

## Dependencies

- None currently (standard JDK only)
- Swing/AWT included in JDK

## Build and Run

- Run directly from IntelliJ IDEA
- Output directory: out/

## Graphics Approach

- Java Swing with JFrame and JPanel
- Double buffering for smooth rendering
- Simple shapes (rectangles, circles) for MVP
- Custom painting via paintComponent override
