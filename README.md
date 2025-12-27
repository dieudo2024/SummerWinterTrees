# Summer Winter Trees

Summer Winter Trees is a lightweight Java Swing application that renders an interactive fractal tree. The visualization supports seasonal color themes and real-time adjustments via keyboard controls, making it suitable for demonstrations of recursion and simple graphics programming.

## Features
- Recursive rendering of a fractal tree with anti-aliased branches
- Instant summer ↔ winter palette toggling with revised depth limits
- Runtime control of recursion depth and branch skewness through keyboard input
- Standalone entry point for rapid execution and experimentation

## Prerequisites
- Java Development Kit (JDK) 17 or newer
- Command-line access to `javac` and `java`

## Setup
1. Clone or download the repository to your local machine.
2. Open a terminal in the project root directory.
3. Compile the sources:
   ```bash
   javac -d out src/SummerTreeAbstract.java src/SummerTree.java
   ```
4. Run the application:
   ```bash
   java -cp out SummerTree
   ```

## Controls
- Arrow Up / Arrow Down: Increase or decrease recursion depth within safe bounds
- Arrow Left / Arrow Right: Adjust branch skewness incrementally
- Space: Toggle winter mode, which updates colors and depth constraints

## Project Structure
- `src/SummerTreeAbstract.java` – Swing frame, rendering helpers, seasonal UI management, and input plumbing
- `src/SummerTree.java` – Concrete tree implementation, recursion logic, and application entry point

## Usage Notes
- The canvas requests focus automatically on launch; click the drawing area if keyboard input is ignored.
- Depth is clamped to prevent excessive recursion; winter mode enforces a lower maximum to maintain performance.
