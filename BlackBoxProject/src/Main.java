import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create buttons for "Play" and "Exit"
        Button playButton = new Button("Play");
        Button exitButton = new Button("Exit");

        // Set the font for the buttons
        Font buttonFont = Font.font("Arial", FontWeight.BOLD, 16);
        playButton.setFont(buttonFont);
        exitButton.setFont(buttonFont);

        // Set the background color for the scene
        Color backgroundColor = Color.rgb(70, 70, 70); // Darker grey
        BackgroundFill backgroundFill = new BackgroundFill(backgroundColor, CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(backgroundFill);

        // Set event handlers for the buttons
        playButton.setOnAction(event -> primaryStage.setScene(createGameScene(primaryStage)));
        exitButton.setOnAction(event -> primaryStage.close());

        // Create a layout pane for the buttons
        StackPane root = new StackPane();
        root.setBackground(background); // Set the background color
        root.setAlignment(Pos.CENTER); // Center align the buttons
        root.setPadding(new Insets(20)); // Add padding to the layout pane
        root.setPrefSize(1300, 800); // Set the preferred size of the scene

        // Set the buttons to fill the available width
        playButton.setMaxWidth(Double.MAX_VALUE);
        exitButton.setMaxWidth(Double.MAX_VALUE);

        // Create a vertical box to hold the buttons
        VBox buttonBox = new VBox(20); // Spacing of 20 pixels
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(playButton, exitButton);

        // Add the button box to the layout pane
        root.getChildren().add(buttonBox);

        // Create a scene with the layout pane as root
        Scene scene = new Scene(root);

        // Set the scene to the stage and show the stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("Main Menu");
        primaryStage.show();
    }





    private Scene createGameScene(Stage primaryStage) {
        Board board = new Board();
        board.generateAtoms(6);

        // Create the game board UI
        HexagonalBoardUI boardUI = new HexagonalBoardUI(board);

        Button backButton = new Button("Back to Menu");
        backButton.setOnAction(event -> primaryStage.setScene(createMainMenuScene(primaryStage))); // Pass primaryStage to createMainMenuScene

        // Create an HBox to center the board horizontally and add padding to the left
        HBox boardContainer = new HBox();
        boardContainer.setAlignment(Pos.CENTER);
        boardContainer.setPadding(new Insets(0, 0, 0, 1300/2)); // Add padding to the left side
        boardContainer.getChildren().add(boardUI);

        // Create a layout pane for the game components
        VBox gamePane = new VBox(10);
        gamePane.setAlignment(Pos.CENTER);
        gamePane.getChildren().addAll(boardContainer, backButton);

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

        playButton.setOnAction(event -> primaryStage.setScene(createGameScene(primaryStage))); // Pass primaryStage to createGameScene
        exitButton.setOnAction(event -> primaryStage.close());

        StackPane root = new StackPane();
        root.setBackground(new Background(new BackgroundFill(Color.rgb(70, 70, 70), CornerRadii.EMPTY, Insets.EMPTY)));

        VBox buttonBox = new VBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(playButton, exitButton);

        root.getChildren().add(buttonBox);

        Scene scene = new Scene(root, 1000, 700);

        return scene;
    }

    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }
}
