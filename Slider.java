package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyListener;

public class Slider extends Polygon {
	public boolean goingLeft = false;

	public boolean goingRight = false;

	public Slider(Point[] array, Point inPosition, double inRotation) {
		super(array, inPosition, inRotation);

	}
	
	
	/**
	 * Retrieves the x and y coordinates of the vertices of the polygon as integers.
	 *
	 * @return A 2D array where the first row contains the x-coordinates and the second row contains the y-coordinates
	 *         of the vertices of the polygon.
	 */

	public int[][] getXY() {
		Point[] prev = super.getPoints();
		int[][] answer = new int[2][prev.length];

		for (int i = 0; i < prev.length; i++) {
			answer[0][i] = (int) prev[i].getX();
			answer[1][i] = (int) prev[i].getY();
		}

		return answer;

	}
	
	
	/**
	 * Paints the polygon using the specified graphics context.
	 * The polygon is filled with a cyan color.
	 * The position of the polygon is adjusted based on the boolean flags goingLeft and goingRight.
	 * If goingLeft is true and the x-coordinate of the position is greater than 40, the position moves left by 10 units.
	 * If goingRight is true and the x-coordinate of the position is less than 699, the position moves right by 10 units.
	 * 
	 * @param brush The graphics context to use for painting.
	 */

	void paint(Graphics brush) {

		int[][] xy = this.getXY();

		brush.setColor(Color.cyan);
		brush.fillPolygon(xy[0], xy[1], 4);

		Move lambda = () -> {

			if (goingLeft && this.position.x > 40) {
				this.position.x -= 10;
			} else if (goingRight && this.position.x < 699) {
				this.position.x += 10;
			}

		};
		lambda.move();

	}

}
