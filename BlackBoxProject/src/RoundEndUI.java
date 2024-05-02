import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Class containing methods to create UI elements specific to the Round End scene
 */
public class RoundEndUI {

    private static final Label NAME_WARNING = CommonUI.createWarningLabel("Please enter your name to save your score");
    private static final Label SCORE_WARNING = CommonUI.createWarningLabel("File error; cannot save score on the leaderboard");

    public static Scene createEndRoundScene(Stage primaryStage, Game game) {
        if(primaryStage == null || game == null) {
            throw new IllegalArgumentException("Invalid argument to RoundEndUI.createEndRoundScene");
        }

        HBox results = new HBox(40);

        VBox endScreenBox = createEndScreenBox(primaryStage, game);

        Region board = createFinalBoardView(game.getBoardUI());

        results.getChildren().addAll(board, endScreenBox);
        results.setBackground(new Background(new BackgroundFill(Color.rgb(70, 70, 70), CornerRadii.EMPTY, Insets.EMPTY)));
        results.setAlignment(Pos.CENTER);

        return new Scene(results, 1300, 800);
    }

    private static VBox createEndScreenBox(Stage primaryStage, Game game) {
        int score = game.countScore();
        int matchCount = game.countMatches();

        VBox endScreenBox = new VBox(20);
        endScreenBox.setAlignment(Pos.CENTER);

        Label nameLabel = CommonUI.createLabel("Enter your name:");
        TextField nameField = createNameField();

        Label scoreLabel = CommonUI.createLabel("Your penalty score: " + score);
        Label matchCountLabel = CommonUI.createLabel("Matches: " + matchCount);

        Button saveScoreButton = createSaveScoreButton(primaryStage, nameField, score);
        Button backToMenuButton = CommonUI.createBackButton(primaryStage);

        NAME_WARNING.setVisible(false);
        SCORE_WARNING.setVisible(false);

        endScreenBox.getChildren().addAll(nameLabel, nameField, scoreLabel, matchCountLabel,
                saveScoreButton, backToMenuButton, NAME_WARNING, SCORE_WARNING);
        return endScreenBox;
    }

    private static TextField createNameField() {
        TextField nameField = new TextField();
        nameField.setPromptText("Your Name");
        nameField.setStyle("-fx-font-size: 14;");
        nameField.setMaxWidth(200);
        return nameField;
    }

    private static Button createSaveScoreButton(Stage primaryStage, TextField nameField, int score) {
        Button saveScoreButton = new Button("Save Score");
        saveScoreButton.setStyle("-fx-font-size: 24; -fx-font-family: Verdana; -fx-background-radius: 30;");
        saveScoreButton.setOnAction(event -> {

            if(!isNameFieldEmpty(nameField)) {
                int status = Leaderboard.saveScore(nameField.getText(), score, "leaderboard.txt");
                if (status == 0) {
                    primaryStage.setScene(LeaderboardUI.createLeaderboardScene(primaryStage));
                    primaryStage.setTitle("Leaderboard");
                } else {
                    SCORE_WARNING.setVisible(true);
                }
            }
        });
        return saveScoreButton;
    }

    // returns whether the name field is empty and triggers a name warning if it is
    private static boolean isNameFieldEmpty(TextField nameField) {
        String name = nameField.getText();
        if (name.isEmpty()) {
            NAME_WARNING.setVisible(true);
            return true;
        } else {
            NAME_WARNING.setVisible(false);
            return false;
        }
    }

    private static Region createFinalBoardView(BoardUI boardUI) {
        boardUI.setAtomsVisible(true);
        boardUI.setInteractive(false);
        boardUI.drawBoard();      // redraw the board to reflect the change in variable interactive
        return boardUI.createBoardContainer();
    }

}
