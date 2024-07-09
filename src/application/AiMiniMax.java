
package application;

import java.util.Random;

import javafx.application.Application;
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

public class AiMiniMax extends Stage {
	private int[] aiChosenMove;
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

	public AiMiniMax() {
		initializeBoard();
		GridPane mainPane = new GridPane();
		BorderPane bord3 = new BorderPane();

		VBox vbox = new VBox(10);
		Label welcome = new Label("WELCOME TO Tic-Tac-Toe");
		Button scenario3 = new Button("Player Vs AI");

		vbox.getChildren().addAll(welcome, scenario3);
		mainPane.add(vbox, 5, 1);
		mainPane.setAlignment(Pos.CENTER);

		String player1Name = getPlayerName("Enter Your Name:");

		// Ask who will start the game
		char startingPlayer = showStartingPlayerDialog(player1Name, "AI miniMax");

		player1Label = new Label(player1Name);
		player2Label = new Label("AI miniMax");

		// Get the number of rounds from the user
		maxRounds = getNumberOfRounds();
		rounds = 0;

		// Initialize the board
		initializeBoard();
		round = new Label(" Round (" + " 1 " + " / " + maxRounds + ")");
		round.setFont(Font.font("Arial", FontWeight.BOLD, 18)); // Set label font
		Label player1 = createPlayerLabel(player1Name, player1WinsProperty);
		Label player2 = createPlayerLabel("AI miniMax", player2WinsProperty);

		// Create the game grid
		gridPane = createAndInitializeGrid();
		bord3.setCenter(gridPane);
		bord3.setLeft(player1);
		bord3.setRight(player2);
		bord3.setBottom(round);
		BorderPane.setAlignment(gridPane, Pos.BOTTOM_CENTER);
		BorderPane.setAlignment(player1, Pos.CENTER_LEFT);
		BorderPane.setAlignment(player2, Pos.CENTER_RIGHT);
		BorderPane.setAlignment(round, Pos.BOTTOM_CENTER);
		Scene scene3 = new Scene(bord3, 900, 600);

		// Set background image
		scene3.getRoot().setStyle("-fx-background-image: url('gameImage.jpg');");

		Stage smimiMax = new Stage();
		smimiMax.setScene(scene3);
		smimiMax.show();

		// Set the currentPlayer based on the chosen symbol and starting player
		currentPlayer = (startingPlayer == 'P') ? 'X' : 'O';

		// Make the first move if the player starts
		if (currentPlayer == 'X') {
			// Let the player make a move
			makePlayerMove(gridPane);
		} else if (currentPlayer == 'O') {
			// If AI starts, make its move
			makeAIMoveMinimax(gridPane);
		} else {
			// If AI starts, make its move
			makeAIMoveMinimax(gridPane);
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

	private void makeAIMoveMinimax(GridPane gridPane) {
		char aiSymbol = 'O'; // Determine AI symbol

		aiChosenMove = minimax(board, aiSymbol);
		int row = aiChosenMove[0];
		int col = aiChosenMove[1];

		Button aiButton = getButtonAt(gridPane, row, col);
		aiButton.setText(String.valueOf(aiSymbol));
		printMoveProbabilities();

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

	private boolean areThreeButtonsLeft(GridPane gridPane) {
		int emptyButtonCount = 0;

		for (Node node : gridPane.getChildren()) {
			if (node instanceof Button) {
				Button button = (Button) node;
				if (button.getText().isEmpty()) {
					emptyButtonCount++;
				}
			}
		}

		return emptyButtonCount <= 3;
	}

	private void printMoveProbabilities() {
	    char aiSymbol = 'O'; // Determine AI symbol

	    Alert probabilitiesAlert = new Alert(Alert.AlertType.INFORMATION);
	    probabilitiesAlert.setTitle("AI Move Probabilities");
	    probabilitiesAlert.setHeaderText(null);

	    StringBuilder contentText = new StringBuilder("AI Move Probabilities:\n");

	    // Iterate through all buttons in the grid
	    for (Node node : gridPane.getChildren()) {
	        if (node instanceof Button) {
	            Button button = (Button) node;
	            int row = GridPane.getRowIndex(button);
	            int col = GridPane.getColumnIndex(button);

	            if (board[row][col] == ' ') {
	                board[row][col] = aiSymbol;

	                // Calculate probabilities for the current empty button
	                int probabilityWin = minimaxHelper(board, 0, false);

	                // Build probabilities contentText based on the conditions
	                contentText.append("Button ").append(row * 3 + col + 1)
	                        .append(" has chances of (win, loss, tie): (");

	                if (aiSymbol == 'O') {
	                    contentText.append(probabilityWin == 1 ? "win, " : "");
	                    contentText.append(probabilityWin == -1 ? "loss, " : "");
	                } else {
	                    contentText.append(probabilityWin == 1 ? "loss, " : "");
	                    contentText.append(probabilityWin == -1 ? "win, " : "");
	                }

	                // Include "tie" if there is a tie
	                contentText.append(probabilityWin == 0 ? "tie)" : ")");
	                contentText.append("\n");

	                board[row][col] = ' '; // Undo the move
	            }
	        }
	    }

	    // Clear existing button types to avoid duplicates
	    probabilitiesAlert.getButtonTypes().clear();

	    // Add an OK button to the alert
	    probabilitiesAlert.getButtonTypes().add(ButtonType.OK);

	    // Set the contentText of the alert
	    probabilitiesAlert.setContentText(contentText.toString());

	    // Show the alert and wait for the user to click OK
	    probabilitiesAlert.showAndWait();

	    // Show the AI's chosen move
	    Button aiButton = getButtonAt(gridPane, aiChosenMove[0], aiChosenMove[1]);
	    if (aiButton != null) {
	        Alert aiMoveAlert = new Alert(Alert.AlertType.INFORMATION);
	        aiMoveAlert.setTitle("AI Move");
	        aiMoveAlert.setHeaderText("AI will play in this button:");
	        aiMoveAlert.setContentText("Button " + (aiChosenMove[0] * 3 + aiChosenMove[1] + 1));
	        aiMoveAlert.showAndWait();
	    }
	}
	private boolean isBoardEmpty(char[][] board) {
	    // Check if all cells in the board are empty
	    for (int i = 0; i < 3; i++) {
	        for (int j = 0; j < 3; j++) {
	            if (board[i][j] != ' ') {
	                return false; // Board is not empty
	            }
	        }
	    }
	    return true; // Board is empty
	}


	private int[] minimax(char[][] board, char currentPlayer) {
		
		
		int bestScore = (currentPlayer == 'O') ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		
		if(isBoardEmpty( board)) {
			Random random = new Random();
			int row, col;

			do {
				// Generate random row and column within the board size
				row = random.nextInt(3);
				col = random.nextInt(3);
			} while (board[row][col] != ' '); // Keep generating until an empty spot is found

			return new int[] { row, col };
		
		}
		int[] bestMove = new int[] { -1, -1 };

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (board[i][j] == ' ') {
					board[i][j] = currentPlayer;
					int score = minimaxHelper(board, 0, false);
					board[i][j] = ' '; // Undo the move

					if ((currentPlayer == 'O' && score > bestScore) || (currentPlayer == 'X' && score < bestScore)) {
						bestScore = score;
						bestMove = new int[] { i, j };
					}
				}
			}
		}

		return bestMove;
	}

	private int minimaxHelper(char[][] board, int depth, boolean isMaximizing) {
		if (checkForWin('O')) {
			return 1;
		} else if (checkForWin('X')) {
			return -1;
		} else if (checkForTie()) {
			return 0;
		}

		if (isMaximizing) {
			int bestScore = Integer.MIN_VALUE;
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					if (board[i][j] == ' ') {
						board[i][j] = 'O';
						int score = minimaxHelper(board, depth + 1, false);
						board[i][j] = ' '; // Undo the move
						bestScore = Math.max(score, bestScore);
					}
				}
			}
			return bestScore;
		} else {
			int bestScore = Integer.MAX_VALUE;
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					if (board[i][j] == ' ') {
						board[i][j] = 'X';
						int score = minimaxHelper(board, depth + 1, true);
						board[i][j] = ' '; // Undo the move
						bestScore = Math.min(score, bestScore);
					}
				}
			}
			return bestScore;
		}
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
							// Let AI (Minimax) make a move after the player
							makeAIMoveMinimax(gridPane);
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

	private Label createPlayerLabel(String playerLabelP, SimpleIntegerProperty winCountProperty) {
		Label playerLabel = new Label(playerLabelP);
		playerLabel.setTextFill(Color.WHITE);
		playerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

		// Bind the text property of the label to the win count property
		playerLabel.textProperty().bind(Bindings.concat(playerLabel.getText(), "\nWins: ", winCountProperty));

		return playerLabel;
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

	// ...

	private void handleGameResult(Label winnerLabel, GridPane gridPane) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Game Over");

		// Update the win counters based on the winner
		if (checkForWin('X')) {
			alert.setHeaderText("Player 1 wins!");
			updateWinCounter(player1WinsProperty);
			updateWinCounter(winnerLabel);
		} else if (checkForWin('O')) {
			alert.setHeaderText("Player 2 (AI) wins!");
			updateWinCounter(player2WinsProperty);
			updateWinCounter(winnerLabel);
		} else {
			alert.setHeaderText("It's a tie!");
			// No win counter is updated in case of a tie
		}
		alert.setContentText(winnerLabel != null ? winnerLabel.getText() : "");
		alert.showAndWait();

		// Increment the rounds counter
		rounds++;

		// Check if either player has won more than half of the max rounds
		if (player1Wins > maxRounds / 2 || player2Wins > maxRounds / 2) {
			// Ask the user if they want to continue
			if (askToContinue()) {
				// Continue the game
				startNewRound(gridPane);
			} else {
				// Finish the game and display the overall result
				displayOverallResult();
			}
		} else {
			// Check if all rounds are completed
			if (rounds < maxRounds) {
				// Start a new round
				startNewRound(gridPane);
			} else {
				// Display overall result when all rounds are completed
				displayOverallResult();
			}
		}
	}

	private boolean askToContinue() {
		Alert confirmContinue = new Alert(Alert.AlertType.CONFIRMATION);
		confirmContinue.setTitle("Continue or Finish?");
		confirmContinue.setHeaderText("One of the players has won more than half of the rounds.");
		confirmContinue.setContentText("Do you want to continue playing?\n\nChoose:");

		ButtonType buttonTypeContinue = new ButtonType("Continue");
		ButtonType buttonTypeFinish = new ButtonType("Finish");

		confirmContinue.getButtonTypes().setAll(buttonTypeContinue, buttonTypeFinish);

		return confirmContinue.showAndWait().orElse(buttonTypeFinish).getText().equals("Continue");
	}

	private void startNewRound(GridPane gridPane) {
		char startingPlayer = showStartingPlayerDialog(player1Label.getText(), "AI miniMax");
		// Set the currentPlayer based on the chosen symbol and starting player
		currentPlayer = (startingPlayer == 'P') ? 'X' : 'O';

		initializeBoard();
		resetButtons(gridPane);
		int roundNum = rounds + 1;
		round.setText(" Rounds  " + "(" + roundNum + " / " + maxRounds + ")");

		// Make the first move if the player starts
		if (currentPlayer == 'X') {
			// Let the player make a move
			makePlayerMove(gridPane);
		} else if (currentPlayer == 'O') {
			// If AI starts, make its move
			makeAIMoveMinimax(gridPane);
		}
	}

	// ...
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

}