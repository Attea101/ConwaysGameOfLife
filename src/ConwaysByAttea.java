import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

public class ConwaysByAttea implements ConwaysGameOfLife {

    private final int rows;
    private final int cols;
    private Map<Point, Boolean> grid;

    public ConwaysByAttea(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new HashMap<>();
        initializeGrid();
    }

    // Initialize grid with random live and dead cells
    private void initializeGrid() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                grid.put(new Point(row, col), Math.random() < 0.5);
            }
        }
    }

    // Getter for rows
    public int getRows() {
        return rows;
    }

    // Getter for columns
    public int getCols() {
        return cols;
    }

    // Count live neighbors around a cell
    private int countLiveNeighbours(Point point) {
        int liveNeighbours = 0;
        int[] directions = {-1, 0, 1};

        for (int i : directions) {
            for (int j : directions) {
                if (i == 0 && j == 0) continue; // Skip the point itself
                Point neighbour = new Point(point.x + i, point.y + j);
                if (grid.getOrDefault(neighbour, false)) {
                    liveNeighbours++;
                }
            }
        }
        return liveNeighbours;
    }

    @Override
    public boolean liveCellWithFewerThanTwoLiveNeighboursDies(Point point) {
        return countLiveNeighbours(point) < 2;
    }

    @Override
    public boolean liveCellWithTwoOrThreeLiveNeighboursLives(Point point) {
        int liveNeighbours = countLiveNeighbours(point);
        return liveNeighbours == 2 || liveNeighbours == 3;
    }

    @Override
    public boolean liveCellWithMoreThanThreeLiveNeighboursDies(Point point) {
        return countLiveNeighbours(point) > 3;
    }

    @Override
    public boolean deadCellWithExactlyThreeLiveNeighboursBecomesAlive(Point point) {
        return countLiveNeighbours(point) == 3;
    }

    // Update the grid for the next generation
    public void updateGrid() {
        Map<Point, Boolean> newGrid = new HashMap<>();

        for (Map.Entry<Point, Boolean> entry : grid.entrySet()) {
            Point point = entry.getKey();
            boolean isAlive = entry.getValue();
            boolean nextState;

            if (isAlive) {
                if (liveCellWithFewerThanTwoLiveNeighboursDies(point) ||
                    liveCellWithMoreThanThreeLiveNeighboursDies(point)) {
                    nextState = false;
                } else {
                    nextState = true;
                }
            } else {
                nextState = deadCellWithExactlyThreeLiveNeighboursBecomesAlive(point);
            }

            newGrid.put(point, nextState);
        }

        grid = newGrid; // Update the grid to the new state
    }

    public Map<Point, Boolean> getGrid() {
        return grid;
    }
}
