import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GUI extends JPanel implements ActionListener, KeyListener {

	private JFrame window;
	private JLabel title, points_title, points, high_score_label, high_score_title;
	private JButton newG;
	private JComboBox size;
	private Insets insets;

	private Board b;
	private SaveGames save;

	private int score, highScore;
	private int SIZE = 4;
	final private int CELLSIZE = 90;
	final private int BUFFER = 1; // half buffer distance
	final private int LABEL = 100;

	final private int[] RED = { 238, 237, 242, 245, 246, 246, 237, 237, 237, 237, 237 };
	final private int[] GREEN = { 228, 224, 177, 149, 124, 94, 207, 204, 200, 197, 194 };
	final private int[] BLUE = { 218, 200, 121, 99, 95, 59, 114, 97, 80, 63, 46 };

	public GUI() throws IOException {

		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		save = new SaveGames(this);

		File f = new File("games2048.txt");
		// if games.txt not found
		if (!f.exists()) {
			// no file procedure
			save.newGames();
			save.writeFiles();
		}

		save.readFiles();

		window = new JFrame("2048");
		window.pack(); // needed to get insets for title bar height
		insets = window.getInsets();
		window.setSize(CELLSIZE * SIZE, CELLSIZE * SIZE + LABEL + insets.top); // includes title bar height
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// upon start, open saved file
		save.readFiles();
		b = save.getGame(2);
		// set current score
		score = save.getSavedScore(4);
		b.setScore(score);
		// set saved high score
		highScore = save.getSavedHighScore(4);

		window.addKeyListener(this);

		this.setLayout(null);
		this.setBackground(Color.WHITE);
		window.getContentPane().add(this);

		title = new JLabel("2048");
		Font t = new Font("SansSerif", Font.BOLD, 40);
		title.setForeground(Color.DARK_GRAY);
		title.setFont(t);
		title.setBounds(5, LABEL / 4, 120, 40);
		add(title);

		Font w = new Font("SansSerif", Font.BOLD, 16);

		points_title = new JLabel("Score: ");
		points_title.setFont(w);
		points_title.setForeground(Color.DARK_GRAY);
		points_title.setBounds(250, 5, 100, 20);
		add(points_title);

		points = new JLabel("score");
		points.setFont(w);
		points.setForeground(Color.DARK_GRAY);
		points.setBounds(250, 25, 300, 20);
		add(points);

		high_score_title = new JLabel("High Score: ");
		high_score_title.setFont(w);
		high_score_title.setForeground(Color.DARK_GRAY);
		high_score_title.setBounds(250, LABEL / 2 - 5, 150, 20);
		add(high_score_title);

		high_score_label = new JLabel("0");
		high_score_label.setFont(w);
		high_score_label.setForeground(Color.DARK_GRAY);
		high_score_label.setBounds(250, LABEL / 2 + 16, 300, 20);
		add(high_score_label);

		newG = new JButton("New Game");
		newG.setFont(w);
		newG.setForeground(Color.DARK_GRAY);
		newG.setBackground(new Color(238, 228, 218, 200));
		newG.setOpaque(true);
		newG.setFocusPainted(false);
		newG.setFocusable(true);
		newG.setBounds(120, LABEL / 8, 120, 40);
		newG.addActionListener(this);
		newG.setFocusable(false);
		add(newG);

		String[] options = { "2x2", "3x3", "4x4", "5x5", "6x6", "7x7", "8x8", "Clear Games" };
		size = new JComboBox(options);
		size.setBackground(new Color(238, 228, 218, 200));
		size.setBounds(120, LABEL / 2 + 5, 120, 40);
		size.setSelectedIndex(2);
		size.addActionListener(this);
		size.setFocusable(false);
		add(size);

		window.setResizable(true);
		window.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		// NEW GAME BUTTON
		if (e.getSource() == newG) {
			b = new Board(SIZE);
			b.addNum();
			b.addNum();
			score = 0;
			try {
				save.saveGame(SIZE, b, score, highScore);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			repaint();
		}
		// COMBO BOX
		if (e.getSource() == size) {
			// CLEAR GAMES
			if (size.getSelectedIndex() == 7) {
				save.newGames();
				save.resetScores();
				try {
					save.writeFiles();
					save.readFiles();
					b = save.getGame(SIZE - 2);
					score = 0;
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				repaint();
			}

			// IF GAME ISN'T OVER, SAVE OLD GAME
			if (!b.gameOver()) {
				try {
					save.saveGame(SIZE, b, score, highScore);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

			// IF GAME OVER, NEW GAME
			else {
				b = new Board(SIZE);
				b.addNum();
				b.addNum();
				score = 0;
			}

			// SPAWN OLD GAME
			if (size.getSelectedIndex() != 7)
				SIZE = 2 + size.getSelectedIndex();
			try {
				save.readFiles();// refresh from games2048.txt
				b = save.getGame(SIZE - 2); // save game
				score = save.getSavedScore(SIZE);
				b.setScore(score);
				highScore = save.getSavedHighScore(SIZE);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			// change window size
			if (SIZE >= 4)
				window.setSize(CELLSIZE * SIZE, CELLSIZE * SIZE + LABEL + insets.top);
			else
				window.setSize(CELLSIZE * 4, CELLSIZE * SIZE + LABEL + insets.top);
			repaint();
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(new Color(238, 228, 218, 200));
		if (SIZE < 4)
			g.fillRect(0, 0, CELLSIZE * 4, CELLSIZE * SIZE + LABEL + insets.top); // paint background color
		else
			g.fillRect(0, 0, CELLSIZE * SIZE, CELLSIZE * SIZE + LABEL + insets.top); // paint background color
		for (int r = 0; r < SIZE; r++) {
			for (int c = 0; c < SIZE; c++) {

				Color color;
				int num = b.getBoard()[r][c];
				String str = num + "";
				if (num == 0)
					color = Color.LIGHT_GRAY;
				else {
					int i = (int) (Math.log(num) / Math.log(2)) - 1;
					color = new Color(RED[i], GREEN[i], BLUE[i]);
				}

				g.setColor(color);
				int x = c * CELLSIZE;
				int y = r * CELLSIZE + LABEL;
				g.fillRect(x, y, CELLSIZE, CELLSIZE);
				g.setColor(Color.BLACK);
				g.drawRect(x + BUFFER, y + BUFFER, CELLSIZE - 2, CELLSIZE - 2);
				g.drawRect(x, y, CELLSIZE, CELLSIZE);

				if (num != 0 && num <= 4) // for tiles 2 & 4
				{
					g.setColor(Color.gray);
					drawCenteredString(g, str, new Rectangle(x, y, CELLSIZE, CELLSIZE),
							new Font("SansSerif", Font.BOLD, 24));
				} else if (num != 0 && num > 4) // all other tiles
				{
					g.setColor(Color.white);
					drawCenteredString(g, str, new Rectangle(x, y, CELLSIZE, CELLSIZE),
							new Font("SansSerif", Font.BOLD, 24));
				}
			}
		}
		// UPDATE SCORE
		score = b.getScore();
		points.setText(score + "");
		if (score > highScore)
			highScore = score;
		high_score_label.setText(highScore + "");
		if (b.getOver()) {
			g.setColor(new Color(238, 228, 218, 200));
			g.fillRect(0, LABEL, SIZE * CELLSIZE, SIZE * CELLSIZE + LABEL);
			g.setColor(Color.DARK_GRAY);
			if (SIZE < 4)
				drawCenteredString(g, "Game Over!",
						new Rectangle(0, 0, CELLSIZE * 4, CELLSIZE * SIZE + LABEL + insets.top),
						new Font("SansSerif", Font.BOLD, 50));
			else
				drawCenteredString(g, "Game Over!",
						new Rectangle(0, 0, SIZE * CELLSIZE, SIZE * CELLSIZE + 2 * (LABEL + insets.top)),
						new Font("SansSerif", Font.BOLD, 50));
		}

	}

	public void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
		FontMetrics metrics = g.getFontMetrics(font); // Get the FontMetrics
		int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
		int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent(); // add the ascent, as in java
																							// 2d 0 is top of the screen
		g.setFont(font);
		g.drawString(text, x, y);
	}

	public void keyPressed(KeyEvent e) {

		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_LEFT: {
			b.left();
			break;
		}

		case KeyEvent.VK_RIGHT: {
			b.right();
			break;
		}

		case KeyEvent.VK_UP: {
			b.up();
			break;
		}

		case KeyEvent.VK_DOWN: {
			b.down();
			break;
		}
		}
		b.gameSequence();
		repaint();

		// upload to games2048.txt
		try {
			save.saveGame(SIZE, b, score, highScore);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		// score = b.getScore();

	}

	public void keyTyped(KeyEvent e) {

	}

	public void keyReleased(KeyEvent e) {

	}

	public static void main(String[] args) throws IOException {
		new GUI();

	}
}
