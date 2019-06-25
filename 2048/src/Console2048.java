//This version of 2048 runs in terminal
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Console2048 {
	private int[][] board, boardCopy, testBoard;
	ArrayList<Integer> empty = new ArrayList<Integer>();
	private int score;
	private final int SIZE;

	public Console2048(int s) {
		SIZE = s;
		board = new int[SIZE][SIZE];
		boardCopy = new int[SIZE][SIZE];
		testBoard = new int[SIZE][SIZE];
	}

	public boolean addNum() // returns false if no room, else returns true
	{
		// first generate 2 or 4
		int rand, row, col;
		if (Math.random() < 0.9)
			rand = 2;
		else
			rand = 4;

		// create a list of all empty spaces
		// clear it first
		empty.clear();
		for (int r = 0; r < SIZE; r++) {
			for (int c = 0; c < SIZE; c++) {
				if (board[r][c] == 0) {
					empty.add(r);
					empty.add(c);
				}

			}
		}

		if (empty.size() == 0)
			return false;

		// select random coordinates
		int index = 2 * (int) (Math.random() * ((empty.size() - 2) / 2) + 0.5);
		row = empty.get(index);
		col = empty.get(index + 1);
		board[row][col] = rand;
		// for debugging -->
		// System.out.println(row + ", " + col);
		// System.out.println(Arrays.toString(empty.toArray()));
		return true;
	}

	public void right(int[][] someBoard) {
		for (int r = 0; r < SIZE; r++) {
			int cap = SIZE; // place less than c
			for (int c = SIZE - 2; c >= 0; c--) {

				while (someBoard[r][c] != 0 && c != SIZE - 1 && c < cap
						&& (someBoard[r][c + 1] == someBoard[r][c] || someBoard[r][c + 1] == 0)) // its not 0 and not in
																									// the last column
																									// and (its the same
																									// as the one to the
																									// right or equal to
																									// 0)
				{
					if (someBoard[r][c + 1] == 0) {
						someBoard[r][c + 1] = someBoard[r][c];
						someBoard[r][c] = 0;
						c++;
					} else if (someBoard[r][c] == someBoard[r][c + 1]) {
						someBoard[r][c + 1] = someBoard[r][c] * 2;
						score += someBoard[r][c + 1];
						someBoard[r][c] = 0;
						cap = c;
					}
				}
			}
		}
	}

	public void left(int[][] someBoard) {
		for (int r = 0; r < SIZE; r++) {
			int cap = -1;
			for (int c = 1; c < SIZE; c++) {

				while (someBoard[r][c] != 0 && c != 0 && c > cap
						&& (someBoard[r][c - 1] == someBoard[r][c] || someBoard[r][c - 1] == 0)) // its not 0 and not in
																									// the last column
																									// and (its the same
																									// as the one to the
																									// right or equal to
																									// 0)
				{
					if (someBoard[r][c - 1] == 0) {
						someBoard[r][c - 1] = someBoard[r][c];
						someBoard[r][c] = 0;
						c--;
					} else if (someBoard[r][c] == someBoard[r][c - 1]) {
						someBoard[r][c - 1] = someBoard[r][c] * 2;
						score += someBoard[r][c - 1];
						someBoard[r][c] = 0;
						cap = c;
					}
				}
			}
		}
	}

	public void up(int[][] someBoard) {
		for (int c = 0; c < SIZE; c++) {
			int cap = -1;
			for (int r = 1; r < SIZE; r++) {

				while (someBoard[r][c] != 0 && r != 0 && r > cap
						&& (someBoard[r - 1][c] == someBoard[r][c] || someBoard[r - 1][c] == 0)) // its not 0 and not in
																									// the last column
																									// and (its the same
																									// as the one to the
																									// right or equal to
																									// 0)
				{
					if (someBoard[r - 1][c] == 0) {
						someBoard[r - 1][c] = someBoard[r][c];
						someBoard[r][c] = 0;
						r--;
					} else if (someBoard[r][c] == someBoard[r - 1][c]) {
						someBoard[r - 1][c] = someBoard[r][c] * 2;
						score += someBoard[r - 1][c];
						someBoard[r][c] = 0;
						cap = r;
					}
				}
			}
		}
	}

	public void down(int[][] someBoard) {
		for (int c = 0; c < SIZE; c++) {
			int cap = SIZE; // place less than c
			for (int r = SIZE - 2; r >= 0; r--) {

				while (someBoard[r][c] != 0 && r != SIZE - 1 && r < cap
						&& (someBoard[r + 1][c] == someBoard[r][c] || someBoard[r + 1][c] == 0)) // its not 0 and not in
																									// the last column
																									// and (its the same
																									// as the one to the
																									// right or equal to
																									// 0)
				{
					if (someBoard[r + 1][c] == 0) {
						someBoard[r + 1][c] = someBoard[r][c];
						someBoard[r][c] = 0;
						r++;
					} else if (someBoard[r][c] == someBoard[r + 1][c]) {
						someBoard[r + 1][c] = someBoard[r][c] * 2;
						score += someBoard[r + 1][c];
						someBoard[r][c] = 0;
						cap = r;
					}
				}
			}
		}
	}

	public void updateCopy() {
		for (int r = 0; r < SIZE; r++) {
			for (int c = 0; c < SIZE; c++) {
				boardCopy[r][c] = board[r][c];
			}
		}

	}

	public boolean stateChange(int[][] someBoard, int[][] thisBoard) // returns true if changed
	{
		for (int r = 0; r < SIZE; r++) {
			for (int c = 0; c < SIZE; c++) {
				if (someBoard[r][c] != thisBoard[r][c])
					return true;
			}
		}
		return false;
	}

	public boolean gameOver() {
		for (int r = 0; r < SIZE; r++) {
			for (int c = 0; c < SIZE; c++) {
				testBoard[r][c] = board[r][c];
			}
		}
		int s = score;

		// checks if any possible moves
		right(testBoard);
		if (stateChange(testBoard, board)) {
			score = s;
			return false;
		}

		left(testBoard);
		if (stateChange(testBoard, board)) {
			score = s;
			return false;
		}

		up(testBoard);
		if (stateChange(testBoard, board)) {
			score = s;
			return false;
		}

		down(testBoard);
		if (stateChange(testBoard, board)) {
			score = s;
			return false;
		}
		score = s;
		return true;
	}

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		System.out.println("Swipe using 'WASD' keys");
		System.out.println("Enter 'Q' to quit");
		System.out.print("Enter board size: ");
		int size = input.nextInt();

		Console2048 game = new Console2048(size); // ENTER SIZE HERE
		// add 2 seeds
		game.addNum();
		game.addNum();

		while (true) {
			game.updateCopy(); // creates identical board

			for (int j = 0; j < game.SIZE; j++) {
				System.out.println(Arrays.toString(game.board[j]));
			}
			String wasd = input.nextLine();
			System.out.println("");

			switch (wasd) {
			case "w": {
				game.up(game.board);
				break;
			}
			case "a": {
				game.left(game.board);
				break;
			}
			case "s": {
				game.down(game.board);
				break;
			}
			case "d": {
				game.right(game.board);
				break;
			}
			}

			if (wasd.equals("q"))
				break;

			// System.out.println(game.stateChange());
			if (game.stateChange(game.boardCopy, game.board)) // if state changes, add num
			{
				game.addNum();
			}
			if (game.gameOver()) {
				for (int j = 0; j < game.SIZE; j++) {
					System.out.println(Arrays.toString(game.board[j]));
				}
				System.out.println("***Game Over***");
				break;
			}
			System.out.println("Score: " + game.score);
		}
		input.close();
	}

}
