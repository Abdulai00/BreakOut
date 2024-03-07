package game;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Represents a brick object in the BreakOut game. Inherits from the Polygon
 * class to define the shape and position of the brick.
 */
public class Brick extends Polygon {

	private int health;
	public boolean visible;

	public Brick(Point[] array, Point inPosition, double inRotation) {
		super(array, inPosition, inRotation);
		this.visible = true;
		this.health = 1;
	}

	public int[][] getXY() {
		Point[] prev = super.getPoints();
		int[][] answer = new int[2][prev.length];

		for (int i = 0; i < prev.length; i++) {
			answer[0][i] = (int) prev[i].getX();
			answer[1][i] = (int) prev[i].getY();
		}

		return answer;
	}

	void paint(Graphics brush) {
		int[][] xy = this.getXY();
		brush.setColor(Color.white);
		brush.fillPolygon(xy[0], xy[1], 4);
	}

	/**
	 * Handles collision with the slider object. Reduces the brick's health upon
	 * collision. If the health becomes 0, the brick disappears, and the player's
	 * score is increased.
	 *
	 * @param slider The slider object involved in the collision.
	 */
	void collision(Slider slider) {
		if (this.health > 0) {
			this.health--;
			// If health becomes 0, the brick should disappear
			if (this.health == 0) {
				slider.rotation = 0;
				this.visible = false;
				BreakOut.score++;
			}
		}
	}
}
