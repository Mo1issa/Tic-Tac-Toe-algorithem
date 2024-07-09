package application;

import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			// Load the GIF file
			String gifPath = "file:C:/Users/HP/Desktop/finished%20courses/java%20full%20meterial/java2/TicTacToeM/src/oxox.gif";
			Image backgroundImage = new Image(gifPath);

			// Create a background with the loaded image
			BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false,
					true, true);
			BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT,
					BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, backgroundSize);
			Background backgroundObject = new Background(background);

			// Set up the root pane with the background
			BorderPane root = new BorderPane();
			root.setBackground(backgroundObject);

			VBox vbox = new VBox(25);

			Button senario1 = createStyledButton("PLAYER VS RANDOM AI");
			Button senario2 = createStyledButton("PLAYER VS PLAYER");
			Button senario3 = createStyledButton("PLAYER VS ADVANCED AI");

			vbox.getChildren().addAll(senario1, senario2, senario3);
			vbox.setAlignment(Pos.TOP_CENTER);
			root.setBottom(vbox);

			senario1.setOnAction(e -> {
				RandomAI rand = new RandomAI();
			});

			senario2.setOnAction(e -> {
				Players player = new Players();
			});

			senario3.setOnAction(e -> {
				AiMiniMax mniMax = new AiMiniMax();
			});

			Scene scene = new Scene(root, 1100, 600);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Button createStyledButton(String text) {
		Button button = new Button(text);
		button.setStyle(
				"-fx-font-size: 20px; -fx-background-color: linear-gradient(to bottom right, #FF69B4, #87CEEB); -fx-text-fill: white;");

		ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), button);
		scaleTransition.setToX(1.2);
		scaleTransition.setToY(1.2);

		button.setOnMouseEntered(event -> scaleTransition.playFromStart());

		Rectangle roundRect = new Rectangle(10, 10, Color.TRANSPARENT);
		roundRect.setArcWidth(20);
		roundRect.setArcHeight(20);
		button.setShape(roundRect);

		return button;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
