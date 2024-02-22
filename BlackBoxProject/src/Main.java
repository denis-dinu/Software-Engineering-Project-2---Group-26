import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setScene(createMainMenuScene(primaryStage));
        primaryStage.setTitle("Main Menu");
        primaryStage.setWidth(1300);
        primaryStage.setHeight(800);
        primaryStage.show();
    }

    private Scene createGameScene(Stage primaryStage) {
        Board board = new Board();
        board.generateAtoms(6);

        // Create the game board UI
        HexagonalBoardUI boardUI = new HexagonalBoardUI(board);

        Button backButton = new Button("Back to Menu");
        backButton.setStyle("-fx-font-size: 24; -fx-font-family: Verdana; -fx-background-radius: 30;"); // Set button font size, family, and round corners

        Button toggleAtomsButton = new Button("Toggle Atoms Visibility");
        toggleAtomsButton.setStyle("-fx-font-size: 24; -fx-font-family: Verdana; -fx-background-radius: 30;"); // Set button font size, family, and round corners

        // Toggle atom visibility when the button is clicked
        toggleAtomsButton.setOnAction(event -> {
            boolean currentVisibility = boardUI.areAtomsVisible();
            boardUI.setAtomsVisible(!currentVisibility);
        });

        // Set the action for the "Back to Menu" button
        backButton.setOnAction(event -> {
            primaryStage.setScene(createMainMenuScene(primaryStage)); // Set the main menu scene directly
        });

        // Create an HBox to contain both buttons
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(backButton, toggleAtomsButton);

        // Create an HBox to center the board horizontally and add padding to the left
        HBox boardContainer = new HBox();
        boardContainer.setAlignment(Pos.CENTER);
        boardContainer.setPadding(new Insets(0, 0, 0, 1300 / 2)); // Add padding to the left side
        boardContainer.getChildren().add(boardUI);

        // Create a layout pane for the game components
        VBox gamePane = new VBox(10);
        gamePane.setAlignment(Pos.CENTER);
        gamePane.getChildren().addAll(boardContainer, buttonBox);

        // Create a layout pane to center the game components horizontally
        StackPane root = new StackPane();
        root.setBackground(new Background(new BackgroundFill(Color.rgb(70, 70, 70), CornerRadii.EMPTY, Insets.EMPTY)));
        root.getChildren().add(gamePane);
        return new Scene(root, 1300, 800);
    }




    // Method to create the main menu scene
    private Scene createMainMenuScene(Stage primaryStage) {
        // This method creates the main menu scene as before
        Button playButton = new Button("Play");
        Button exitButton = new Button("Exit");

        playButton.setStyle("-fx-font-size: 24; -fx-font-family: Verdana; -fx-background-radius: 30;"); // Set button font size, family, and round corners
        exitButton.setStyle("-fx-font-size: 24; -fx-font-family: Verdana; -fx-background-radius: 30;"); // Set button font size, family, and round corners

        playButton.setOnAction(event -> primaryStage.setScene(createGameScene(primaryStage))); // Pass primaryStage to createGameScene
        exitButton.setOnAction(event -> primaryStage.close());

        StackPane root = new StackPane();
        root.setBackground(new Background(new BackgroundFill(Color.rgb(70, 70, 70), CornerRadii.EMPTY, Insets.EMPTY)));

        VBox buttonBox = new VBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(playButton, exitButton);

        root.getChildren().add(buttonBox);
        return new Scene(root, 1300, 800);
    }

    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }
}
