import java.util.*;

public class Board {

    // a 2D array that stores the cells of the board (padded with null references where the row is not full of cells)
    private final Cell[][] cells;
    private final int BOARD_SIZE = 9;
    public int cellsNumber = 0;
    private final double ROW_HEIGHT = 50.0;

    // Board constructor creates the board and each of its cells and also sets up the neighbours of each cell
    public Board() {
        cells = new Cell[BOARD_SIZE][];
        for (int i = 0; i < BOARD_SIZE; i++) {
            int rowLength = i < BOARD_SIZE / 2 ? i + 5 : BOARD_SIZE - i + 4;
            cells[i] = new Cell[rowLength];
        }
        setupNeighbours();
    }

    // Set up neighbours for each cell
    private void setupNeighbours() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            int rowLength = cells[i].length;
            double yOffset = (BOARD_SIZE - rowLength) * ROW_HEIGHT / 2.0;
            for (int j = 0; j < rowLength; j++) {
                Cell cell = cells[i][j];
                if (cell == null) continue; // Skip null cells

                // Set neighbours
                if (j > 0) cell.setNeighbour(5, cells[i][j - 1]); // Left neighbour

                if (i > 0) {
                    if (i <= BOARD_SIZE / 2) {
                        cell.setNeighbour(0, cells[i - 1][j]); // Upper-left neighbour
                        if (j < cells[i - 1].length) {
                            cell.setNeighbour(1, cells[i - 1][j + 1]); // Upper-right neighbour
                        }
                    } else {
                        cell.setNeighbour(3, cells[i - 1][j]); // Lower-left neighbour
                        if (j > 0) cell.setNeighbour(4, cells[i - 1][j - 1]); // Lower-right neighbour
                    }

                }
                // Set vertical position
                double yPosition = yOffset + j * ROW_HEIGHT;
                cell.setY(yPosition);
            }
        }
    }

    public Cell[][] getCells() {
        return cells;
    }

    public void generateRandomAtoms(int maxAtoms) {
        int count = 0;
        for (Cell[] row : cells) for (Cell cell : row) count++;

        cellsNumber = count;
        System.out.println(cellsNumber);

    }


}