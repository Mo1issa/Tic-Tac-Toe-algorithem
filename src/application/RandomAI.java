
package application;

import java.util.Optional;
import java.util.Random;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class RandomAI extends Stage {

	private char[][] board = new char[3][3];
	private char currentPlayer = 'X';
	private int player1Wins = 0;
	private int player2Wins = 0;
	private byte rounds = 0;
	private byte maxRounds = 0;
	private GridPane gridPane;
	private Label player1Label;
	private Label player2Label;
	private Label round = new Label();
	private SimpleIntegerProperty player1WinsProperty = new SimpleIntegerProperty(0);
	private SimpleIntegerProperty player2WinsProperty = new SimpleIntegerProperty(0);

	public RandomAI() {
		initializeBoard();
		GridPane mainPane = new GridPane();
		BorderPane bord3 = new BorderPane();

		VBox vbox = new VBox(10);
		Label welcome = new Label("WELCOME TO Tic-Tac-Toe");

		Button scenario1 = new Button("Player Vs Random AI");

		vbox.getChildren().addAll(welcome, scenario1);
		mainPane.add(vbox, 5, 1);
		mainPane.setAlignment(Pos.CENTER);

		String player1Name = getPlayerName("Enter Your Name:");

		// Ask who will start the game
		char startingPlayer = showStartingPlayerDialog(player1Name, "AI Random");

		player1Label = new Label(player1Name );
		player2Label = new Label("AI Random");

		// Get the number of rounds from the user
		maxRounds = getNumberOfRounds();
		rounds = 0;

		// Initialize the board
		initializeBoard();
		round = new Label(" Round (" + " 1 " + " / " + maxRounds + ")");
		Label player1 = createPlayerLabel(player1Name, player1WinsProperty);
		Label player2 = createPlayerLabel("AI Random", player2WinsProperty);
		// Create the game grid
		gridPane = createAndInitializeGrid();
		bord3.setCenter(gridPane);
		bord3.setLeft(player1);
		bord3.setRight(player2);
		bord3.setBottom(round);
		round.setFont(Font.font("Arial", FontWeight.BOLD, 18)); // Set label font

		BorderPane.setAlignment(gridPane, Pos.BOTTOM_CENTER);
		BorderPane.setAlignment(player1, Pos.CENTER_LEFT);
		BorderPane.setAlignment(player2, Pos.CENTER_RIGHT);
		BorderPane.setAlignment(round, Pos.BOTTOM_CENTER);
		Scene scene3 = new Scene(bord3, 900, 600);

		// Set background image
		scene3.getRoot().setStyle("-fx-background-image: url('gameImage.jpg');");

		Stage sRandStage = new Stage();
		sRandStage.setScene(scene3);
		sRandStage.show();

		// Set the currentPlayer based on the chosen symbol and starting player
		currentPlayer = (startingPlayer == 'P') ? 'X' : 'O';

		// Make the first move if the player starts
		if (currentPlayer == 'X') {
			// Let the player make a move
			makePlayerMove(gridPane);
		} else if (currentPlayer == 'O') {
			// If AI starts, make its move using Random AI
			makeAIMoveRandom(gridPane);
		} else {
			// If AI starts, make its move using Random AI
			makeAIMoveRandom(gridPane);
		}

	}

	private char showStartingPlayerDialog(String player1Name, String player2Name) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Choose Starting Player");
		alert.setHeaderText(player1Name + " vs " + player2Name);
		alert.setContentText("Who will start the game?\n\nChoose:\n'P' for Player\n'A' for AI");

		ButtonType buttonTypeP = new ButtonType("P");
		ButtonType buttonTypeA = new ButtonType("A");

		alert.getButtonTypes().setAll(buttonTypeP, buttonTypeA);

		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.setStyle("-fx-background-color: #a0a0a0;");

		// Reset the board and rounds on a new game
		initializeBoard();

		return alert.showAndWait().orElse(buttonTypeP).getText().charAt(0);
	}

//

	private void makeAIMoveRandom(GridPane gridPane) {
		char aiSymbol = 'O'; // Determine AI symbol

		int[] aiMove = random_option(board, aiSymbol);
		int row = aiMove[0];
		int col = aiMove[1];

		Button aiButton = getButtonAt(gridPane, row, col);
		aiButton.setText(String.valueOf(aiSymbol));

		// Update the board based on the chosen button's row and column
		board[row][col] = aiSymbol;

		// Check if the game is over
		if (!isGameOver()) {
			currentPlayer = 'X'; // Switch player after AI moves
			// Let the player make a move
			makePlayerMove(gridPane);
		} else {
			// If the game is over, handle the result
			handleGameResult(player2Label, gridPane);
		}
	}

	private int[] random_option(char[][] board, char currentPlayer) {
		Random random = new Random();
		int row, col;

		do {
			// Generate random row and column within the board size
			row = random.nextInt(3);
			col = random.nextInt(3);
		} while (board[row][col] != ' '); // Keep generating until an empty spot is found

		return new int[] { row, col };
	}

	private void makePlayerMove(GridPane gridPane) {
		// Allow the user to make a move
		for (Node node : gridPane.getChildren()) {
			if (node instanceof Button) {
				Button button = (Button) node;
				button.setOnAction(e -> {
					int row = GridPane.getRowIndex(button);
					int col = GridPane.getColumnIndex(button);

					// Check if the button is empty before allowing the move
					if (board[row][col] == ' ') {
						// Update the board based on the chosen button's row and column
						board[row][col] = 'X';

						// Update the UI with the player's move
						button.setText("X");

						// Check if the game is over
						if (isGameOver()) {
							handleGameResult(player1Label, gridPane);
						} else {
							// Let AI (Random) make a move after the player
							makeAIMoveRandom(gridPane);
						}
					}
				});
			}
		}
	}

	private String getPlayerName(String prompt) {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Player Name");
		dialog.setHeaderText(prompt);
		dialog.setContentText("Enter name:");
		return dialog.showAndWait().orElse("Unknown");
	}

	private byte getNumberOfRounds() {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Number of Rounds");
		dialog.setHeaderText("Enter the number of rounds to play:");
		dialog.setContentText("Rounds:");

		return Byte.parseByte(dialog.showAndWait().orElse("1"));
	}

	private void updateWinCounter(SimpleIntegerProperty winCountProperty) {
		winCountProperty.set(winCountProperty.get() + 1);
	}

	private void updateWinCounter(Label winnerLabel) {
		if (winnerLabel == player1Label) {
			player1Wins++;
		} else {
			player2Wins++;
		}
	}

	private void displayOverallResult() {
		String resultMessage;

		if (player1Wins > player2Wins) {
			resultMessage = player1Label.getText() + " is the overall winner!" + "\nBy :" + player1Wins;
		} else if (player2Wins > player1Wins) {
			resultMessage = player2Label.getText() + " is the overall winner!" + "\nBy :" + player2Wins;
		} else {
			resultMessage = "It's a tie! Both players have the same number of wins.";
		}

		Alert overallResult = new Alert(Alert.AlertType.INFORMATION);
		overallResult.setTitle("Overall Result");
		overallResult.setHeaderText(resultMessage);
		overallResult.showAndWait();
	}

	private boolean checkForWin(char player) {
		// Check rows, columns, and diagonals for a win
		for (int i = 0; i < 3; i++) {
			if (board[i][0] == player && board[i][1] == player && board[i][2] == player) {
				return true; // Row win
			}
			if (board[0][i] == player && board[1][i] == player && board[2][i] == player) {
				return true; // Column win
			}
		}
		if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
			return true; // Diagonal win
		}
		if (board[0][2] == player && board[1][1] == player && board[2][0] == player) {
			return true; // Diagonal win
		}
		return false;
	}

	private boolean checkForTie() {
		// Check if the board is full (no empty spaces) and no player has won
		boolean isFull = isBoardFull();
		boolean xWins = checkForWin('X');
		boolean oWins = checkForWin('O');

		return isFull && !xWins && !oWins;
	}

	private boolean isBoardFull() {
		// Check if the board is full (no empty spaces)
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (board[i][j] == ' ') {
					return false; // There is an empty space, board is not full
				}
			}
		}
		return true; // Board is full
	}
	private void handleGameResult(Label winnerLabel, GridPane gridPane) {
	    Alert alert = new Alert(Alert.AlertType.INFORMATION);
	    alert.setTitle("Game Over");

	    if (checkForWin('X')) {
	        alert.setHeaderText("Player 1 wins!");
	        updateWinCounter(player1Label);
	        updateWinCounter(player1WinsProperty);
	    } else if (checkForWin('O')) {
	        alert.setHeaderText("Player 2 (AI) wins!");
	        updateWinCounter(player2Label);
	        updateWinCounter(player2WinsProperty);
	    } else {
	        alert.setHeaderText("It's a tie!");
	    }

	    alert.setContentText(winnerLabel != null ? winnerLabel.getText() : "");
	    alert.showAndWait();

	    // Increment the rounds counter
	    rounds++;

	    // Check if either player has won more than half of the maximum rounds
	    if (player1Wins > maxRounds / 2 || player2Wins > maxRounds / 2) {
	        // Ask the user if they want to continue
	        Alert continueAlert = new Alert(Alert.AlertType.CONFIRMATION);
	        continueAlert.setTitle("Continue?");
	        continueAlert.setHeaderText("Do you want to continue to the next round?");
	        continueAlert.setContentText("Choose your option.");

	        ButtonType continueButton = new ButtonType("Continue");
	        ButtonType finishButton = new ButtonType("Finish");

	        continueAlert.getButtonTypes().setAll(continueButton, finishButton);

	        DialogPane dialogPane = continueAlert.getDialogPane();
	        dialogPane.setStyle("-fx-background-color: #a0a0a0;");

	        // Reset the board and rounds on a new game
	        initializeBoard();

	        // Show the alert and handle the user's choice
	        Optional<ButtonType> result = continueAlert.showAndWait();
	        if (result.isPresent() && result.get() == continueButton) {
	            // Start a new round
	            char startingPlayer = showStartingPlayerDialog(player1Label.getText(), " Random AI ");
	            // Set the currentPlayer based on the chosen symbol and starting player
	            currentPlayer = (startingPlayer == 'P') ? 'X' : 'O';

	            resetButtons(gridPane);
	            int roundNum = rounds + 1;
	            round.setText(" Rounds  " + "(" + roundNum + " / " + maxRounds + ")");

	            // Make the first move if the player starts
	            if (currentPlayer == 'X') {
	                // Let the player make a move
	                makePlayerMove(gridPane);
	            } else if (currentPlayer == 'O') {
	                // If AI starts, make its move
	                makeAIMoveRandom(gridPane);
	            }
	        } else {
	            // Display overall result when the user chooses not to continue
	            displayOverallResult();
	        }
	    } else {
	        // Check if all rounds are completed
	        if (rounds < maxRounds) {
	            // Start a new round
	            char startingPlayer = showStartingPlayerDialog(player1Label.getText(), " Random AI ");
	            // Set the currentPlayer based on the chosen symbol and starting player
	            currentPlayer = (startingPlayer == 'P') ? 'X' : 'O';

	            resetButtons(gridPane);
	            int roundNum = rounds + 1;
	            round.setText(" Rounds  " + "(" + roundNum + " / " + maxRounds + ")");

	            // Make the first move if the player starts
	            if (currentPlayer == 'X') {
	                // Let the player make a move
	                makePlayerMove(gridPane);
	            } else if (currentPlayer == 'O') {
	                // If AI starts, make its move
	                makeAIMoveRandom(gridPane);
	            }
	        } else {
	            // Display overall result when all rounds are completed
	            displayOverallResult();
	        }
	    }
	}

	private void handleButtonClick(Button button, Integer row, Integer col, GridPane gridPane) {
		// Your existing implementation for handling button clicks
	}

	private Button getButtonAt(GridPane gridPane, int row, int col) {
		for (Node node : gridPane.getChildren()) {
			if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col && node instanceof Button) {
				return (Button) node;
			}
		}
		return null;
	}

	private void initializeBoard() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				board[i][j] = ' ';
			}
		}
	}

	private GridPane createAndInitializeGrid() {
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				Button button = createButton(i, j);
				gridPane.add(button, j, i);
			}
		}

		return gridPane;
	}

	private Button createButton(int row, int col) {
		Button button = new Button();
		// Increase button size
		button.setPrefSize(120, 120);

		// Set a fun and game-like font
		button.setStyle("-fx-font-family: 'Comic Sans MS'; -fx-font-size: 22;");

		// Add drop shadow effect
		DropShadow dropShadow = new DropShadow();
		dropShadow.setColor(Color.GRAY);
		dropShadow.setRadius(5);
		dropShadow.setOffsetX(3);
		dropShadow.setOffsetY(3);
		button.setEffect(dropShadow);

		button.setOnAction(e -> handleButtonClick(button, row, col, gridPane));
		return button;
	}

	private void resetButtons(GridPane gridPane) {
		for (Node node : gridPane.getChildren()) {
			if (node instanceof Button) {
				Button button = (Button) node;
				button.setText("");
			}
		}
	}

	private boolean isGameOver() {
		return checkForWin('X') || checkForWin('O') || checkForTie();
	}
	private Label createPlayerLabel(String playerLabelP, SimpleIntegerProperty winCountProperty) {
		Label playerLabel = new Label(playerLabelP);
		playerLabel.setTextFill(Color.WHITE);
		playerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

		// Bind the text property of the label to the win count property
		playerLabel.textProperty().bind(Bindings.concat(playerLabel.getText(), "\nWins: ", winCountProperty));

		return playerLabel;
	}

}