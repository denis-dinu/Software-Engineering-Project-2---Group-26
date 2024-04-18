import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import java.io.*;


/**
 * Class containing methods to create UI elements specific to the Main Menu and to launch the game
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setScene(createMainMenuScene(primaryStage));
        primaryStage.setTitle("Main Menu");
        primaryStage.setWidth(1300);
        primaryStage.setHeight(800);
        primaryStage.show();
    }


    // Method to create the main menu scene
    public static Scene createMainMenuScene(Stage primaryStage) {
        Button playButton = createPlayButton(primaryStage);
        Button leaderboardButton = createLeaderboardButton(primaryStage);
        Button exitButton = createExitButton(primaryStage);
        Region root = createMainMenuRoot(playButton, leaderboardButton, exitButton);

        return new Scene(root, 1300, 800);
    }

    private static Button createLeaderboardButton(Stage primaryStage) {
        Button leaderboardButton = new Button("View Leaderboard");
        leaderboardButton.setStyle("-fx-font-size: 24; -fx-font-family: Verdana; -fx-background-radius: 30;");
        leaderboardButton.setMinWidth(200);

        leaderboardButton.setOnAction(event -> {
            primaryStage.setScene(LeaderboardUI.createLeaderboardScene(primaryStage));
            primaryStage.setTitle("Leaderboard");
        });

        return leaderboardButton;
    }


    private static Button createPlayButton(Stage primaryStage) {
        Button playButton = new Button("Play Game");
        playButton.setStyle("-fx-font-size: 24; -fx-font-family: Verdana; -fx-background-radius: 30;");
        playButton.setMinWidth(200);

        playButton.setOnAction(event -> {
            Game game = new Game();
            primaryStage.setScene(GameUI.createGameScene(primaryStage, game));
            primaryStage.setTitle("Game");
        });

        return playButton;
    }

    private static Button createExitButton(Stage primaryStage) {
        Button exitButton = new Button("Exit");
        exitButton.setStyle("-fx-font-size: 24; -fx-font-family: Verdana; -fx-background-radius: 30;");
        exitButton.setMinWidth(200);

        exitButton.setOnAction(event -> primaryStage.close());

        return exitButton;
    }

    private static Region createMainMenuRoot(Button playButton, Button leaderboardButton, Button exitButton) {
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