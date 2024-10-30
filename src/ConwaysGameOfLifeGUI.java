import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ConwaysGameOfLifeGUI extends JPanel {
    private ConwaysByAttea gameOfLife; // Instance of the Game of Life
    private final int cellSize = 20; // Size of each cell in the grid
    private final int transitionDelay = 100; // Delay for flag transition animation
    private final int transitionStartDelay = 500; // Delay before starting the flag transition
    private final int waveAmplitude = 10; // Amplitude for the waving effect
    private final int wavePeriod = 2; // Period for the waving effect
    private int currentDiagonalStep = 0; // Tracks the current diagonal step in flag transition
    private boolean isTransitioningToFlag = false; // Indicates if the transition to the flag is active
    private boolean isGameOfLifeMode = true; // Indicates if the Game of Life mode is active
    private final Color transLightBlue = new Color(85, 205, 252); // Light blue color for the trans flag
    private final Color transLightPink = new Color(247, 168, 184); // Light pink color for the trans flag
    private final Color transWhite = Color.WHITE; // White color for the trans flag
    private double time = 0; // Tracks time for the waving effect

    public ConwaysGameOfLifeGUI() {
        // Initialize the game with a grid of 30 rows and 60 columns
        gameOfLife = new ConwaysByAttea(30, 60);
        startConwaysGameOfLife(); // Start the Game of Life loop
        addKeyListener(new ToggleListener()); // Add key listener for toggling modes
        setFocusable(true); // Make the panel focusable for key events
    }

    private void startConwaysGameOfLife() {
        // Timer to update the Game of Life at regular intervals
        Timer gameTimer = new Timer();
        gameTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isGameOfLifeMode && !isTransitioningToFlag) {
                    gameOfLife.updateGrid(); // Update the grid if in Game of Life mode
                }
                repaint(); // Repaint the panel to reflect updates
            }
        }, 0, 100); // Run every 100 milliseconds
    }

    private void initiateTransitions() {
        startFlagTransition(); // Start the flag transition
        startWavingEffect(); // Start the waving effect
    }

    private void startFlagTransition() {
        // Delay the start of the flag transition
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isGameOfLifeMode) {
                    isTransitioningToFlag = true; // Set transitioning flag
                    startDiagonalFlagTransition(); // Start diagonal transition for flag
                }
            }
        }, transitionStartDelay);
    }

    private void startDiagonalFlagTransition() {
        Timer transitionTimer = new Timer();
        transitionTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isTransitioningToFlag) {
                    currentDiagonalStep++; // Increment diagonal step
                    markFlagCells(); // Mark cells for the flag
                    repaint(); // Repaint the panel
                }
                // Stop the transition once all cells are marked
                if (currentDiagonalStep > gameOfLife.getRows() + gameOfLife.getCols()) {
                    transitionTimer.cancel();
                }
            }
        }, 0, transitionDelay); // Run at defined transition delay
    }

    private void markFlagCells() {
        // Only mark cells in the diagonal range based on the current diagonal step
        for (int row = 0; row < gameOfLife.getRows(); row++) {
            for (int col = 0; col < gameOfLife.getCols(); col++) {
                // Check if the cell is within the diagonal range
                if (row + col < currentDiagonalStep) {
                    gameOfLife.setFlagColor(new Point(row, col)); // Set cell color to flag color
                }
            }
        }
    }

    private void startWavingEffect() {
        // Timer for the waving effect
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                time += 0.1; // Increment time for wave calculation
                repaint(); // Repaint the panel
            }
        }, 0, 50); // Run every 50 milliseconds
    }

    private class ToggleListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            // Toggle modes when 'T' or 't' is pressed
            if (e.getKeyChar() == 't' || e.getKeyChar() == 'T') {
                isGameOfLifeMode = !isGameOfLifeMode; // Switch between modes
                isTransitioningToFlag = !isGameOfLifeMode; // Set transition flag based on mode
                currentDiagonalStep = 0; // Reset diagonal step

                if (!isGameOfLifeMode) {
                    // Immediately start transitions if switching to flag mode
                    initiateTransitions();
                } else {
                    // Reset the Game of Life when switching back
                    gameOfLife = new ConwaysByAttea(30, 60);
                    startConwaysGameOfLife(); // Restart the Game of Life loop
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Call superclass paint method

        for (int row = 0; row < gameOfLife.getRows(); row++) {
            for (int col = 0; col < gameOfLife.getCols(); col++) {
                Point cell = new Point(row, col);
                // Calculate wave offset for waving effect
                int waveOffset = (int) (waveAmplitude * Math.sin((col + time) / wavePeriod));

                // Get the cell state from the grid
                ConwaysByAttea.CellState cellState = gameOfLife.getGrid().getOrDefault(cell, ConwaysByAttea.CellState.DEAD);
                
                if (cellState == ConwaysByAttea.CellState.FLAG_COLOR) {
                    setFlagColor(g, row); // Set color for flag cells
                } else {
                    // Set color based on alive or dead state
                    g.setColor(cellState == ConwaysByAttea.CellState.ALIVE ? Color.BLACK : Color.GRAY);
                }
                
                // Draw the cell with the calculated wave offset
                g.fillRect(col * cellSize, (row * cellSize) + (isGameOfLifeMode ? 0 : waveOffset), cellSize, cellSize);
            }
        }
    }

    private void setFlagColor(Graphics g, int row) {
        // Determine color based on the row for the trans flag
        int flagSectionHeight = gameOfLife.getRows() / 5; // Height of each flag section
        if (row < flagSectionHeight || row >= 4 * flagSectionHeight) {
            g.setColor(transLightBlue); // Top and bottom sections
        } else if ((row >= flagSectionHeight && row < 2 * flagSectionHeight) ||
                   (row >= 3 * flagSectionHeight && row < 4 * flagSectionHeight)) {
            g.setColor(transLightPink); // Middle sections
        } else {
            g.setColor(transWhite); // White section
        }
    }

    public static void main(String[] args) {
        // Setup the main frame for the game
        JFrame frame = new JFrame("Conway's Game of Life - Press T to Toggle");
        ConwaysGameOfLifeGUI gameOfLifePanel = new ConwaysGameOfLifeGUI();
        frame.add(gameOfLifePanel); // Add the game panel to the frame
        frame.setSize(1200, 600); // Set the frame size
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit on close
        frame.setVisible(true); // Make the frame visible
    }
}