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
        startTransitionToFlag();
        startWavingEffect();
    }

    // Start Conway's Game of Life rules
    private void startConwaysGameOfLife() {
        Timer gameTimer = new Timer();
        gameTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isTransitioningToFlag) {
                    gameOfLife.updateGrid();
                    repaint();
                }
            }
        }, 0, 100); // Update every 100 milliseconds
    }

    // Start transition to trans flag after 10 seconds
    private void startTransitionToFlag() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                isTransitioningToFlag = true;
                startDiagonalFlagTransition();
            }
        }, transitionStartDelay);
    }

    // Transition the game to the trans flag diagonally
    private void startDiagonalFlagTransition() {
        Timer transitionTimer = new Timer();
        transitionTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                currentDiagonalStep++;
                repaint();

                // Stop when the diagonal has reached the entire grid
                if (currentDiagonalStep > gameOfLife.getRows() + gameOfLife.getCols()) {
                    transitionTimer.cancel();
                }
            }
        }, 0, transitionDelay); // Trigger each diagonal step with a delay
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
                int waveOffset = (int) (10 * Math.sin((col + time) / 2)); // Adjust wave parameters
                if (isTransitioningToFlag && row + col < currentDiagonalStep) {
                    // Paint trans flag colors based on the current row
                    setFlagColor(g, row);
                } else {
                    // Paint Game of Life cells
                    g.setColor(gameOfLife.getGrid().get(cell) ? Color.BLACK : Color.GRAY);
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
