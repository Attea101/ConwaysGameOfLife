import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class ConwaysGameOfLifeGUI extends JPanel {
    private final ConwaysByAttea gameOfLife;
    private final int cellSize = 20;
    private final int transitionDelay = 100; // Delay between each diagonal step
    private int currentDiagonalStep = 0; // Tracks the diagonal transition progress
    private boolean isTransitioningToFlag = false; // Flag to indicate when the transition starts
    private final Color transLightBlue = new Color(85, 205, 252);
    private final Color transLightPink = new Color(247, 168, 184);
    private final Color transWhite = Color.WHITE;
    private final int transitionStartDelay = 10000; // 10 seconds before starting the transition
    private double time = 0; // Time variable to animate the wave

    public ConwaysGameOfLifeGUI() {
        gameOfLife = new ConwaysByAttea(30, 60);
        startConwaysGameOfLife();
        initiateTransitions();
    }

    // Start Conway's Game of Life
    private void startConwaysGameOfLife() {
        Timer gameTimer = new Timer();
        gameTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isTransitioningToFlag) {
                    gameOfLife.updateGrid();
                }
                repaint();
            }
        }, 0, 100);
    }

    // Initialize both the flag transition and waving effect
    private void initiateTransitions() {
        startFlagTransition();
        startWavingEffect();
    }

    private void startFlagTransition() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                isTransitioningToFlag = true;
                startDiagonalFlagTransition();
            }
        }, transitionStartDelay);
    }

    private void startDiagonalFlagTransition() {
        Timer transitionTimer = new Timer();
        transitionTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                currentDiagonalStep++;
                markFlagCells(); // Mark cells for the trans flag transition
                repaint();

                if (currentDiagonalStep > gameOfLife.getRows() + gameOfLife.getCols()) {
                    transitionTimer.cancel();
                }
            }
        }, 0, transitionDelay);
    }

    // Mark cells with FLAG_COLOR as part of the transition
    private void markFlagCells() {
        for (int row = 0; row < gameOfLife.getRows(); row++) {
            for (int col = 0; col < gameOfLife.getCols(); col++) {
                if (row + col < currentDiagonalStep) {
                    gameOfLife.setFlagColor(new Point(row, col));
                }
            }
        }
    }

    private void startWavingEffect() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                time += 0.1;  // Adjust the speed of the wave
                repaint();
            }
        }, 0, 50); // Repaint every 50 milliseconds
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Loop through the grid and paint cells based on either game state or flag transition
        for (int row = 0; row < gameOfLife.getRows(); row++) {
            for (int col = 0; col < gameOfLife.getCols(); col++) {
                Point cell = new Point(row, col);
                int waveOffset = (int) (10 * Math.sin((col + time) / 2));

                ConwaysByAttea.CellState cellState = gameOfLife.getGrid().getOrDefault(cell, ConwaysByAttea.CellState.DEAD);
                
                if (cellState == ConwaysByAttea.CellState.FLAG_COLOR) {
                    setFlagColor(g, row);
                } else {
                    g.setColor(cellState == ConwaysByAttea.CellState.ALIVE ? Color.BLACK : Color.GRAY);
                }
                
                g.fillRect(col * cellSize, (row * cellSize) + waveOffset, cellSize, cellSize);
            }
        }
    }

    // Set color based on row to maintain the trans flag pattern
    private void setFlagColor(Graphics g, int row) {
        int flagSectionHeight = gameOfLife.getRows() / 5;
        if (row < flagSectionHeight || row >= 4 * flagSectionHeight) {
            g.setColor(transLightBlue);
        } else if ((row >= flagSectionHeight && row < 2 * flagSectionHeight) ||
                   (row >= 3 * flagSectionHeight && row < 4 * flagSectionHeight)) {
            g.setColor(transLightPink);
        } else {
            g.setColor(transWhite);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Conway's Game of Life - Diagonal Trans Flag Transition");
        ConwaysGameOfLifeGUI gameOfLifePanel = new ConwaysGameOfLifeGUI();
        frame.add(gameOfLifePanel);
        frame.setSize(1200, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
