# Summer Winter Trees

A small Swing application that renders an interactive fractal tree and can toggle between summer and winter appearances. The tree reacts to keyboard input so you can explore different branching depths and skews in real time.

## Features
- Recursive drawing of a fractal tree with smooth anti-aliased branches
- Seasonal color palette with on-the-fly toggle between summer and winter looks
- Adjustable recursion depth and branch skewness using arrow keys
- Simple command-line entry point for quick testing

## Controls
- **Arrow Up / Down**: Increase or decrease recursion depth within safe bounds
- **Arrow Left / Right**: Adjust branch skewness incrementally
- **Space**: Toggle winter mode, which changes colors and depth limits

## Getting Started
1. Ensure you have Java 17 or later installed (`java -version`).
2. Compile the sources:
   ```bash
   javac -d out src/SummerTreeAbstract.java src/SummerTree.java
   ```
3. Run the application:
   ```bash
   java -cp out SummerTree
   ```

## Project Structure
- `src/SummerTreeAbstract.java` – Swing frame, rendering helpers, and input plumbing
- `src/SummerTree.java` – Concrete tree behavior, recursion logic, and entry point

## Notes
- The window grabs keyboard focus when it appears. If arrow keys do not respond, click the canvas area once.
- Depth is clamped to avoid runaway recursion; winter mode uses a lower maximum.
