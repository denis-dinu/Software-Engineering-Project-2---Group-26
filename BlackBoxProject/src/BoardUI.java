
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.Text;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class BoardUI extends AnchorPane {
    private static final double HEX_SIZE = 35.0;
    private static final double HEX_WIDTH = Math.sqrt(3) * HEX_SIZE;
    private static final double HEX_HEIGHT = 2 * HEX_SIZE;
    private static final Color HEX_COLOR = Color.LIGHTGRAY;
    private static final double horizontalGap = 1; // Adjust horizontal gap
    private static final double verticalGap = 1; // Adjust vertical gap
    private boolean interactive = true;     // set to false for final display (when user can't press buttons anymore)

    final Board board;

    // Getter method for retrieving the atom coordinates
    public ArrayList<double[]> getAtomCoordinates() {
        return atomCoordinates;
    }

    // Getter method for retrieving the player marker coordinates
    public ArrayList<double[]> getPlayerMarkerCoordinates() {
        return playerMarkerCoordinates;
    }
    private boolean atomsVisible = false; // Track the visibility state of atoms

    /*
        A hashmap to store the text labels showing input points on the edge of the board, with the
        number shown on the label as key and the JavaFX text node itself as the value
     */
    private final HashMap<Integer, Text> numberLabels = new HashMap<>();
    // List to store the coordinates of cells with atoms
    private final ArrayList<double[]> atomCoordinates = new ArrayList<>();
    private final ArrayList<double[]> playerMarkerCoordinates = new ArrayList<>();

    public BoardUI(Board board) {
        this.board = board;
        initializeCellCoordinates(board.getCells());
        initializeNumberLabels(board.getCells());
        initializeAtomCoordinates(board.getCells());
        drawBoard();
    }

    private void initializeCellCoordinates(Cell[][] cells) {
        boolean isOffset = false; // Flag to track alternating offset

        double sceneWidth = computeMinWidth(0);

        for (int i = 0; i < cells.length; i++) {
            int rowLength = cells[i].length;
            double rowWidth = HEX_WIDTH * rowLength + (rowLength - 1) * horizontalGap;
            double rowStartX;

            if (isOffset) rowStartX = (sceneWidth - rowWidth) / 2.0 - HEX_WIDTH / 2; // Offset backward
            else rowStartX = (sceneWidth - rowWidth) / 2.0; // No offset

            for (int j = 0; j < rowLength; j++) {
                if (i % 2 == 0) cells[i][j].setCenterX(rowStartX + j * (HEX_WIDTH + horizontalGap) + HEX_WIDTH / 2.0);
                else cells[i][j].setCenterX(rowStartX + j * (HEX_WIDTH + horizontalGap) + HEX_WIDTH);
                cells[i][j].setCenterY(HEX_HEIGHT * i * 0.75 + HEX_HEIGHT / 2 + verticalGap); // Add vertical gap
            }

            // Toggle offset flag for the next row
            isOffset = !isOffset;
        }

    }

    private void initializeNumberLabels(Cell[][] cells) {
        for(Cell[] cellRow: cells) {
            for(Cell cell: cellRow) {
                //add input number labels on the edge of the board (if the cell is an edge cell)
                addNumberLabels(cell.getCenterX(), cell.getCenterY(), cell);
            }
        }
    }

    private void initializeAtomCoordinates(Cell[][] cells) {

        for(Cell[] cellRow: cells) {
            for(Cell cell: cellRow) {
                // Store the coordinates of cells with atoms
                if (cell.hasAtom()) {
                    atomCoordinates.add(new double[]{cell.getCenterX(), cell.getCenterY()});
                }
            }
        }

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

        for (Cell[] cellRow: cells) {
            for (Cell cell: cellRow) {

                // Draw hexagon
                drawHexagon(cell.getCenterX(), cell.getCenterY());

                ArrayList<RaySegment> raySegments = cell.getRaySegments();
                if (!raySegments.isEmpty() && atomsVisible) {
                    // Draw the ray segments passing through this cell, if any
                    drawRaySegments(cell.getCenterX(), cell.getCenterY(), raySegments);
                }
            }

        }

        for(double[] coordinates : playerMarkerCoordinates)
        {
            drawPlayerMarker(coordinates[0],coordinates[1]);
        }

        // Draw atoms and their circles of influence after drawing all cells if atoms are visible
        if (atomsVisible) {
            for (double[] coordinates : atomCoordinates) {
                Color c;
                if(hasPlayerMarker(coordinates[0], coordinates[1])) {
                    c = Color.GREEN;
                } else {
                    c = Color.RED;
                }
                drawAtom(coordinates[0], coordinates[1], c);
                drawAtomCircleOfInfluence(coordinates[0], coordinates[1], c);
            }
        }

        drawRayMarkers();
        drawNumberLabels();
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
        HexagonButton hexagonButton = new HexagonButton(centerX, centerY, HEX_SIZE);

        hexagonButton.setFill(HEX_COLOR); // Set fill color
        hexagonButton.setStroke(Color.DARKGRAY); // Set outline color
        hexagonButton.setStrokeWidth(2); // Set outline width
        if(interactive) {
            hexagonButton.setOnClickAction(() -> {
                // Perform actions when the hexagon button is clicked
                if (hasPlayerMarker(centerX, centerY)) {
                    removePlayerMarker(centerX, centerY);
                } else {
                    addPlayerMarker(centerX, centerY);
                }
            });
        }

        getChildren().add(hexagonButton);
    }

    private boolean hasPlayerMarker(double centerX, double centerY) {
        for (double[] coordinates : playerMarkerCoordinates) {
            if (coordinates[0] == centerX && coordinates[1] == centerY) {
                return true;
            }
        }
        return false;
    }

    private void addPlayerMarker(double centerX, double centerY) {
        if (playerMarkerCoordinates.size() < 6) { // Check if the number of markers is less than 6
            playerMarkerCoordinates.add(new double[]{centerX, centerY});
            drawPlayerMarker(centerX, centerY);
        } else {
            // Display a message indicating that the maximum limit of markers has been reached
            System.out.println("\nMaximum limit of atom markers reached. Please replace already existing ones by removing them.");
        }
    }

    private void removePlayerMarker(double centerX, double centerY) {
        // Iterate through playerMarkercoordinates list and remove the marker with matching coordinates
        Iterator<double[]> iterator = playerMarkerCoordinates.iterator();
        while (iterator.hasNext()) {
            double[] coordinates = iterator.next();
            if (coordinates[0] == centerX && coordinates[1] == centerY) {
                iterator.remove();
                break;
            }
        }
        // Redraw the board to update the UI
        drawBoard();
    }


    private void drawAtom(double centerX, double centerY, Color color) {
        Circle atomCircle = new Circle(centerX, centerY, HEX_SIZE / 4); // Radius is 1/4 of hexagon size
        atomCircle.setFill(color); // Atom color
        getChildren().add(atomCircle);
    }

    private void drawPlayerMarker(double centerX, double centerY) {
        Circle atomCircle = new Circle(centerX, centerY, HEX_SIZE / 4); // Radius is 1/4 of hexagon size
        atomCircle.setFill(Color.GREY); // Atom color
        if(interactive) {
            atomCircle.setOnMouseClicked(event -> removePlayerMarker(centerX, centerY));
        }
        getChildren().add(atomCircle);
    }


    ////////
    private void drawAtomCircleOfInfluence(double centerX, double centerY, Color color) {
        // Define the radius of the circle of influence
        double influenceRadius = HEX_SIZE * 1.7; // Adjust the multiplier as needed for the desired size

        // Create a transparent circle representing the influence of the atom
        Circle atomInfluenceCircle = new Circle(centerX, centerY, influenceRadius);
        atomInfluenceCircle.setFill(Color.TRANSPARENT);
        atomInfluenceCircle.setStroke(color);
        atomInfluenceCircle.setStrokeWidth(2); // Adjust the stroke width as needed

        // Add the circle to the pane
        getChildren().add(atomInfluenceCircle);
    }

    private void addNumberLabels(double centerX, double centerY, Cell cell) {

        if(cell.getCol() == 0) {
            //left label
            addNewLabel(getLeftX(centerX, cell), getLeftY(centerY), cell.getRow() * 2 + 2);
            if(cell.getRow() <= 4) {
                //upper left label
                addNewLabel(getUpperLeftX(centerX, cell), getUpperLeftY(centerY), cell.getRow()*2+1);
            }
        }

        if(cell.getRow() == 0 && cell.getCol() > 0) {
            //upper left label
            addNewLabel(getUpperLeftX(centerX, cell), getUpperLeftY(centerY), 55 - cell.getCol()*2);
        }
        if((cell.getCol() == 0 && cell.getRow() >= 4) || cell.getRow() == Board.BOARD_SIZE-1) {
            //lower left label
            addNewLabel(getLowerLeftX(centerX), getLowerLeftY(centerY), (cell.getRow()+ cell.getCol())*2+3);

        }
        if(cell.getRow() == Board.BOARD_SIZE-1 || cell.getCol() == 12 - cell.getRow()) {
            //lower right label
            addNewLabel(getLowerRightX(centerX), getLowerRightY(centerY), cell.getCol()*2 + 20);
        }
        if(cell.getCol() == 12 - cell.getRow() || cell.getCol() == 4 + cell.getRow()) {
            //right label
            addNewLabel(getRightX(centerX), getRightY(centerY), 45 - cell.getRow()*2);
        }
        if(cell.getRow() == 0 || cell.getCol() == 4 + cell.getRow()) {
            //upper right label
            addNewLabel(getUpperRightX(centerX), getUpperRightY(centerY), 54 - cell.getCol()*2);
        }

    }

    private void addNewLabel(double x, double y, int number) {
        Text label = new Text(x, y, String.valueOf(number));
        label.setStroke(Color.WHITE);
        this.numberLabels.put(number, label);
    }

    private void drawNumberLabels() {
        getChildren().addAll(numberLabels.values());
    }


    private void drawRayMarkers() {
        ArrayList<RayMarker> rayMarkers = board.getRayMarkers();
        for(RayMarker rm : rayMarkers) {
            int inputPoint = rm.inputPoint();
            int outputPoint = rm.outputPoint();

            //place input point ray marker
            placeRayMarker(inputPoint, rm.color());

            //if the ray is not absorbed or reflected, place ray marker at the output point as well
            if(outputPoint != -1 && outputPoint != inputPoint) {
                placeRayMarker(outputPoint, rm.color());
            }
        }
    }


    //places a ray marker of the specified color at the label with the specified number
    private void placeRayMarker(int number, Color color) {
        Cell cell;
        if(number <= 18 && number % 2 == 0) {   //ray marker to the left

            cell = board.getCells()[number/2-1][0];
            drawMarker(getLeftX(cell.getCenterX(), cell), getLeftY(cell.getCenterY()), color);

        } else if(number >= 11 && number <= 27 && number % 2 == 1) {    //ray marker to the lower left

            cell = board.getCells()[Math.min((number-3)/2, 8)][number/19*(number-19)/2];
            drawMarker(getLowerLeftX(cell.getCenterX()), getLowerLeftY(cell.getCenterY()), color);

        } else if(number >= 20 && number <= 36 && number % 2 == 0) {    //ray marker to the lower right

            cell = board.getCells()[Math.min((44-number)/2, 8)][(number-20)/2];
            drawMarker(getLowerRightX(cell.getCenterX()), getLowerRightY(cell.getCenterY()), color);

        } else if(number >= 29 && number <= 45 && number % 2 == 1) {    //ray marker to the right

            cell = board.getCells()[(45-number)/2][number<=37 ? (number-21)/2 : (53-number)/2];
            drawMarker(getRightX(cell.getCenterX()), getRightY(cell.getCenterY()), color);

        } else if(number >= 38 && number % 2 == 0) {    //ray marker to the upper right

            cell = board.getCells()[Math.max((46-number)/2, 0)][(54-number)/2];
            drawMarker(getUpperRightX(cell.getCenterX()), getUpperRightY(cell.getCenterY()), color);

        } else if(number >= 47) {   //ray marker to the upper left (top edge)

            cell = board.getCells()[0][(55-number)/2];
            drawMarker(getUpperLeftX(cell.getCenterX(), cell), getUpperLeftY(cell.getCenterY()), color);

        } else {    //ray marker to the upper left (left edge)

            cell = board.getCells()[number/2][0];
            drawMarker(getUpperLeftX(cell.getCenterX(), cell), getUpperLeftY(cell.getCenterY()), color);

        }

        //remove the label for this number, as it has been replaced with a ray marker
        numberLabels.remove(number);
    }

    private void drawMarker(double x, double y, Color color) {
        Circle marker = new Circle(x + HEX_SIZE/8, y - HEX_SIZE/8, HEX_SIZE / 4);
        marker.setFill(color); // marker color
        getChildren().add(marker);
    }



    //utility methods for computing layout coordinates of labels/ray markers and making the code more readable
    private double getLeftX(double centerX, Cell cell) {
        return cell.getRow() < 4 ? centerX - 1.25 * HEX_SIZE : centerX - 1.4 * HEX_SIZE;
    }
    private double getLeftY(double centerY) {
        return centerY + HEX_SIZE / 8;
    }
    private double getUpperLeftX(double centerX, Cell cell) {
        return cell.getRow() == 0 && cell.getCol() > 0 ? centerX - 0.75 * HEX_SIZE : centerX - 0.65 * HEX_SIZE;
    }
    private double getUpperLeftY(double centerY) {
        return centerY - 0.85 * HEX_SIZE;
    }
    private double getLowerLeftX(double centerX) {
        return centerX - 0.85 * HEX_SIZE;
    }
    private double getLowerLeftY(double centerY) {
        return centerY + 1.1 * HEX_SIZE;
    }
    private double getLowerRightX(double centerX) {
        return centerX + 0.4 * HEX_SIZE;
    }
    private double getLowerRightY(double centerY) {
        return centerY + 1.1 * HEX_SIZE;
    }
    private double getRightX(double centerX) {
        return centerX + HEX_SIZE;
    }
    private double getRightY(double centerY) {
        return centerY + HEX_SIZE / 8;
    }
    private double getUpperRightX(double centerX) {
        return centerX + 0.45 * HEX_SIZE;
    }
    private double getUpperRightY(double centerY) {
        return centerY - 0.85 * HEX_SIZE;
    }

    @Override
    protected double computeMinWidth(double height) {
        return HEX_WIDTH * 9 + 8 * horizontalGap;
    }

    @Override
    protected double computePrefWidth(double height) {
        return HEX_WIDTH * 9 + 8 * horizontalGap;
    }

    public void setInteractive(boolean interactive) {
        this.interactive = interactive;
    }
}


class HexagonButton extends Polygon {
    private Runnable onClickAction;

    public HexagonButton(double centerX, double centerY, double size) {
        super(
                centerX + size * Math.cos(Math.toRadians(30)), centerY + size * Math.sin(Math.toRadians(30)),
                centerX + size * Math.cos(Math.toRadians(90)), centerY + size * Math.sin(Math.toRadians(90)),
                centerX + size * Math.cos(Math.toRadians(150)), centerY + size * Math.sin(Math.toRadians(150)),
                centerX + size * Math.cos(Math.toRadians(210)), centerY + size * Math.sin(Math.toRadians(210)),
                centerX + size * Math.cos(Math.toRadians(270)), centerY + size * Math.sin(Math.toRadians(270)),
                centerX + size * Math.cos(Math.toRadians(330)), centerY + size * Math.sin(Math.toRadians(330))
        );

        setOnMouseClicked(event -> {
            if (onClickAction != null) {
                onClickAction.run();
            }
        });
    }

    public void setOnClickAction(Runnable onClickAction) {
        this.onClickAction = onClickAction;
    }

}
