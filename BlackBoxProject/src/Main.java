
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
        //start a new game
        Game game = new Game();

        // Create the game components
        // Create an HBox to contain the game's board UI
        Region boardContainer = createBoardContainer(game.getBoardUI());

        // Create game scene buttons and an HBox to contain them
        Region buttonBox = createButtonBox(primaryStage, game.getBoardUI());

        // Create the input field and output area and an HBox to contain them
        Region consoleBox = createConsoleBox(game);


        // Create a layout pane for the game components
        Region gamePane = createGamePane(boardContainer, buttonBox, consoleBox);

        return new Scene(gamePane, 1300, 800);
    }

    //--- Methods to create the game scene components ---

    // Create an HBox to center the board horizontally and add padding to the left
    private Region createBoardContainer(HexagonalBoardUI boardUI) {
        HBox boardContainer = new HBox();
        boardContainer.setAlignment(Pos.CENTER);
        boardContainer.setPadding(new Insets(0, 0, 0, 1300.0 / 2.0)); // Add padding to the left side
        boardContainer.getChildren().add(boardUI);
        return boardContainer;
    }

    private Region createButtonBox(Stage primaryStage, HexagonalBoardUI boardUI) {
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(createBackButton(primaryStage), createToggleAtomsButton(boardUI));
        return buttonBox;
    }

    private Button createBackButton(Stage primaryStage) {
        Button backButton = new Button("Back to Menu");
        backButton.setStyle("-fx-font-size: 24; -fx-font-family: Verdana; -fx-background-radius: 30;"); // Set button font size, family, and round corners
        backButton.setMinWidth(100); // Set the minimum width for the button
        backButton.setMinHeight(50); // Set the minimum height for the button

        // Set the action for the "Back to Menu" button
        backButton.setOnAction(event -> {
            primaryStage.setScene(createMainMenuScene(primaryStage)); // Set the main menu scene directly
            primaryStage.setTitle("Main menu");
        });
        return backButton;
    }

    private static Button createToggleAtomsButton(HexagonalBoardUI boardUI) {
        Button toggleAtomsButton = new Button("Toggle Atoms Visibility");
        toggleAtomsButton.setStyle("-fx-font-size: 24; -fx-font-family: Verdana; -fx-background-radius: 30;"); // Set button font size, family, and round corners
        toggleAtomsButton.setMinWidth(200); // Set the minimum width for the button
        toggleAtomsButton.setMinHeight(50); // Set the minimum height for the button

        // Toggle atom visibility when the button is clicked
        toggleAtomsButton.setOnAction(event -> {
            boolean currentVisibility = boardUI.areAtomsVisible();
            boardUI.setAtomsVisible(!currentVisibility);
        });
        return toggleAtomsButton;
    }

    private Region createConsoleBox(Game game) {
        HBox consoleBox = new HBox(10); // Adjust spacing between components as needed
        consoleBox.setAlignment(Pos.CENTER);

        // Create the console components
        TextArea outputArea = createOutputArea();
        TextField inputField = createInputField(game, outputArea);

        consoleBox.getChildren().addAll(inputField, outputArea);
        return consoleBox;
    }

    private TextArea createOutputArea() {
        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setStyle("-fx-font-size: 14;");
        outputArea.setPrefWidth(200); // Set a fixed width for the output area
        outputArea.setPrefHeight(100); // Set a fixed height for the output area
        outputArea.setWrapText(true);
        return outputArea;
    }

    private TextField createInputField(Game game, TextArea outputArea) {
        TextField inputField = new TextField();
        inputField.setPromptText("Enter ray input point");
        inputField.setStyle("-fx-font-size: 14;");
        inputField.setPrefWidth(200); // Set a fixed width for the input field


        // Set the action for the input field
        inputField.setOnAction(event -> {
            try {
                int outputCoordinate = game.sendExperimenterRay(inputField.getText());

                if (outputCoordinate == -1) outputArea.setText("Ray absorbed");
                else outputArea.setText("Output point: " + outputCoordinate);
            }
            catch (IllegalArgumentException e) {
                outputArea.setText("Invalid input point, please enter a valid input point");
            } catch (InputPointTestedException e) {
                outputArea.setText(e.getMessage());
            }
        });

        return inputField;
    }

    private Region createGamePane(Region boardContainer, Region buttonBox, Region consoleBox) {
        VBox gamePane = new VBox(10);
        gamePane.setAlignment(Pos.CENTER);
        gamePane.getChildren().addAll(boardContainer, buttonBox, consoleBox);
        gamePane.setBackground(new Background(new BackgroundFill(Color.rgb(70, 70, 70), CornerRadii.EMPTY, Insets.EMPTY)));
        return gamePane;
    }



    // Method to create the main menu scene
    private Scene createMainMenuScene(Stage primaryStage) {
        // This method creates the main menu scene as before

        Button playButton = createPlayButton(primaryStage);
        Button exitButton = createExitButton(primaryStage);
        Region root = createMainMenuRoot(playButton, exitButton);

        return new Scene(root, 1300, 800);
    }

    private Button createPlayButton(Stage primaryStage) {
        Button playButton = new Button("Play");
        playButton.setStyle("-fx-font-size: 24; -fx-font-family: Verdana; -fx-background-radius: 30;"); // Set button font size, family, and round corners
        playButton.setOnAction(event -> {
            primaryStage.setScene(createGameScene(primaryStage)); // Pass primaryStage to createGameScene
            primaryStage.setTitle("BlackBox");
        });
        return playButton;
    }

    private Button createExitButton(Stage primaryStage) {
        Button exitButton = new Button("Exit");
        exitButton.setStyle("-fx-font-size: 24; -fx-font-family: Verdana; -fx-background-radius: 30;"); // Set button font size, family, and round corners
        exitButton.setOnAction(event -> primaryStage.close());
        return exitButton;
    }

    private Region createMainMenuRoot(Button playButton, Button exitButton) {
        VBox buttonBox = new VBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(playButton, exitButton);
        buttonBox.setBackground(new Background(new BackgroundFill(Color.rgb(70, 70, 70), CornerRadii.EMPTY, Insets.EMPTY)));

        return buttonBox;
    }

    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }
}