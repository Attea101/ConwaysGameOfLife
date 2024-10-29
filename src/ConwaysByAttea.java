import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

public class ConwaysByAttea implements ConwaysGameOfLife {

    public enum CellState { ALIVE, DEAD, FLAG_COLOR }
    
    private final int rows;
    private final int cols;
    private Map<Point, CellState> grid;

    public ConwaysByAttea(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new HashMap<>();
        initializeGrid();
    }

    // Initializes the grid with random live and dead cells
    private void initializeGrid() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                grid.put(new Point(row, col), Math.random() < 0.5 ? CellState.ALIVE : CellState.DEAD);
            }
        }
    }
    
    // Counts the live neighbors around a cell
    private int countLiveNeighbours(Point point) {
        int liveNeighbours = 0;
        int[] directions = {-1, 0, 1};

        for (int i : directions) {
            for (int j : directions) {
                if (i == 0 && j == 0) continue;
                Point neighbour = new Point(point.x + i, point.y + j);
                if (grid.getOrDefault(neighbour, CellState.DEAD) == CellState.ALIVE) {
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

    // Updates the grid for the next generation
    public void updateGrid() {
        Map<Point, CellState> newGrid = new HashMap<>(grid.size());

        for (Map.Entry<Point, CellState> entry : grid.entrySet()) {
            Point point = entry.getKey();
            CellState currentState = entry.getValue();
            CellState nextState;

            if (currentState == CellState.ALIVE) {
                nextState = liveCellWithTwoOrThreeLiveNeighboursLives(point) ? CellState.ALIVE : CellState.DEAD;
            } else {
                nextState = deadCellWithExactlyThreeLiveNeighboursBecomesAlive(point) ? CellState.ALIVE : CellState.DEAD;
            }

            newGrid.put(point, nextState);
        }

        grid = newGrid;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Map<Point, CellState> getGrid() {
        return grid;
    }

    // Method to set a cell as part of the flag color
    public void setFlagColor(Point point) {
        grid.put(point, CellState.FLAG_COLOR);
    }
}
