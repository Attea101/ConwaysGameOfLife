import java.awt.*;

public interface ConwaysGameOfLife {
    boolean liveCellWithFewerThanTwoLiveNeighboursDies(Point point);
    boolean liveCellWithTwoOrThreeLiveNeighboursLives(Point point);
    boolean liveCellWithMoreThanThreeLiveNeighboursDies(Point point);
    boolean deadCellWithExactlyThreeLiveNeighboursBecomesAlive(Point point);
}
