import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

public class ConwaysByAttea implements ConwaysGameOfLife {

    public enum CellState { ALIVE, DEAD, FLAG_COLOR } // Define cell states
    
    private final int rows; // Number of rows in the grid
    private final int cols; // Number of columns in the grid
    private Map<Point, CellState> grid; // Map to hold the grid cells

    public ConwaysByAttea(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new HashMap<>(); // Initialize the grid
        initializeGrid(); // Populate the grid with random states
    }

    private void initializeGrid() {
        // Randomly set cells to alive or dead
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                grid.put(new Point(row, col), Math.random() < 0.5 ? CellState.ALIVE : CellState.DEAD);
            }
        }
    }
    
    private int countLiveNeighbours(Point point) {
        // Count the number of live neighbors for a given cell
        int liveNeighbours = 0;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue; // Skip the cell itself
                Point neighbour = new Point(point.x + dx, point.y + dy);
                if (grid.getOrDefault(neighbour, CellState.DEAD) == CellState.ALIVE) {
                    liveNeighbours++;
                }
            }
        }
        return liveNeighbours; // Return the count of live neighbors
    }

    public void updateGrid() {
        // Create a copy of the current grid to apply updates
        Map<Point, CellState> newGrid = new HashMap<>(grid);
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Point cell = new Point(row, col);
                int liveNeighbours = countLiveNeighbours(cell); // Count live neighbors
                
                // Apply the rules of the Game of Life
                if (grid.get(cell) == CellState.ALIVE) {
                    if (liveNeighbours < 2 || liveNeighbours > 3) {
                        newGrid.put(cell, CellState.DEAD); // Cell dies
                    }
                } else {
                    if (liveNeighbours == 3) {
                        newGrid.put(cell, CellState.ALIVE); // Cell becomes alive
                    }
                }
            }
        }
        grid = newGrid; // Update the grid with the new states
    }

    public void setFlagColor(Point cell) {
        // Set a cell's state to FLAG_COLOR for the trans flag transition
        grid.put(cell, CellState.FLAG_COLOR);
    }

    public int getRows() {
        return rows; // Return number of rows
    }

    public int getCols() {
        return cols; // Return number of columns
    }

    public Map<Point, CellState> getGrid() {
        return grid; // Return the grid map
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
}
