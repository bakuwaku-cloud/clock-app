# Stardew-Inspired JavaFX Clock

## Overview

This project is a simple clock application built using **JavaFX** and **Java Threads**. It displays the current system date and time both visually (on a styled clock interface) and in the console.

The design is inspired by the in-game clock from *Stardew Valley*, where each section of the date and time is placed into its own container.

## Features

* Live clock synced to system time (`LocalDateTime.now()`)
* Multithreaded design (background update thread + UI thread)
* Thread priority usage for smoother updates
* Custom JavaFX UI with image-based layout
* Dynamic text centering using `StackPane`
* Individual time digit rendering (HH:mm:ss split into boxes)
* Console output that updates in-place (no log spam)

## How It Works

### 1. Clock Data

The application retrieves the current system time using:

```java
LocalDateTime.now();
```

Different `DateTimeFormatter` patterns are used to format:

* Day (`EEEE`)
* Month (`MMMM`)
* Year (`yyyy`)
* Date (`M/dd/yyyy`)
* Time (`HH:mm:ss`)

### 2. Threading Model

A background thread continuously:

1. Gets the current time
2. Formats it
3. Updates the console
4. Updates the UI safely using:

```java
Platform.runLater(() -> { ... });
```

Thread configuration:

* Runs infinitely with a `while(true)` loop
* Sleeps for 1 second between updates
* Uses `Thread.MAX_PRIORITY` for smoother timing

### 3. UI Layout

* The clock image is used as a **background**
* Text is layered on top using:

  * `StackPane` (for centered text containers)
  * `Label` (for text display)

Each section (day, month, etc.) is placed inside its own invisible container so text remains centered regardless of length.

### 4. Time Rendering

Instead of one label for time, the app uses:

```java
Label[] timeLabels = new Label[8];
```

Each character in `"HH:mm:ss"` is placed into its own box, allowing precise alignment with the UI.

### 5. Console Output

The console updates in place using:

```java
System.out.print("\r" + formattedTime);
```

This prevents clutter and simulates a real-time terminal clock.

## Installation 

Download:
https://www.oracle.com/java/technologies/downloads/

Verify installation:

`java -version`
`javac -version`

2. Install JavaFX SDK 26

Download:
https://openjfx.io/

Extract to: `C:/YOUR/PATH/javafx-sdk-26/`

## VS Code Setup (Optional but Recommended) 
If you are using Visual Studio Code, you can configure JavaFX and Gson so you can run the app directly from the editor. 

### 1. Install Extensions 
Install: - Extension Pack for Java - Java Debugger --- 

### 2. Create .vscode/launch.json 
Create a folder named .vscode in your project root, then add:
```bash
{"version": "0.2.0",
  "configurations": [
    {
      "type": "java",
      "name": "Current File",
      "request": "launch",
      "mainClass": "App",
      "vmArgs": "--module-path \"C:\\Program Files\\Java\\javafx-sdk-26\\lib\" --add-modules javafx.controls"
    }
  ]
}
```

### 3. Create .vscode/settings.json
```bash
{
    "java.project.sourcePaths": ["src"],
    "java.project.outputPath": "bin",
    "java.project.referencedLibraries": [
        "lib/**/*.jar",
        "C:/Program Files/Java/javafx-sdk-26/lib/**/*.jar"
    ]
}
```

## How to Run

### 1. Compile

```bash
javac --module-path "C:\PATH\TO\javafx-sdk-26\lib" --add-modules javafx.controls -d bin src/App.java
```

### 2. Run

```bash
java --module-path "C:\PATH\TO\javafx-sdk-26\lib" --add-modules javafx.controls -cp bin App
```

##  License

MIT License

Copyright (c) 2026 Angelica Strong

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to do so, subject to the
following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.