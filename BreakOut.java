package game;

/*
CLASS: YourGameNameoids
DESCRIPTION: Extending Game, YourGameName is all in the paint method.
NOTE: This class is the metaphorical "main method" of your program,
      it is your control center.

*/
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Random;

import javax.swing.JButton;

class BreakOut extends Game implements KeyListener {

	/**
	 * Constructs a BreakOut game instance with default settings. Initializes game
	 * components such as the slider, ball, map, and score. Sets up initial game
	 * state.
	 */

	public Slider slider;

	public Ball ball;

	public static int counter = 0;

	public Brick[][] map = new Brick[12][5];

	Random random = new Random();

	public static int score = 0;

	public static boolean gameOn = true;

	public BreakOut() {
		super("BreakOut!", 800, 600);
		// width, height
		this.setFocusable(true);
		this.requestFocus();

		Point[] sliderP = { new Point(0, 0), new Point(0, 14), new Point(100, 14), new Point(100, 0) };

		slider = new Slider(sliderP, new Point(400, 500), 0);

		this.addKeyListener(this);

		Circle circle = new Circle(12.5);

		Point[] points = circle.getPoints();

		ball = new Ball(points, new Point(300, 300), 0, Circle.numVertices, 12.5);

		setupMap();

	}

	/**
	 * Sets up the map by creating and positioning bricks within the game
	 * environment. This method populates the 'map' array with brick objects,
	 * positioning them accordingly. The bricks are created with predefined points
	 * and placed at calculated positions with specified spacing.
	 */

	public void setupMap() {
		// Define the points for the brick
		Point[] bp = { new Point(0, 0), new Point(0, 14), new Point(50, 14), new Point(50, 0) };

		int startX = 50;
		int startY = 50;
		int brickWidth = 50;
		int brickHeight = 15;
		int spacingX = 10;
		int spacingY = 5;

		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				// Calculate the position for each brick
				int posX = startX + i * (brickWidth + spacingX);
				int posY = startY + j * (brickHeight + spacingY);

				// Create the brick and assign it to the map
				map[i][j] = new Brick(bp, new Point(posX, posY), 0);
			}
		}
	}

	/**
	 * Paints the game components on the screen. This method is responsible for
	 * rendering the game environment, including the slider, ball, bricks, and
	 * score.
	 *
	 * @param brush the graphics context used for painting
	 */

	public void paint(Graphics brush) {
		brush.setColor(Color.black);
		brush.fillRect(0, 0, width, height);

		brush.setColor(Color.yellow);
		brush.fillRect(0, 0, 5, 600);
		brush.fillRect(0, 0, 800, 5);
		brush.fillRect(781, 0, 5, 600);

		Font font = new Font("Arial", Font.BOLD, 24);
		brush.setFont(font);
		brush.setColor(Color.cyan);
		brush.drawString("Score: " + score, 20, 25);

		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				if (map[i][j].visible) {
					map[i][j].paint(brush);
				}
			}
		}

		slider.paint(brush);
		ball.paint(brush);

		ball.inBounds(0, 0, 0, 600);
		ball.inBounds(0, 0, 800, 0);
		ball.inBounds(800, 0, 800, 600);

		if (ball.position.y > 590) {
			gameOn = false;
			gameOver(brush);

		}

		if (Victory(brush)) {
			Font font2 = new Font("cyan", Font.BOLD, 72); // Big font size
			brush.setFont(font);

			brush.setColor(Color.RED);

			brush.drawString("Your Nice Bruh", 100, 300);
			gameOn = false;
		}

		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				if (map[i][j].visible && ball.handleCollisions(map[i][j])) {
					map[i][j].collision(slider);
				}
			}

		}

		ball.handleCollisions(slider);


	}

	public void gameOver(Graphics brush) {
		Font font = new Font("Arial", Font.BOLD, 72); // Big font size
		brush.setFont(font);

		brush.setColor(Color.RED);

		brush.drawString("YOU LOST BRUH", 100, 300);
	}

	public boolean Victory(Graphics brush) {

		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				if (map[i][j].visible == true) {
					return false;
				}
			}
		}

		return true;

	}

	public void keyPressed(KeyEvent e) {
		// Handle paddle movement
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_LEFT) {
			slider.goingLeft = true;
		} else if (keyCode == KeyEvent.VK_RIGHT) {
			slider.goingRight = true;
		}

	}

	public void keyReleased(KeyEvent e) {

		int keyCode = e.getKeyCode();

		if (keyCode == KeyEvent.VK_LEFT) {
			slider.goingLeft = false;
		} else if (keyCode == KeyEvent.VK_RIGHT) {
			slider.goingRight = false;
		}

	}

	public void keyTyped(KeyEvent e) {

	}

	public static void main(String[] args) {
		BreakOut a = new BreakOut();
		a.repaint();
	}

	/**
	 * The {@code Circle} class represents a circle object defined by its radius and
	 * center. It generates points around the circumference of the circle and
	 * provides methods to access them.
	 */

	private class Circle {

		public static int numVertices = 500;

		private double radius;

		public static Point center = new Point(0, 0);

		private Point[] circleVertices = new Point[numVertices];

		public Circle(double radius) {
			this.radius = radius;
		}

		private void genPoints() {

			for (int i = 0; i < numVertices; i++) {
				double angle = Math.toRadians((360.0 / numVertices) * i);
				double x = center.x + this.radius * Math.cos(angle);
				double y = center.y + this.radius * Math.sin(angle);
				this.circleVertices[i] = new Point(x, y);
			}
		}

		private Point[] getPoints() {
			genPoints();

			return this.circleVertices;
		}

	}
}