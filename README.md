# Conway's Game of Life - Trans Flag Animation by Attea Williams

## Overview

This project implements Conway's Game of Life with a unique twist: a visual transition to a waving trans flag. The game allows you to toggle between traditional Game of Life simulation and a colorful animation representing the trans flag. The cells evolve according to the rules of the Game of Life and upon toggle, will be overwritten to display the trans flag in a waving effect.

## Features

- **Conway's Game of Life Simulation:** Experience the classic game where cells live, die, or reproduce based on their neighbors.
- **Trans Flag Animation:** Transition from the Game of Life to a waving trans flag
- **Interactive Mode Switching:** Toggle between the Game of Life and flag animation using the 'T' key.

## Technologies Used

- Java: The programming language used for implementing the game logic and graphical user interface.
- Swing: A part of Java Foundation Classes (JFC) used to create the graphical user interface (GUI).

## Installation

1. Ensure you have Java Development Kit (JDK) installed on your machine. You can download it from [Oracle's website](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html).

2. Clone this repository to your local machine:
   ```bash
   git clone <repository-url>
   cd <repository-directory>
    
3. Compile the Java files:
    ```bash
    javac ConwaysGameOfLifeGUI.java ConwaysByAttea.java
4. Run the application:
    ```bash
    java ConwaysGameOfLifeGUI

## Usage

### Start the Game: 
- Upon launching, the application will display a grid with randomly populated alive and dead cells.
- Toggle Modes: Press the 'T' key to switch between the Game of Life mode and the trans flag animation. The flag animation will start after a short delay, transitioning from the grid to a waving effect.
- Exit the Application: Close the window to exit the program.

## Game Rules

In Conway's Game of Life, the following rules determine the state of each cell:

- Any live cell with fewer than two live neighbors dies (underpopulation).
- Any live cell with two or three live neighbors lives on to the next generation.
- Any live cell with more than three live neighbors dies (overpopulation).
- Any dead cell with exactly three live neighbors becomes a live cell (reproduction).

## Customization

You can customize the following parameters in the code:

- cellSize: Change the size of each cell in the grid.
- transitionDelay: Adjust the delay for the flag transition animation.
- transitionStartDelay: Modify the delay before starting the flag transition.
- waveAmplitude: Change the amplitude of the waving effect.
- wavePeriod: Adjust the period for the waving effect.
