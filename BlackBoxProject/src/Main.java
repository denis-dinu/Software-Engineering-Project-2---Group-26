import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setScene(createMainMenuScene(primaryStage));
        primaryStage.setTitle("Main Menu");
        primaryStage.setWidth(1300);
        primaryStage.setHeight(800);
        primaryStage.show();
    }

    private Scene createGameScene(Stage primaryStage, Game game) {
        // Create the game components
        BoardUI boardUI = game.getBoardUI();

        // Create an HBox to contain the game's board UI
        Region boardContainer = createBoardContainer(boardUI);

        // Create game scene buttons and an HBox to contain them
        Region buttonBox = createButtonBox(primaryStage, boardUI, game);

        // Create the input field and output area and an HBox to contain them
        Region consoleBox = createConsoleBox(game);

        // Create a layout pane for the game components
        Region gamePane = createGamePane(boardContainer, buttonBox, consoleBox);



        return new Scene(gamePane, 1300, 800);
    }

    //--- Methods to create the game scene components ---

    // Create an HBox to center the board horizontally and add padding to the left

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

    private Region createBoardContainer(BoardUI boardUI) {
        HBox boardContainer = new HBox();
        boardContainer.setAlignment(Pos.CENTER);
        boardContainer.setPadding(new Insets(0, 0, 0, 1300.0 / 2.0)); // Add padding to the left side
        boardContainer.getChildren().add(boardUI);
        return boardContainer;
    }

    private Region createButtonBox(Stage primaryStage, BoardUI boardUI, Game game) {
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(
                createBackButton(primaryStage),
                createToggleAtomsButton(boardUI),
                createCountMatchesButton(primaryStage, game)
        );
        return buttonBox;
    }



    private Button createCountMatchesButton(Stage primaryStage, Game game) {
        Button countMatchesButton = new Button("Count Matches");
        countMatchesButton.setStyle("-fx-font-size: 24; -fx-font-family: Verdana; -fx-background-radius: 30;");
        countMatchesButton.setMinWidth(200);
        countMatchesButton.setMinHeight(50);

        countMatchesButton.setOnAction(event -> {
            int matchCount = game.countMatches();
            primaryStage.setScene(createGameEndScene(primaryStage, matchCount));
            primaryStage.setTitle("Game End");
        });

        return countMatchesButton;
    }

    private Region createGamePane(Region boardContainer, Region buttonBox, Region consoleBox) {
        VBox gamePane = new VBox(10);
        gamePane.setAlignment(Pos.CENTER);
        gamePane.getChildren().addAll(boardContainer, buttonBox, consoleBox);
        gamePane.setBackground(new Background(new BackgroundFill(Color.rgb(70, 70, 70), CornerRadii.EMPTY, Insets.EMPTY)));
        return gamePane;
    }


    // Method to create the game end scene
    private Scene createGameEndScene(Stage primaryStage, int matchCount) {
        VBox endScreenBox = new VBox(20);
        endScreenBox.setAlignment(Pos.CENTER);
        Label matchCountLabel = new Label("Number of Matches: " + matchCount);
        matchCountLabel.setStyle("-fx-font-size: 24; -fx-font-family: Verdana; -fx-text-fill: white;");
        Button backToMenuButton = new Button("Back to Menu");
        backToMenuButton.setStyle("-fx-font-size: 24; -fx-font-family: Verdana; -fx-background-radius: 30;");
        backToMenuButton.setOnAction(event -> {
            primaryStage.setScene(createMainMenuScene(primaryStage));
            primaryStage.setTitle("Main Menu");
        });
        endScreenBox.getChildren().addAll(matchCountLabel, backToMenuButton);
        endScreenBox.setBackground(new Background(new BackgroundFill(Color.rgb(70, 70, 70), CornerRadii.EMPTY, Insets.EMPTY)));
        return new Scene(endScreenBox, 1300, 800);
    }




    private Button createBackButton(Stage primaryStage) {
        Button backButton = new Button("Back to Menu");
        backButton.setStyle("-fx-font-size: 24; -fx-font-family: Verdana; -fx-background-radius: 30;");
        backButton.setMinWidth(100);
        backButton.setMinHeight(50);

        backButton.setOnAction(event -> {
            primaryStage.setScene(createMainMenuScene(primaryStage));
            primaryStage.setTitle("Main menu");
        });
        return backButton;
    }

    private static Button createToggleAtomsButton(BoardUI boardUI) {
        Button toggleAtomsButton = new Button("Toggle Atoms Visibility");
        toggleAtomsButton.setStyle("-fx-font-size: 24; -fx-font-family: Verdana; -fx-background-radius: 30;");
        toggleAtomsButton.setMinWidth(200);
        toggleAtomsButton.setMinHeight(50);

        // Toggle atom visibility when the button is clicked
        toggleAtomsButton.setOnAction(event -> {
            boolean currentVisibility = boardUI.areAtomsVisible();
            boardUI.setAtomsVisible(!currentVisibility);
        });
        return toggleAtomsButton;
    }

    // Method to create the main menu scene
    private Scene createMainMenuScene(Stage primaryStage) {
        Button playButton = createPlayButton(primaryStage);
        Button exitButton = createExitButton(primaryStage);
        Region root = createMainMenuRoot(playButton, exitButton);

        return new Scene(root, 1300, 800);
    }

    private Button createPlayButton(Stage primaryStage) {
        Button playButton = new Button("Play Game");
        playButton.setStyle("-fx-font-size: 24; -fx-font-family: Verdana; -fx-background-radius: 30;");
        playButton.setMinWidth(200);
        playButton.setMinHeight(50);

        playButton.setOnAction(event -> {
            Game game = new Game();
            primaryStage.setScene(createGameScene(primaryStage, game));
            primaryStage.setTitle("Game");
        });

        return playButton;
    }

    private Button createExitButton(Stage primaryStage) {
        Button exitButton = new Button("Exit");
        exitButton.setStyle("-fx-font-size: 24; -fx-font-family: Verdana; -fx-background-radius: 30;");
        exitButton.setMinWidth(200);
        exitButton.setMinHeight(50);

        exitButton.setOnAction(event -> primaryStage.close());

        return exitButton;
    }

    private Region createMainMenuRoot(Button playButton, Button exitButton) {
        VBox root = new VBox(100);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(playButton, exitButton);
        root.setBackground(new Background(new BackgroundFill(Color.rgb(70, 70, 70), CornerRadii.EMPTY, Insets.EMPTY)));
        return root;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
