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

import java.io.*;
import java.util.ArrayList;
import java.util.List;


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

        System.setOut(new PrintStream(new TextAreaOutputStream(outputArea)));   //redirect standard output to the output text area

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

                if (outputCoordinate == -1) outputArea.setText("Ray absorbed\n");
                else outputArea.setText("Output point: " + outputCoordinate + "\n");
            }
            catch (IllegalArgumentException e) {
                outputArea.setText("Invalid input point, please enter a valid input point\n");
            } catch (InputPointTestedException e) {
                outputArea.setText(e.getMessage() + "\n");
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
                createEndRoundButton(primaryStage, game)
        );
        return buttonBox;
    }



    private Button createEndRoundButton(Stage primaryStage, Game game) {
        Button countMatchesButton = new Button("End Round");
        countMatchesButton.setStyle("-fx-font-size: 24; -fx-font-family: Verdana; -fx-background-radius: 30;");
        countMatchesButton.setMinWidth(200);
        countMatchesButton.setMinHeight(50);

        countMatchesButton.setOnAction(event -> {
            //check if player has made 6 guesses
            if(game.getBoardUI().getPlayerMarkerCoordinates().size() != 6) {
                System.out.println("\nPlease place 6 atom markers before ending the round");
                return;
            }

            int matchCount = game.countMatches();
            int score = game.countScore();
            primaryStage.setScene(createEndRoundScene(primaryStage,game,matchCount, score));
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


    private Scene createEndRoundScene(Stage primaryStage, Game game, int matchCount, int score) {
        VBox endScreenBox = new VBox(20);
        endScreenBox.setAlignment(Pos.CENTER);

        Label nameLabel = new Label("Enter your name:");
        nameLabel.setStyle("-fx-font-size: 20; -fx-font-family: Verdana; -fx-text-fill: white;");

        HBox nameInputBox = new HBox(); // Container for the name input field
        nameInputBox.setAlignment(Pos.CENTER);
        nameInputBox.setSpacing(10);

        TextField nameField = new TextField();
        nameField.setPromptText("Your Name");
        nameField.setStyle("-fx-font-size: 14;");
        nameField.setPrefWidth(200); // Set the preferred width of the name field

        nameInputBox.getChildren().add(nameField);

        Label scoreLabel = new Label("Your penalty score: " + score);
        scoreLabel.setStyle("-fx-font-size: 24; -fx-font-family: Verdana; -fx-text-fill: white;");

        // Create a label to display the amount of matches
        Label matchCountLabel = new Label("Matches: " + matchCount);
        matchCountLabel.setStyle("-fx-font-size: 20; -fx-font-family: Verdana; -fx-text-fill: white;");

        Button saveScoreButton = new Button("Save Score");
        saveScoreButton.setStyle("-fx-font-size: 24; -fx-font-family: Verdana; -fx-background-radius: 30;");
        saveScoreButton.setOnAction(event -> {
            String name = nameField.getText();
            saveScore(name, score);
            primaryStage.setScene(createLeaderboardScene(primaryStage));
            primaryStage.setTitle("Leaderboard");
        });

        Button backToMenuButton = new Button("Back to Menu");
        backToMenuButton.setStyle("-fx-font-size: 24; -fx-font-family: Verdana; -fx-background-radius: 30;");
        backToMenuButton.setOnAction(event -> {
            primaryStage.setScene(createMainMenuScene(primaryStage));
            primaryStage.setTitle("Main Menu");
        });

        endScreenBox.getChildren().addAll(nameLabel, nameInputBox, scoreLabel, matchCountLabel, saveScoreButton, backToMenuButton);
        endScreenBox.setBackground(new Background(new BackgroundFill(Color.rgb(70, 70, 70), CornerRadii.EMPTY, Insets.EMPTY)));
        return new Scene(endScreenBox, 1300, 800);
    }




    private void saveScore(String name, int score) {
        try (FileWriter writer = new FileWriter("leaderboard.txt", true)) {
            writer.write(name + ":" + score + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private Scene createLeaderboardScene(Stage primaryStage) {
        VBox leaderboardBox = new VBox(20);
        leaderboardBox.setAlignment(Pos.CENTER);

        List<String> leaderboard = loadLeaderboard();
        for (String entry : leaderboard) {
            Label entryLabel = new Label(entry);
            entryLabel.setStyle("-fx-font-size: 20; -fx-font-family: Verdana; -fx-text-fill: white;");
            leaderboardBox.getChildren().add(entryLabel);
        }

        Button backToMenuButton = new Button("Back to Menu");
        backToMenuButton.setStyle("-fx-font-size: 24; -fx-font-family: Verdana; -fx-background-radius: 30;");
        backToMenuButton.setOnAction(event -> {
            primaryStage.setScene(createMainMenuScene(primaryStage));
            primaryStage.setTitle("Main Menu");
        });

        leaderboardBox.getChildren().add(backToMenuButton);
        leaderboardBox.setBackground(new Background(new BackgroundFill(Color.rgb(70, 70, 70), CornerRadii.EMPTY, Insets.EMPTY)));
        return new Scene(leaderboardBox, 1300, 800);
    }

    private List<String> loadLeaderboard() {
        List<String> leaderboard = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("leaderboard.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                leaderboard.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        leaderboard.sort((entry1, entry2) -> {
            try {
                int score1 = Integer.parseInt(entry1.split(":")[1].trim());
                int score2 = Integer.parseInt(entry2.split(":")[1].trim());
                return Integer.compare(score1, score2);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                e.printStackTrace(); // Handle parsing errors
                return 0; // Return 0 if there's an error to keep the original order
            }
        });

        return leaderboard;
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
        Button leaderboardButton = createLeaderboardButton(primaryStage);
        Button exitButton = createExitButton(primaryStage);
        Region root = createMainMenuRoot(playButton, leaderboardButton, exitButton);

        return new Scene(root, 1300, 800);
    }

    private Button createLeaderboardButton(Stage primaryStage) {
        Button leaderboardButton = new Button("View Leaderboard");
        leaderboardButton.setStyle("-fx-font-size: 24; -fx-font-family: Verdana; -fx-background-radius: 30;");
        leaderboardButton.setMinWidth(200);

        leaderboardButton.setOnAction(event -> {
            primaryStage.setScene(createLeaderboardScene(primaryStage));
            primaryStage.setTitle("Leaderboard");
        });

        return leaderboardButton;
    }


    private Button createPlayButton(Stage primaryStage) {
        Button playButton = new Button("Play Game");
        playButton.setStyle("-fx-font-size: 24; -fx-font-family: Verdana; -fx-background-radius: 30;");
        playButton.setMinWidth(200);

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

        exitButton.setOnAction(event -> primaryStage.close());

        return exitButton;
    }

    private Region createMainMenuRoot(Button playButton, Button leaderboardButton, Button exitButton) {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(playButton, leaderboardButton, exitButton);
        root.setBackground(new Background(new BackgroundFill(Color.rgb(70, 70, 70), CornerRadii.EMPTY, Insets.EMPTY)));
        return root;
    }


    public static void main(String[] args) {
        launch(args);
    }
}

class TextAreaOutputStream extends OutputStream {

    private final TextArea out;
    public TextAreaOutputStream(TextArea out) {
        this.out = out;
    }

    @Override
    public void write(int b) {
        out.appendText(String.valueOf((char)b));
    }
}