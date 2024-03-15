import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class HexagonalBoardUI extends Pane {
    private static final double HEX_SIZE = 35.0;
    private static final double HEX_WIDTH = Math.sqrt(3) * HEX_SIZE;
    private static final double HEX_HEIGHT = 2 * HEX_SIZE;
    private static final Color HEX_COLOR = Color.LIGHTGRAY;

    private final Board board;
    private boolean atomsVisible = true; // Track the visibility state of atoms

    public HexagonalBoardUI(Board board) {
        this.board = board;
        drawBoard();
    }

    // Method to toggle the visibility of atoms
    public void setAtomsVisible(boolean visible) {
        atomsVisible = visible;
        drawBoard(); // Redraw the board with updated visibility
    }

    public boolean areAtomsVisible() {
        return atomsVisible;
    }

    protected void drawBoard() {
        getChildren().clear(); // Clear previous drawings

        Cell[][] cells = board.getCells();

        double horizontalGap = 1; // Adjust horizontal gap
        double verticalGap = 1; // Adjust vertical gap

        boolean isOffset = false; // Flag to track alternating offset

        double sceneWidth = getWidth() / 4000;

        // List to store the coordinates of cells with atoms
        ArrayList<double[]> atomCoordinates = new ArrayList<>();

        for (int i = 0; i < cells.length; i++) {
            int rowLength = cells[i].length;
            double rowWidth = HEX_WIDTH * rowLength + (rowLength - 1) * horizontalGap;
            double rowStartX;

            if (isOffset) rowStartX = (sceneWidth - rowWidth) / 2.0 - HEX_WIDTH / 2; // Offset backward
            else rowStartX = (sceneWidth - rowWidth) / 2.0; // No offset

            for (int j = 0; j < rowLength; j++) {
                double centerX;
                if (i % 2 == 0) centerX = rowStartX + j * (HEX_WIDTH + horizontalGap) + HEX_WIDTH / 2.0;
                else centerX = rowStartX + j * (HEX_WIDTH + horizontalGap) + HEX_WIDTH;
                double centerY = HEX_HEIGHT * i * 0.75 + HEX_HEIGHT / 2 + verticalGap; // Add vertical gap

                // Check if the cell has an atom
                boolean hasAtom = cells[i][j] != null && cells[i][j].hasAtom();

                // Store the coordinates of cells with atoms
                if (atomsVisible && hasAtom) {
                    atomCoordinates.add(new double[]{centerX, centerY});
                }
                // Draw hexagon
                drawHexagon(centerX, centerY);

                //add input number labels on the edge of the board (if the cell is an edge cell)
                drawNumberLabels(centerX, centerY, cells[i][j]);

                ArrayList<RaySegment> raySegments = cells[i][j].getRaySegments();
                if (!raySegments.isEmpty()) {
                    //draw the ray segments passing through this cell, if any
                    drawRaySegments(centerX, centerY, raySegments);
                }
            }
            // Toggle offset flag for the next row
            isOffset = !isOffset;
        }
//////////////////////////////////////
        // Draw atoms and their circles of influence after drawing all cells
        if (atomsVisible) {
            for (double[] coordinates : atomCoordinates) {
                drawAtom(coordinates[0], coordinates[1]);
                drawAtomCircleOfInfluence(coordinates[0], coordinates[1]);
            }
        }
        /////////////////////////
    }

    private void drawRaySegments(double centerX, double centerY, ArrayList<RaySegment> raySegments) {
        for(RaySegment raySegment: raySegments) {
            Polyline polyline = new Polyline();

            //set polyline coordinates
            polyline.getPoints().addAll(getLineCoordinates((raySegment.entryPoint() + 4) % 6, centerX, centerY));
            polyline.getPoints().addAll(centerX, centerY);
            //if the ray has not been absorbed
            if(raySegment.exitPoint() != -1) {
                polyline.getPoints().addAll(getLineCoordinates((raySegment.exitPoint() + 4) % 6, centerX, centerY));
            }

            //style polyline
            polyline.setStrokeWidth(2);
            polyline.setStroke(Color.GRAY);
            polyline.setStrokeLineJoin(StrokeLineJoin.ROUND);

            getChildren().add(polyline);
        }
    }

    private ArrayList<Double> getLineCoordinates(int i, double centerX, double centerY) {
        ArrayList<Double> coordinates = new ArrayList<>();

        double angleRad1 = Math.toRadians(60 * i - 30);
        double angleRad2 = Math.toRadians(60 * ((i+1) % 6) - 30);
        coordinates.add((centerX + HEX_SIZE * Math.cos(angleRad1) + centerX + HEX_SIZE * Math.cos(angleRad2)) / 2);
        coordinates.add((centerY + HEX_SIZE * Math.sin(angleRad1) + centerY + HEX_SIZE * Math.sin(angleRad2)) / 2);

        return coordinates;
    }

    private void drawHexagon(double centerX, double centerY) {
        // Create a filled hexagon
        Polygon filledHexagon = createFilledHexagon(centerX, centerY);
        getChildren().add(filledHexagon);

        // Create a hexagon outline
        Polygon outlineHexagon = createOutlineHexagon(centerX, centerY);
        getChildren().add(outlineHexagon);
    }

    private Polygon createFilledHexagon(double centerX, double centerY) {
        Polygon filledHexagon = createHexagon(centerX, centerY);
        filledHexagon.setFill(HEX_COLOR);
        return filledHexagon;
    }

    private Polygon createOutlineHexagon(double centerX, double centerY) {
        Polygon outlineHexagon = createHexagon(centerX, centerY);
        outlineHexagon.setStroke(Color.DARKGRAY); // Set outline color
        outlineHexagon.setFill(Color.TRANSPARENT); // Make sure it's not filled
        outlineHexagon.setStrokeWidth(2); // Set outline width
        return outlineHexagon;
    }

    private Polygon createHexagon(double centerX, double centerY) {
        Polygon hexagon = new Polygon();
        for (int i = 0; i < 6; i++) {
            double angleRad = Math.toRadians(60 * i - 30);
            double x = centerX + HEX_SIZE * Math.cos(angleRad);
            double y = centerY + HEX_SIZE * Math.sin(angleRad);
            hexagon.getPoints().addAll(x, y);
        }
        return hexagon;
    }

    private void drawAtom(double centerX, double centerY) {
        Circle atomCircle = new Circle(centerX, centerY, HEX_SIZE / 4); // Radius is 1/4 of hexagon size
        atomCircle.setFill(Color.RED); // Atom color
        getChildren().add(atomCircle);
    }

    ////////
    private void drawAtomCircleOfInfluence(double centerX, double centerY) {
        // Define the radius of the circle of influence
        double influenceRadius = HEX_SIZE * 1.5; // Adjust the multiplier as needed for the desired size

        // Create a transparent circle representing the influence of the atom
        Circle atomInfluenceCircle = new Circle(centerX, centerY, influenceRadius);
        atomInfluenceCircle.setFill(Color.TRANSPARENT);
        atomInfluenceCircle.setStroke(Color.RED);
        atomInfluenceCircle.setStrokeWidth(2); // Adjust the stroke width as needed

        // Add the circle to the pane
        getChildren().add(atomInfluenceCircle);
    }

    private void drawNumberLabels(double centerX, double centerY, Cell cell) {

        if(cell.getCol() == 0) {
            //left label
            addNewLabel(cell.getRow() < 4 ? centerX - 1.25 * HEX_SIZE : centerX - 1.4 * HEX_SIZE,
                    centerY + HEX_SIZE / 8, String.valueOf(cell.getRow() * 2 + 2));
            if(cell.getRow() <= 4) {
                //upper left label
                addNewLabel(centerX - 0.65 * HEX_SIZE, centerY - 0.85 * HEX_SIZE, String.valueOf(cell.getRow()*2+1));
            }
        }

        if(cell.getRow() == 0 && cell.getCol() > 0) {
            //upper left label
            addNewLabel(centerX - 0.75 * HEX_SIZE, centerY - 0.85 * HEX_SIZE, String.valueOf(55 - cell.getCol()*2));
        }
        if((cell.getCol() == 0 && cell.getRow() >= 4) || cell.getRow() == Board.BOARD_SIZE-1) {
            //lower left label
            addNewLabel(centerX - 0.85 * HEX_SIZE, centerY + 1.1 * HEX_SIZE, String.valueOf((cell.getRow()+ cell.getCol())*2+3));

        }
        if(cell.getRow() == Board.BOARD_SIZE-1 || (cell.getRow() >= 4 && cell.getCol() == 12 - cell.getRow())) {
            //lower right label
            addNewLabel(centerX + 0.4 * HEX_SIZE, centerY + 1.1 * HEX_SIZE, String.valueOf(cell.getCol()*2 + 20));
        }
        if(cell.getCol() == 12 - cell.getRow() || cell.getCol() == 4 + cell.getRow()) {
            //right label
            addNewLabel(centerX + HEX_SIZE, centerY + HEX_SIZE / 8, String.valueOf(45 - cell.getRow()*2));
        }
        if(cell.getRow() == 0 || cell.getCol() == 4 + cell.getRow()) {
            //upper right label
            addNewLabel(centerX + 0.45 * HEX_SIZE, centerY - 0.85 * HEX_SIZE, String.valueOf(54 - cell.getCol()*2));
        }

    }

    private void addNewLabel(double x, double y, String text) {
        Text label = new Text(x, y, text);
        label.setStroke(Color.WHITE);
        getChildren().add(label);
    }
}



