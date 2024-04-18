
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Class containing methods to create UI elements specific to the Round End scene
 */
public class RoundEndUI {

    public static Scene createEndRoundScene(Stage primaryStage, Game game) {

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

        Label nameWarning = CommonUI.createWarningLabel("Please enter your name to save your score");
        Label scoreWarning = CommonUI.createWarningLabel("File error; cannot save score on the leaderboard");

        Button saveScoreButton = new Button("Save Score");
        saveScoreButton.setStyle("-fx-font-size: 24; -fx-font-family: Verdana; -fx-background-radius: 30;");
        saveScoreButton.setOnAction(event -> {

            if(!isNameFieldEmpty(nameField, nameWarning)) {
                int status = Leaderboard.saveScore(nameField.getText(), score);
                if (status == 0) {
                    primaryStage.setScene(LeaderboardUI.createLeaderboardScene(primaryStage));
                    primaryStage.setTitle("Leaderboard");
                } else {
                    scoreWarning.setVisible(true);
                }
            }
        });


        Button backToMenuButton = CommonUI.createBackButton(primaryStage);

        endScreenBox.getChildren().addAll(nameLabel, nameField, scoreLabel, matchCountLabel, saveScoreButton, backToMenuButton, nameWarning, scoreWarning);
        return endScreenBox;
    }

    private static TextField createNameField() {
        TextField nameField = new TextField();
        nameField.setPromptText("Your Name");
        nameField.setStyle("-fx-font-size: 14;");
        nameField.setMaxWidth(200);
        return nameField;
    }

    private static Region createFinalBoardView(BoardUI boardUI) {
        boardUI.setAtomsVisible(true);
        boardUI.setInteractive(false);
        boardUI.drawBoard();      // redraw the board to reflect the change in variable interactive
        return boardUI.createBoardContainer();
    }

    // returns whether the name field is empty and triggers a name warning if it is
    private static boolean isNameFieldEmpty(TextField nameField, Label nameWarning) {
        String name = nameField.getText();
        if (name.isEmpty()) {
            nameWarning.setVisible(true);
            return true;
        } else {
            nameWarning.setVisible(false);
            return false;
        }
    }
}
