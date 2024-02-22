import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

public class HexagonalBoardUI extends Pane {
    private static final double HEX_SIZE = 50.0;
    private static final double HEX_WIDTH = Math.sqrt(3) * HEX_SIZE;
    private static final double HEX_HEIGHT = 2 * HEX_SIZE;
    private static final Color HEX_COLOR = Color.LIGHTGRAY;

    private final Board board;
    private boolean atomsVisible = true; // Track the visibility state of atoms
    public boolean togglePress = false;

    public HexagonalBoardUI(Board board) {
        this.board = board;
        drawBoard();
    }

    // Method to toggle the visibility of atoms
    public void setAtomsVisible(boolean visible) {
        atomsVisible = visible;
         togglePress = true;
        drawBoard(); // Redraw the board with updated visibility
    }

    public boolean areAtomsVisible() {
        return atomsVisible;
    }

    private void drawBoard() {
        getChildren().clear(); // Clear previous drawings

        Cell[][] cells = board.getCells();

        double horizontalGap = 1; // Adjust horizontal gap
        double verticalGap = 1; // Adjust vertical gap

        boolean isOffset = false; // Flag to track alternating offset

        double sceneWidth = getWidth();
        double sceneHeight = getHeight();

        // TODO
        if(togglePress)sceneWidth /= 200; // a bad fix for a dumb bug

        for (int i = 0; i < cells.length; i++) {
            int rowLength = cells[i].length;
            double rowWidth = HEX_WIDTH * rowLength + (rowLength - 1) * horizontalGap;
            double rowStartX;

            if (isOffset) {
                rowStartX = (sceneWidth - rowWidth) / 2.0 - HEX_WIDTH / 2; // Offset backward
            } else {
                rowStartX = (sceneWidth - rowWidth) / 2.0; // No offset
            }

            for (int j = 0; j < rowLength; j++) {
                double centerX;
                if (i % 2 == 0) {
                    centerX = rowStartX + j * (HEX_WIDTH + horizontalGap) + HEX_WIDTH / 2.0;
                } else {
                    centerX = rowStartX + j * (HEX_WIDTH + horizontalGap) + HEX_WIDTH;
                }
                double centerY = HEX_HEIGHT * i * 0.75 + HEX_HEIGHT / 2 + verticalGap; // Add vertical gap

                // Check if the cell has an atom
                boolean hasAtom = cells[i][j] != null && cells[i][j].hasAtom();

                // Draw hexagon and atom only if atoms are visible
                if (atomsVisible) {
                    drawHexagon(centerX, centerY, hasAtom);
                    if (hasAtom) {
                        drawAtom(centerX, centerY);
                    }
                } else {
                    // Only draw hexagon if atoms are not visible
                    drawHexagon(centerX, centerY, false);
                }
            }
            // Toggle offset flag for the next row
            isOffset = !isOffset;
        }
    }


    private void drawHexagon(double centerX, double centerY, boolean hasAtom) {
        // Create a filled hexagon
        Polygon filledHexagon = new Polygon();
        for (int i = 0; i < 6; i++) {
            double angleRad = Math.toRadians(60 * i - 30);
            double x = centerX + HEX_SIZE * Math.cos(angleRad);
            double y = centerY + HEX_SIZE * Math.sin(angleRad);
            filledHexagon.getPoints().addAll(x, y);
        }
        filledHexagon.setFill(HEX_COLOR);
        getChildren().add(filledHexagon);

        // Create a hexagon outline
        Polygon outlineHexagon = new Polygon();
        for (int i = 0; i < 6; i++) {
            double angleRad = Math.toRadians(60 * i - 30);
            double x = centerX + HEX_SIZE * Math.cos(angleRad);
            double y = centerY + HEX_SIZE * Math.sin(angleRad);
            outlineHexagon.getPoints().addAll(x, y);
        }
        outlineHexagon.setStroke(Color.DARKGRAY); // Set outline color
        outlineHexagon.setFill(Color.TRANSPARENT); // Make sure it's not filled
        outlineHexagon.setStrokeWidth(2); // Set outline width
        getChildren().add(outlineHexagon);
    }

    private void drawAtom(double centerX, double centerY) {
        Circle atomCircle = new Circle(centerX, centerY, HEX_SIZE / 4); // Radius is 1/4 of hexagon size
        atomCircle.setFill(Color.RED); // Atom color
        getChildren().add(atomCircle);
    }
}
