import javafx.scene.paint.Color;

/**
 * A ray marker is a triple made up of an input point, output point and a color.
 *
 * @param inputPoint the label number of the point where the ray marked by this marker enters the board
 * @param outputPoint the label number of the point where the ray marked by this marker exits the board
 * @param color the color of the ray marker (black for absorbed rays, white for reflected rays and some
 *             other random color for other rays)
 *
 */
public record RayMarker (int inputPoint, int outputPoint, Color color) {
}
