import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;

public class BoardUI extends Pane {
    private static final double HEX_SIZE = 35.0;
    private static final double HEX_WIDTH = Math.sqrt(3) * HEX_SIZE;
    private static final double HEX_HEIGHT = 2 * HEX_SIZE;
    private static final Color HEX_COLOR = Color.LIGHTGRAY;

    private final Board board;
    private boolean atomsVisible = true; // Track the visibility state of atoms
    public boolean weirdBoard = false;

    public BoardUI(Board board) {
        this.board = board;
        drawBoard();
    }

    // Method to toggle the visibility of atoms
    public void setAtomsVisible(boolean visible) {
        atomsVisible = visible;
        weirdBoard = true;
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

        double sceneWidth = getWidth();
        double sceneHeight = getHeight();

        // TODO
        if (weirdBoard) sceneWidth /= 4000; // a bad fix for a dumb bug

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

                // Draw hexagon only if atoms are visible
                if (atomsVisible) {
                    drawHexagon(centerX, centerY, hasAtom);
                    if (hasAtom) {
                        // Store the coordinates of cells with atoms
                        atomCoordinates.add(new double[]{centerX, centerY});
                    }
                } else drawHexagon(centerX, centerY, false);

                ArrayList<RaySegment> raySegments = cells[i][j].getRaySegments();
                if (raySegments.size() > 0) {
                    // Draw a gray hexagon to mark the cell as part of the ray's path
                    drawHexagon(centerX, centerY, true);

                    // Draw the ray segments
                    // Inside the drawBoard method
                    // Inside the drawBoard method
                    // Inside the drawBoard method
                    for (RaySegment segment : raySegments) {
                        double entryX, entryY, exitX, exitY;

                        // Calculate entry point coordinates based on entry position
                        switch (segment.entryPoint()) {
                            case 0: // Entering through upper-left
                                entryX = centerX - HEX_SIZE * 0.5;
                                entryY = centerY - HEX_SIZE * Math.sqrt(3) / 2;
                                break;
                            case 1: // Entering through upper-right
                                entryX = centerX + HEX_SIZE * 0.5;
                                entryY = centerY - HEX_SIZE * Math.sqrt(3) / 2;
                                break;
                            case 2: // Entering through right
                                entryX = centerX + HEX_SIZE;
                                entryY = centerY;
                                break;
                            case 3: // Entering through lower-right
                                entryX = centerX + HEX_SIZE * 0.5;
                                entryY = centerY + HEX_SIZE * Math.sqrt(3) / 2;
                                break;
                            case 4: // Entering through lower-left
                                entryX = centerX - HEX_SIZE * 0.5;
                                entryY = centerY + HEX_SIZE * Math.sqrt(3) / 2;
                                break;
                            case 5: // Entering through left
                                entryX = centerX - HEX_SIZE;
                                entryY = centerY;
                                break;
                            default: // Ray absorbed, entry coordinates same as exit coordinates
                                entryX = centerX;
                                entryY = centerY;
                        }

                        // Calculate exit point coordinates based on exit position
                        switch (segment.exitPoint()) {
                            case 0: // Exiting through upper-left
                                exitX = centerX - HEX_SIZE * 0.5;
                                exitY = centerY - HEX_SIZE * Math.sqrt(3) / 2;
                                break;
                            case 1: // Exiting through upper-right
                                exitX = centerX + HEX_SIZE * 0.5;
                                exitY = centerY - HEX_SIZE * Math.sqrt(3) / 2;
                                break;
                            case 2: // Exiting through right
                                exitX = centerX + HEX_SIZE;
                                exitY = centerY;
                                break;
                            case 3: // Exiting through lower-right
                                exitX = centerX + HEX_SIZE * 0.5;
                                exitY = centerY + HEX_SIZE * Math.sqrt(3) / 2;
                                break;
                            case 4: // Exiting through lower-left
                                exitX = centerX - HEX_SIZE * 0.5;
                                exitY = centerY + HEX_SIZE * Math.sqrt(3) / 2;
                                break;
                            case 5: // Exiting through left
                                exitX = centerX - HEX_SIZE;
                                exitY = centerY;
                                break;
                            default: // Ray absorbed, exit coordinates same as entry coordinates
                                exitX = entryX;
                                exitY = entryY;
                        }

                        // Draw the line representing the ray segment
                        drawRaySegment(entryX, entryY, exitX, exitY);
                    }



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

    // Method to draw a line representing a ray segment
    private void drawRaySegment(double startX, double startY, double endX, double endY) {
        Line rayLine = new Line(startX, startY, endX, endY);
        rayLine.setStroke(Color.RED); // Set the color of the ray line
        rayLine.setStrokeWidth(2); // Set the width of the ray line
        getChildren().add(rayLine); // Add the ray line to the pane
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

    ////////
    private void drawAtomCircleOfInfluence(double centerX, double centerY) {
        // Define the radius of the circle of influence
        double influenceRadius = HEX_SIZE * 1.5; // Adjust the multiplier as needed for the desired size

        // Create a transparent circle representing the influence of the atom
        Circle atomInfluenceCircle = new Circle(centerX, centerY, influenceRadius);
        atomInfluenceCircle.setFill(Color.TRANSPARENT);
        atomInfluenceCircle.setStroke(Color.GRAY);
        atomInfluenceCircle.setStrokeWidth(2); // Adjust the stroke width as needed

        // Add the circle to the pane
        getChildren().add(atomInfluenceCircle);
    }
}
