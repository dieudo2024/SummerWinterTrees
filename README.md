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
### Command Line
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

### Visual Studio Code
1. Install the "Extension Pack for Java" (or at minimum "Language Support for Java by Red Hat") in VS Code.
2. Open the project folder in VS Code (`File` → `Open Folder…`).
3. When prompted, trust the authors and allow VS Code to configure the Java project.
4. Use the Run and Debug view (Ctrl+Shift+D) to launch `SummerTree` directly, or right-click `SummerTree.java` in the Explorer and choose "Run Java".
5. If you prefer tasks, create `.vscode/tasks.json` with a `javac` build task and trigger it via `Terminal` → `Run Build Task…` before running.

### IntelliJ IDEA
1. Launch IntelliJ IDEA and select `Open` to choose the project directory.
2. When the import dialog appears, accept the default "Create project from existing sources" and ensure the JDK 17 (or newer) SDK is selected.
3. Allow IntelliJ to index the project; verify there are no errors in the `Project` tool window.
4. Open `SummerTree.java`, then click the gutter play icon beside the `main` method and select `Run 'SummerTree.main()'`.
5. Optionally, create an application run configuration (Run → Edit Configurations…) targeting `SummerTree` for repeated launches.

## Controls
- Arrow Up / Arrow Down: Increase or decrease recursion depth within safe bounds
- Arrow Left / Arrow Right: Adjust branch skewness incrementally
- Space: Toggle winter mode, which updates colors and depth constraints

## Project Structure
- `src/SummerTreeAbstract.java` – Swing frame, rendering helpers, seasonal UI management, and input plumbing
- `src/SummerTree.java` – Concrete tree implementation, recursion logic, and application entry point

## Demo
<video controls autoplay loop muted playsinline width="640">
   <source src="SummerWinterTrees%20Recording%202025-12-25%20234004.mp4" type="video/mp4" />
   Your browser does not support the video tag. You can download the clip instead: <a href="SummerWinterTrees%20Recording%202025-12-25%20234004.mp4">Watch the application demo</a>.
</video>

## Usage Notes
- The canvas requests focus automatically on launch; click the drawing area if keyboard input is ignored.
- Depth is clamped to prevent excessive recursion; winter mode enforces a lower maximum to maintain performance.
