import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Class containing methods to create UI elements specific to the leaderboard
 */
public class LeaderboardUI {

    public static Scene createLeaderboardScene(Stage primaryStage) {
        VBox leaderboardBox = new VBox(20);
        leaderboardBox.setAlignment(Pos.CENTER);

        leaderboardBox.getChildren().add(
                CommonUI.createUnderlineLabel("LEADERBOARD"));

        fillLeaderboard(leaderboardBox);

        Button backToMenuButton = CommonUI.createBackButton(primaryStage);

        leaderboardBox.getChildren().add(backToMenuButton);
        leaderboardBox.setBackground(new Background(new BackgroundFill(Color.rgb(70, 70, 70), CornerRadii.EMPTY, Insets.EMPTY)));
        return new Scene(leaderboardBox, 1300, 800);
    }

    private static void fillLeaderboard(VBox leaderboardBox) {
        List<String> leaderboard = new ArrayList<>();
        int status = Leaderboard.loadLeaderboard(leaderboard);
        if(status == 1) {
            leaderboardBox.getChildren().add(
                    CommonUI.createColoredLabel("Leaderboard formatting error: please either fix leaderboard.txt format or erase it and try again", Color.RED));
        } else if(status == 2) {
            leaderboardBox.getChildren().add(
                    CommonUI.createColoredLabel("File error; leaderboard unavailable", Color.RED));
        } else {
            setLeaderboardEntries(leaderboardBox, leaderboard);
        }
    }

    private static void setLeaderboardEntries(VBox leaderboardBox, List<String> leaderboard) {
        if(leaderboard.isEmpty()) {
            leaderboardBox.getChildren().add(
                    CommonUI.createLabel("Leaderboard is currently empty..."));
        } else {
            for(int i=0; i< leaderboard.size(); i++) {
                Color c = getPlacementColor(i);

                leaderboardBox.getChildren().add(
                        CommonUI.createColoredLabel(leaderboard.get(i), c));
            }
        }
    }

    private static Color getPlacementColor(int i) {
        if(i == 0) {
            return Color.GOLD;
        } else if(i == 1) {
            return Color.SILVER;
        } else if(i == 2) {
            return Color.DARKGOLDENROD;
        } else {
            return Color.WHITE;
        }
    }

}
