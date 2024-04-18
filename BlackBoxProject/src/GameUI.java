import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.PrintStream;

/**
 * Class containing methods to create UI elements specific to the Game scene
 */
public class GameUI{


    public static Scene createGameScene(Stage primaryStage, Game game) {
        // Create the game components
        BoardUI boardUI = game.getBoardUI();

        // Create an HBox to contain the game's board UI
        Region boardContainer = boardUI.createBoardContainer();

        // Create game scene buttons and an HBox to contain them
        Region buttonBox = createButtonBox(primaryStage, game);

        // Create the input field and output area and an HBox to contain them
        Region consoleBox = createConsoleBox(game);

        // Create a layout pane for the game components
        Region gamePane = createGamePane(boardContainer, buttonBox, consoleBox);

        return new Scene(gamePane, 1300, 800);
    }

    //--- Methods to create the game scene components ---

    // Create an HBox to center the board horizontally and add padding to the left

    private static Region createConsoleBox(Game game) {
        HBox consoleBox = new HBox(10); // Adjust spacing between components as needed
        consoleBox.setAlignment(Pos.CENTER);

        // Create the console components
        TextArea outputArea = createOutputArea();
        TextField inputField = createInputField(game, outputArea);

        consoleBox.getChildren().addAll(inputField, outputArea);
        return consoleBox;
    }

    private static TextArea createOutputArea() {
        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setStyle("-fx-font-size: 14;");
        outputArea.setPrefWidth(200); // Set a fixed width for the output area
        outputArea.setPrefHeight(100); // Set a fixed height for the output area
        outputArea.setWrapText(true);

        System.setOut(new PrintStream(new TextAreaOutputStream(outputArea)));   //redirect standard output to the output text area

        return outputArea;
    }

    private static TextField createInputField(Game game, TextArea outputArea) {
        TextField inputField = new TextField();
        inputField.setPromptText("Enter ray input point");
        inputField.setStyle("-fx-font-size: 14;");
        inputField.setPrefWidth(200); // Set a fixed width for the input field


        // Set the action for the input field
        inputField.setOnAction(event -> processInput(inputField.getText(), game, outputArea));

        return inputField;
    }

    private static void processInput(String input, Game game, TextArea outputArea) {
        try {
            int outputCoordinate = game.sendExperimenterRay(input);

            if (outputCoordinate == -1) outputArea.setText("Ray absorbed\n");
            else outputArea.setText("Output point: " + outputCoordinate + "\n");
        }
        catch (IllegalArgumentException e) {
            outputArea.setText("Invalid input point, please enter a valid input point\n");
        } catch (InputPointTestedException e) {
            outputArea.setText(e.getMessage() + "\n");
        }
    }


    private static Region createButtonBox(Stage primaryStage, Game game) {
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(
                CommonUI.createBackButton(primaryStage),
                createEndRoundButton(primaryStage, game)
        );
        return buttonBox;
    }


    private static Button createEndRoundButton(Stage primaryStage, Game game) {
        Button countMatchesButton = new Button("End Round");
        countMatchesButton.setStyle("-fx-font-size: 24; -fx-font-family: Verdana; -fx-background-radius: 30;");
        countMatchesButton.setMinWidth(200);
        countMatchesButton.setMinHeight(50);

        countMatchesButton.setOnAction(event -> endRound(primaryStage, game));

        return countMatchesButton;
    }

    private static void endRound(Stage primaryStage, Game game) {
        //check if player has made 6 guesses
        if(game.getBoardUI().getPlayerMarkerCoordinates().size() != 6) {
            System.out.println("\nPlease place 6 atom markers before ending the round");
            return;
        }

        primaryStage.setScene(RoundEndUI.createEndRoundScene(primaryStage, game));
        primaryStage.setTitle("Game End");
    }

    private static Region createGamePane(Region boardContainer, Region buttonBox, Region consoleBox) {
        VBox gamePane = new VBox(10);
        gamePane.setAlignment(Pos.CENTER);
        gamePane.getChildren().addAll(boardContainer, buttonBox, consoleBox);
        gamePane.setBackground(new Background(new BackgroundFill(Color.rgb(70, 70, 70), CornerRadii.EMPTY, Insets.EMPTY)));
        return gamePane;
    }

    // not used in final version
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


}
