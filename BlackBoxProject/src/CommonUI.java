import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Class containing methods to create UI elements common to the Main Menu, Game scene, Round End scene or the leaderboard
 */
public class CommonUI {

    static Button createBackButton(Stage primaryStage) {
        Button backButton = new Button("Back to Menu");
        backButton.setStyle("-fx-font-size: 24; -fx-font-family: Verdana; -fx-background-radius: 30;");
        backButton.setMinWidth(100);
        backButton.setMinHeight(50);

        backButton.setOnAction(event -> {
            primaryStage.setScene(Main.createMainMenuScene(primaryStage));
            primaryStage.setTitle("Main menu");
        });
        return backButton;
    }

    private static Label createLabel(String txt, Color color, boolean initiallyVisible, boolean underline) {
        Label label = new Label(txt);
        label.setStyle("-fx-font-size: 20; -fx-font-family: Verdana" + (underline ? "; -fx-underline: true" : ""));
        label.setTextFill(color);
        label.setVisible(initiallyVisible);
        return label;
    }

    static Label createLabel(String txt) {
        return createLabel(txt, Color.WHITE, true, false);
    }

    static Label createColoredLabel(String txt, Color color) {
        return createLabel(txt, color, true, false);
    }

    static Label createWarningLabel(String txt) {
        return createLabel(txt, Color.RED, false, false);
    }

    static Label createUnderlineLabel(String txt) {
        return createLabel(txt, Color.WHITE, true, true);
    }

}
