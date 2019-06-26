//writes games to games2048.txt file so highscores and boards are saved
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class SaveGames {

	private ArrayList<int[][]> games = new ArrayList<int[][]>(7);
	private int[] highScores = new int[7];
	private int[] scores = new int[7];
	private String fileName = "games2048.txt";
	private GUI gui;

	public SaveGames(GUI g) {
		gui = g;
	}

	public void resetScores() {
		for (int i = 0; i < 7; i++) {
			scores[i] = 0;
		}
	}

	public int getSavedHighScore(int size) {
		return highScores[size - 2];
	}

	public int getSavedScore(int size) {
		return scores[size - 2];
	}

	public void saveGame(int size, Board b, int s, int hs) throws IOException {
		games.set(size - 2, b.getBoard());
		scores[size - 2] = s;
		highScores[size - 2] = hs;
		writeFiles();
	}

	public Board getGame(int index) throws FileNotFoundException {
		readFiles();
		Board board = new Board(games.get(index).length);
		board.setBoard(games.get(index));
		return board;
	}

	public void readFiles() throws FileNotFoundException {
		Scanner input = new Scanner(new File(fileName));
		games.clear();
		for (int arr = 0; arr <= 6; arr++) {
			int[][] board = new int[arr + 2][arr + 2];
			for (int r = 0; r < arr + 2; r++) {
				for (int c = 0; c < arr + 2; c++) {
					board[r][c] = input.nextInt();
				}
			}
			games.add(board);
			scores[arr] = input.nextInt(); // score
			highScores[arr] = input.nextInt();// high score
		}
		input.close();
	}

	public void writeFiles() throws IOException {
		FileWriter write = new FileWriter(fileName, false);
		PrintWriter outputStream = new PrintWriter(write);
		for (int arr = 0; arr <= 6; arr++) {
			for (int r = 0; r < arr + 2; r++) {
				for (int c = 0; c < arr + 2; c++) {
					outputStream.print(games.get(arr)[r][c] + "   ");
				}
				outputStream.println("");
			}
			outputStream.println(scores[arr]);
			outputStream.println(highScores[arr]);
			outputStream.println("");
			outputStream.flush(); // flushes data in ram to file
		}
		outputStream.close(); // flushes and closes print_line
	}

	public void newGames() {
		for (int i = 0; i <= 6; i++) // adds "spacer" games to populate arraylist
		{
			Board b = new Board(2);
			games.add(b.getBoard());
		}
		for (int arr = 0; arr <= 6; arr++) {
			Board b = new Board(arr + 2);
			b.addNum();
			b.addNum();
			games.set(arr, b.getBoard()); // adds different sized boards
		}
	}

}
