# Flappy Bird Game Plan

## Overview

This document contains the iterative planning for the Flappy Bird game implementation. Each system is planned in
multiple passes, with increasing detail.

---

## First Pass: Key Considerations

### 1. Game Window System

- Window size needs to be fixed (recommended: 400x600 or similar portrait orientation)
- Must support keyboard input
- Needs to be closeable and properly dispose resources
- Double buffering required for smooth rendering

### 2. Game Loop System

- Fixed timestep for consistent physics (60 FPS recommended)
- Must handle update and render separately
- Needs pause capability
- Should not block the main thread

### 3. Bird Physics System

- Gravity pulls bird down constantly
- Flap gives upward velocity (not position change)
- Bird has position (x, y) and velocity
- Bird x-position is fixed, only y changes
- Must clamp bird within screen bounds (or detect out-of-bounds as death)

### 4. Pipe System

- Pipes spawn off-screen to the right
- Pipes move left at constant speed
- Each pipe pair has a gap for the bird to pass through
- Gap position varies randomly within safe bounds
- Pipes are removed when they exit screen left
- Need to track if bird has passed a pipe (for scoring)

### 5. Collision Detection System

- Bird vs top pipe
- Bird vs bottom pipe
- Bird vs ground
- Bird vs ceiling (optional - can allow flying off top)
- Hitboxes: rectangles for simplicity

### 6. Score System

- Score increments when bird passes a pipe
- Need to track which pipes have been scored
- Display score on screen
- Track high score (optional for MVP)

### 7. Game State System

- States: MENU, PLAYING, GAME_OVER
- MENU: shows start prompt, waits for input
- PLAYING: game is active
- GAME_OVER: shows score, waits for restart input

### 8. Input System

- Single input action: FLAP
- Triggered by spacebar or mouse click
- Must work in MENU (to start) and PLAYING (to flap)
- In GAME_OVER, input restarts the game

### 9. Rendering System

- Clear screen each frame
- Draw background (solid color for MVP)
- Draw pipes (rectangles)
- Draw bird (circle or rectangle)
- Draw score (text)
- Draw state-specific UI (menu text, game over text)

---

## Second Pass: Detailed System Design

### 1. Game Window System

**Class:** GamePanel extends JPanel implements KeyListener

**Constants:**

- WIDTH = 400
- HEIGHT = 600
- TITLE = "Flappy Bird"

**Setup:**

- Create JFrame with fixed size (non-resizable)
- Add GamePanel as content
- Set background color (sky blue: RGB 135, 206, 235)
- Enable double buffering via JPanel default
- Add KeyListener to JFrame
- Set default close operation to EXIT_ON_CLOSE
- Center window on screen

**Interactions:**

- Receives key events, forwards to Input System
- Hosts the game loop timer
- Provides Graphics2D context to Rendering System

### 2. Game Loop System

**Implementation:** javax.swing.Timer

**Constants:**

- FRAME_TIME = 16 (milliseconds, approximately 60 FPS)

**Logic:**

```
Timer fires every FRAME_TIME:
  1. Calculate deltaTime (fixed at 0.016 seconds for simplicity)
  2. If state == PLAYING:
     - Update bird physics
     - Update pipes (move, spawn, remove)
     - Check collisions
     - Update score
  3. Repaint (triggers paintComponent)
```

**Interactions:**

- Calls Bird.update(deltaTime)
- Calls PipeManager.update(deltaTime)
- Calls CollisionDetector.check()
- Triggers repaint for Rendering System

### 3. Bird Physics System

**Class:** Bird

**Constants:**

- GRAVITY = 1200 (pixels per second squared)
- FLAP_VELOCITY = -400 (pixels per second, negative = up)
- BIRD_SIZE = 30 (diameter/width)
- BIRD_X = 80 (fixed horizontal position)
- START_Y = HEIGHT / 2

**State:**

- y: double (vertical position, top of bird)
- velocityY: double (vertical velocity)

**Methods:**

`update(deltaTime):`

```
velocityY = velocityY + GRAVITY * deltaTime
y = y + velocityY * deltaTime
```

`flap():`

```
velocityY = FLAP_VELOCITY
```

`reset():`

```
y = START_Y
velocityY = 0
```

`getBounds(): Rectangle`

```
return new Rectangle(BIRD_X, (int)y, BIRD_SIZE, BIRD_SIZE)
```

**Interactions:**

- Input System calls flap()
- Game Loop calls update()
- Collision System calls getBounds()
- Rendering System reads position for drawing

### 4. Pipe System

**Class:** Pipe

**Constants:**

- PIPE_WIDTH = 60
- GAP_HEIGHT = 150 (vertical space between top and bottom pipe)
- PIPE_SPEED = 200 (pixels per second)
- SPAWN_INTERVAL = 1.5 (seconds between spawns)
- MIN_GAP_Y = 100 (minimum distance from top for gap center)
- MAX_GAP_Y = HEIGHT - 100 - GROUND_HEIGHT (maximum distance from top)

**Pipe State:**

- x: double (horizontal position, left edge)
- gapY: int (center of the gap)
- passed: boolean (has bird passed this pipe for scoring)

**Pipe Methods:**

`update(deltaTime):`

```
x = x - PIPE_SPEED * deltaTime
```

`getTopBounds(): Rectangle`

```
topHeight = gapY - GAP_HEIGHT/2
return new Rectangle((int)x, 0, PIPE_WIDTH, topHeight)
```

`getBottomBounds(): Rectangle`

```
bottomY = gapY + GAP_HEIGHT/2
bottomHeight = HEIGHT - bottomY
return new Rectangle((int)x, bottomY, PIPE_WIDTH, bottomHeight)
```

**Class:** PipeManager

**State:**

- pipes: List<Pipe>
- spawnTimer: double

**Methods:**

`update(deltaTime):`

```
spawnTimer += deltaTime
if spawnTimer >= SPAWN_INTERVAL:
    spawnPipe()
    spawnTimer = 0

for each pipe in pipes:
    pipe.update(deltaTime)

remove pipes where x + PIPE_WIDTH < 0
```

`spawnPipe():`

```
gapY = random between MIN_GAP_Y and MAX_GAP_Y
pipe = new Pipe(x = WIDTH, gapY)
pipes.add(pipe)
```

`reset():`

```
pipes.clear()
spawnTimer = 0
```

**Interactions:**

- Game Loop calls update()
- Collision System iterates pipes for bounds
- Score System checks passed flag
- Rendering System iterates pipes for drawing

### 5. Collision Detection System

**Class:** CollisionDetector

**Constants:**

- GROUND_Y = HEIGHT - 50 (top of ground)

**Methods:**

`checkCollision(bird, pipeManager): boolean`

```
birdBounds = bird.getBounds()

// Ground collision
if birdBounds.y + birdBounds.height >= GROUND_Y:
    return true

// Ceiling collision (optional, enabled)
if birdBounds.y <= 0:
    return true

// Pipe collision
for each pipe in pipeManager.pipes:
    if birdBounds.intersects(pipe.getTopBounds()):
        return true
    if birdBounds.intersects(pipe.getBottomBounds()):
        return true

return false
```

**Interactions:**

- Game Loop calls checkCollision()
- Returns result to Game State System (triggers GAME_OVER)

### 6. Score System

**Class:** ScoreManager

**State:**

- score: int
- highScore: int

**Methods:**

`update(bird, pipeManager):`

```
for each pipe in pipeManager.pipes:
    if not pipe.passed:
        if bird.BIRD_X > pipe.x + PIPE_WIDTH:
            pipe.passed = true
            score++
```

`reset():`

```
if score > highScore:
    highScore = score
score = 0
```

`getScore(): int` `getHighScore(): int`

**Interactions:**

- Game Loop calls update()
- Rendering System reads score for display
- Game State System calls reset() on game over

### 7. Game State System

**Enum:** GameState { MENU, PLAYING, GAME_OVER }

**State:**

- currentState: GameState

**Transitions:**

```
MENU:
  - On input: transition to PLAYING, reset bird and pipes

PLAYING:
  - On collision: transition to GAME_OVER

GAME_OVER:
  - On input: transition to MENU (or directly to PLAYING with reset)
```

**Interactions:**

- Input System triggers transitions
- Collision System triggers GAME_OVER
- Game Loop checks state before updating
- Rendering System checks state for UI

### 8. Input System

**Implementation:** KeyListener on JFrame

**Key Bindings:**

- SPACE: flap/start/restart
- ESCAPE: quit (optional)

**Logic:**

`keyPressed(KeyEvent e):`

```
if e.getKeyCode() == VK_SPACE:
    switch currentState:
        MENU:
            resetGame()
            currentState = PLAYING
        PLAYING:
            bird.flap()
        GAME_OVER:
            resetGame()
            currentState = PLAYING
```

**Interactions:**

- Modifies Game State
- Calls Bird.flap()
- Triggers game reset

### 9. Rendering System

**Implementation:** Override paintComponent(Graphics g) in GamePanel

**Render Order (back to front):**

1. Background (sky)
2. Pipes
3. Ground
4. Bird
5. Score
6. State-specific UI

**Drawing Details:**

`paintComponent(Graphics g):`

```
Graphics2D g2d = (Graphics2D) g

// 1. Background
g2d.setColor(SKY_BLUE)
g2d.fillRect(0, 0, WIDTH, HEIGHT)

// 2. Pipes (green rectangles)
g2d.setColor(PIPE_GREEN) // RGB 34, 139, 34
for each pipe in pipeManager.pipes:
    g2d.fill(pipe.getTopBounds())
    g2d.fill(pipe.getBottomBounds())

// 3. Ground (brown rectangle)
g2d.setColor(GROUND_BROWN) // RGB 139, 69, 19
g2d.fillRect(0, GROUND_Y, WIDTH, HEIGHT - GROUND_Y)

// 4. Bird (yellow circle)
g2d.setColor(BIRD_YELLOW) // RGB 255, 255, 0
g2d.fillOval(BIRD_X, (int)bird.y, BIRD_SIZE, BIRD_SIZE)

// 5. Score (white text, top center)
g2d.setColor(Color.WHITE)
g2d.setFont(new Font("Arial", Font.BOLD, 36))
g2d.drawString(String.valueOf(score), WIDTH/2 - 10, 50)

// 6. State UI
if currentState == MENU:
    drawCenteredText("Press SPACE to Start", HEIGHT/2)
if currentState == GAME_OVER:
    drawCenteredText("Game Over", HEIGHT/2 - 30)
    drawCenteredText("Score: " + score, HEIGHT/2)
    drawCenteredText("Press SPACE to Restart", HEIGHT/2 + 30)
```

**Interactions:**

- Reads from Bird, PipeManager, ScoreManager, GameState
- Called by repaint() from Game Loop

---

## Third Pass: Integration Review

### Issue 1: Ground Height Constant Duplication

**Problem:** GROUND_Y is defined in CollisionDetector but also needed in Rendering and Pipe systems.

**Solution:** Move GROUND_Y to a shared Constants class or define in GamePanel and reference from there.

**Resolution:** Create a Constants class with all shared values.

### Issue 2: Pipe Gap Bounds Calculation

**Problem:** MAX_GAP_Y references GROUND_HEIGHT which isn't defined. Should use GROUND_Y.

**Solution:** MAX_GAP_Y = GROUND_Y - GAP_HEIGHT/2 - buffer (say 50 pixels)

**Resolution:** MAX_GAP_Y = 600 - 50 - 75 - 50 = 425. So gap center ranges from 100 to 425.

### Issue 3: Bird Hitbox Shape Mismatch

**Problem:** Bird is drawn as circle (fillOval) but collision uses Rectangle. This creates corner inaccuracy.

**Solution:** Accept this for MVP. Rectangle hitbox is simpler and slightly harder (fair for game difficulty). Could
shrink hitbox slightly for forgiveness.

**Resolution:** Keep rectangle hitbox but make it slightly smaller than visual: HITBOX_PADDING = 3 pixels on each side.

### Issue 4: First Pipe Timing

**Problem:** If pipe spawns immediately, player has no reaction time.

**Solution:** Initialize spawnTimer to negative value or spawn first pipe after delay.

**Resolution:** Set initial spawnTimer = -1.0 so first pipe spawns at 0.5 seconds into game.

### Issue 5: Score Display Position

**Problem:** Score drawn at fixed position might overlap with pipes.

**Solution:** Draw score with shadow/outline for visibility, or place in safe zone.

**Resolution:** Add black outline to score text (draw black text offset by 2 pixels, then white on top).

### Issue 6: Game Reset Coordination

**Problem:** Multiple systems need reset (Bird, PipeManager, ScoreManager). Who coordinates?

**Solution:** GamePanel owns all systems and has a resetGame() method that calls each reset.

**Resolution:**

```
resetGame():
    bird.reset()
    pipeManager.reset()
    scoreManager.reset()
```

### Issue 7: Delta Time Consistency

**Problem:** Using fixed deltaTime (0.016) but Timer isn't guaranteed to fire exactly on time.

**Solution:** For MVP, fixed timestep is fine. Physics will be consistent. Visual timing might vary slightly but
acceptable.

**Resolution:** Keep fixed deltaTime. If issues arise, can switch to variable deltaTime with System.nanoTime().

### Issue 8: Pipe Passed Detection Edge Case

**Problem:** If bird dies while passing a pipe, should score count?

**Solution:** Check score before collision in game loop order.

**Resolution:** Game loop order: update bird, update pipes, update score, check collision. Score counts if bird crosses
pipe center before dying.

### Issue 9: Window Focus for Input

**Problem:** KeyListener might not receive events if window loses focus.

**Solution:** Request focus on window creation. Use setFocusable(true) on GamePanel.

**Resolution:** In GamePanel constructor: setFocusable(true) and requestFocusInWindow() after adding to frame.

### Issue 10: Class Structure

**Problem:** Need to decide file organization. Single file or multiple?

**Solution:** For Java 25 with implicit classes, could use single file. But for clarity, use traditional multi-file
structure.

**Resolution:**

- Main.java - entry point only
- GamePanel.java - main game class
- Bird.java - bird entity
- Pipe.java - single pipe
- PipeManager.java - manages pipe collection
- Constants.java - shared constants

---

## Implementation Checklist

### Phase 1: Foundation

- [ ] Create Constants.java with all shared values
- [ ] Create GamePanel.java with window setup
- [ ] Implement basic game loop (Timer)
- [ ] Test: Window opens, closes properly

### Phase 2: Bird

- [ ] Create Bird.java with physics
- [ ] Add bird rendering (yellow circle)
- [ ] Add gravity in game loop
- [ ] Add flap on spacebar
- [ ] Test: Bird falls and flaps

### Phase 3: Pipes

- [ ] Create Pipe.java with bounds methods
- [ ] Create PipeManager.java with spawn/update logic
- [ ] Add pipe rendering (green rectangles)
- [ ] Add ground rendering
- [ ] Test: Pipes scroll left, new pipes spawn

### Phase 4: Collision and Score

- [ ] Add collision detection
- [ ] Add score tracking
- [ ] Add score display
- [ ] Test: Game ends on collision, score increments

### Phase 5: Game States

- [ ] Add GameState enum
- [ ] Implement MENU state with start prompt
- [ ] Implement GAME_OVER state with restart
- [ ] Add reset functionality
- [ ] Test: Full game loop works

### Phase 6: Polish

- [ ] Add score text outline for visibility
- [ ] Tune physics values (gravity, flap, speed)
- [ ] Add high score tracking
- [ ] Final testing and adjustments
