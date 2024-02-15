import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
        playButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Code to start the game goes here
                primaryStage.setScene(createGameScene(primaryStage)); // Pass primaryStage to createGameScene
            }
        });

        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Close the application when "Exit" button is clicked
                primaryStage.close();
            }
        });

        // Create a layout pane for the buttons
        StackPane root = new StackPane();
        root.setBackground(background); // Set the background color
        root.setAlignment(Pos.CENTER); // Center align the buttons
        root.setPadding(new Insets(20)); // Add padding to the layout pane
        root.setPrefSize(1000, 700); // Set the preferred size of the scene

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

    // Method to create the game screen scene
    private Scene createGameScene(Stage primaryStage) {
        // This is a placeholder method to create a simple game screen
        // You should replace this with your actual game screen creation logic
        StackPane gamePane = new StackPane();
        gamePane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        gamePane.setPrefSize(1000, 700);
        Button backButton = new Button("Back to Menu");
        backButton.setOnAction(event -> primaryStage.setScene(createMainMenuScene(primaryStage))); // Pass primaryStage to createMainMenuScene
        gamePane.getChildren().add(backButton);
        return new Scene(gamePane);
    }

    // Method to create the main menu scene
    private Scene createMainMenuScene(Stage primaryStage) {
        // This method creates the main menu scene as before
        Button playButton = new Button("Play");
        Button exitButton = new Button("Exit");
        // Set the font for the buttons and other styling as needed

        playButton.setOnAction(event -> primaryStage.setScene(createGameScene(primaryStage))); // Pass primaryStage to createGameScene
        exitButton.setOnAction(event -> primaryStage.close());

        StackPane root = new StackPane();
        // Set the layout, background color, etc.

        VBox buttonBox = new VBox(20);
        // Set up the layout for the buttons

        root.getChildren().add(buttonBox);

        Scene scene = new Scene(root);
        // Set the scene to the stage and show the stage

        return scene;
    }

    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }
}
