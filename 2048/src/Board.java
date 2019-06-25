import java.util.ArrayList;

public class Board {
	private int[][] board, boardCopy, testBoard;
	ArrayList<Integer> empty = new ArrayList<Integer>();
	private int score = 0;
	private final int SIZE;
	private boolean over = false;

	public Board(int s) {
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

	public void left() {
		left(board);
	}

	public void right() {
		right(board);
	}

	public void up() {
		up(board);
	}

	public void down() {
		down(board);
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

	public int[][] getBoard() {
		return board;
	}

	public boolean getOver() {
		return over;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int s) {
		score = s;
	}

	public void setBoard(int[][] savedGame) {
		for (int r = 0; r < savedGame.length; r++) {
			for (int c = 0; c < savedGame[0].length; c++) {
				board[r][c] = savedGame[r][c];
			}
		}
		updateCopy();
	}

	public void gameSequence() {

		if (stateChange(boardCopy, board)) // if state changes, add num
		{
			addNum();
		}
		if (gameOver()) {
			over = true;
		}

		updateCopy(); // creates identical board
	}

}
