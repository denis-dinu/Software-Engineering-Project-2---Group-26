import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import javafx.event.Event;
import javafx.scene.input.*;

import java.util.ArrayList;
import java.util.Arrays;

class PlayerMarkerTest {
    BoardUI boardUI;
    ArrayList<HexagonButton> hexagonButtons;
    ArrayList<double[]> playerMarkers;

    @BeforeEach
    void setBoardUI() {
        boardUI = new BoardUI(new Board());
        hexagonButtons = boardUI.getHexagonButtons();
        playerMarkers = boardUI.getPlayerMarkerCoordinates();
    }

    @Test
    void testPlaceMarker() {
        HexagonButton cell = hexagonButtons.get(5);     // get a target cell
        double[] cellCoordinates = new double[] {cell.getCenterX(), cell.getCenterY()};

        // simulate a user clicking the target cell
        Event.fireEvent(cell, new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                true, true, true, true, true, true, null));

        // check if player marker is set
        // cannot use ArrayList.contains because double[] does not override equals
        boolean found = false;
        for(double[]marker : playerMarkers) {
            if(Arrays.equals(cellCoordinates, marker)) {
                found = true;
                break;
            }
        }

        assertTrue(found);
    }

    @Test
    void testNumberOfMarkers() {
        // at any point, the board shouldn't contain more than 6 player markers
        for(int i = 0; i < 10; i++) {
            Event.fireEvent(hexagonButtons.get(i), new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                    true, true, true, true, true, true, null));
        }

        assertEquals(6, playerMarkers.size());
    }

    @Test
    void testRemoveMarker() {
        HexagonButton cell = hexagonButtons.get(18);     // get a target cell
        double[] cellCoordinates = new double[] {cell.getCenterX(), cell.getCenterY()};

        // simulate a user clicking the target cell twice
        for(int i = 0; i < 2; i++) {
            Event.fireEvent(cell, new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                    true, true, true, true, true, true, null));
        }

        boolean found = false;
        for(double[]marker : playerMarkers) {
            if(Arrays.equals(cellCoordinates, marker)) {
                found = true;
                break;
            }
        }

        assertFalse(found);
    }

}

