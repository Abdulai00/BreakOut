package game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;


/**
 * The {@code Ball} class represents a ball object in the BreakOut game.
 * It extends the {@code Polygon} class and implements functionalities for movement,
 * collision detection, and painting the ball.
 */

public class Ball extends Polygon {

	public int numVerticies;

	public Point velocity = new Point(2, 5);

	private double radius;

	Random random = new Random();

	public Ball(Point[] array, Point inPosition, double inRotation, int numVerticies, double radius) {
		super(array, inPosition, inRotation);
		this.numVerticies = numVerticies;
		this.radius = radius;

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
	 * The polygon is filled with a random color if the counter value in BreakOut class is a multiple of 500.
	 * The polygon is then filled with the specified color and its vertices are moved according to the velocity.
	 *
	 * @param brush The graphics context to use for painting.
	 */
	void paint(Graphics brush) {

		int[][] xy = this.getXY();

		if (BreakOut.counter % 1 == 0) {
			int red = random.nextInt(256);
			int green = random.nextInt(256);
			int blue = random.nextInt(256);
			
			

			Color randomColor = new Color(red, green, blue);
			brush.setColor(randomColor);
		}

		brush.fillPolygon(xy[0], xy[1], this.numVerticies);

		Move lambda = () -> {
			this.position.x += this.velocity.x;
			this.position.y += this.velocity.y;
		};
		
		lambda.move();

	}

	public void inBounds(int x1, int y1, int x2, int y2) {
		CollisionHelper collisionHelper = new CollisionHelper();
		collisionHelper.inBounds(x1, y1, x2, y2);
	}

	// Inner class for collision related calculations
	/**
	 * The {@code CollisionHelper} class provides methods for collision-related calculations
	 * within the {@code Ball} class..
	 */
	private class CollisionHelper {
		
		
		/**
	     * Checks if the ball is within the bounds defined by the line segment
	     * from (x1, y1) to (x2, y2) and performs collision resolution if a collision occurs.
	     *
	     * @param x1 the x-coordinate of the first endpoint of the line segment
	     * @param y1 the y-coordinate of the first endpoint of the line segment
	     * @param x2 the x-coordinate of the second endpoint of the line segment
	     * @param y2 the y-coordinate of the second endpoint of the line segment
	     */

		public void inBounds(int x1, int y1, int x2, int y2) {
			// Calculate distance from the ball's position to the line defined by (x1, y1)
			// and (x2, y2)
			double distance = distanceToLine(Ball.this.position.x, Ball.this.position.y, x1, y1, x2, y2);

			// Check if the distance is within a threshold (e.g., the ball's radius)
			if (distance <= Ball.this.radius) {
				// Determine collision normal based on the line segment's orientation
				Point collisionNormal = calculateCollisionNormal(x1, y1, x2, y2);

				// Calculate new direction using reflection physics
				Ball.this.velocity.x -= 2
						* (Ball.this.velocity.x * collisionNormal.x + Ball.this.velocity.y * collisionNormal.y)
						* collisionNormal.x;
				Ball.this.velocity.y -= 2
						* (Ball.this.velocity.x * collisionNormal.x + Ball.this.velocity.y * collisionNormal.y)
						* collisionNormal.y;
			}
		}
		
		
		 /**
	     * Calculates the distance from a point (x, y) to the line defined by two points (x1, y1) and (x2, y2).
	     *
	     * @param x the x-coordinate of the point
	     * @param y the y-coordinate of the point
	     * @param x1 the x-coordinate of the first endpoint of the line segment
	     * @param y1 the y-coordinate of the first endpoint of the line segment
	     * @param x2 the x-coordinate of the second endpoint of the line segment
	     * @param y2 the y-coordinate of the second endpoint of the line segment
	     * @return the distance from the point to the line
	     */

		private double distanceToLine(double x, double y, double x1, double y1, double x2, double y2) {
			// Calculate distance from point (x, y) to the line defined by (x1, y1) and (x2,
			// y2)
			double numerator = Math.abs((y2 - y1) * x - (x2 - x1) * y + x2 * y1 - y2 * x1);
			double denominator = Math.sqrt(Math.pow(y2 - y1, 2) + Math.pow(x2 - x1, 2));
			return numerator / denominator;
		}
		
		/**
	     * Calculates the collision normal for a line segment defined by two points (x1, y1) and (x2, y2).
	     *
	     * @param x1 the x-coordinate of the first endpoint of the line segment
	     * @param y1 the y-coordinate of the first endpoint of the line segment
	     * @param x2 the x-coordinate of the second endpoint of the line segment
	     * @param y2 the y-coordinate of the second endpoint of the line segment
	     * @return a {@code Point} representing the collision normal
	     */

		private Point calculateCollisionNormal(int x1, int y1, int x2, int y2) {
			if (x1 == x2) { // Vertical line
				return new Point(1, 0); // Positive x direction
			} else if (y1 == y2) { // Horizontal line
				return new Point(0, 1); // Positive y direction
			} else {
				// Handle other cases as needed
				return new Point(0, 0); // Default to no collision normal
			}
		}
	}
	
	/**
	 * Handles collisions between the ball and a given polygon.
	 * Checks if any of the vertices of the ball intersects with the given polygon,
	 * and if so, reflects the ball's velocity based on the collision normal.
	 * Additionally, if the collided polygon is a Slider, it rotates the slider randomly.
	 *
	 * @param polygon the polygon to check collision with
	 * @return true if a collision occurs, false otherwise
	 */
	public boolean handleCollisions(Polygon polygon) {
		Point[] ballPoints = this.getPoints();

		// Check collision with each point of the ball
		for (Point ballPoint : ballPoints) {
			if (polygon.contains(ballPoint)) {
				// Calculate collision normal
				Point[] polyPoints = polygon.getPoints();
				for (int i = 0; i < polyPoints.length; i++) {
					Point p1 = polyPoints[i];
					Point p2 = polyPoints[(i + 1) % polyPoints.length];

					if (doIntersect(p1, p2, ballPoint, this.position)) {
						// Reflect velocity based on collision normal
						double nx = p2.y - p1.y;
						double ny = p1.x - p2.x;

						// Normalize collision normal
						double length = Math.sqrt(nx * nx + ny * ny);
						nx /= length;
						ny /= length;

						double dotProduct = this.velocity.x * nx + this.velocity.y * ny;
						this.velocity.x -= 2 * dotProduct * nx;
						this.velocity.y -= 2 * dotProduct * ny;

						return true;
					}
				}
			}
		}

		return false;
	}
	
	
	/**
	 * Checks if two line segments intersect.
	 *
	 * @param p1 the first endpoint of the first line segment
	 * @param q1 the second endpoint of the first line segment
	 * @param p2 the first endpoint of the second line segment
	 * @param q2 the second endpoint of the second line segment
	 * @return true if the line segments intersect, false otherwise
	 */

	// Helper method to check if two line segments intersect
	private boolean doIntersect(Point p1, Point q1, Point p2, Point q2) {
		int o1 = orientation(p1, q1, p2);
		int o2 = orientation(p1, q1, q2);
		int o3 = orientation(p2, q2, p1);
		int o4 = orientation(p2, q2, q1);

		// General case
		if (o1 != o2 && o3 != o4)
			return true;

		// Special Cases
		if (o1 == 0 && onSegment(p1, p2, q1))
			return true;
		if (o2 == 0 && onSegment(p1, q2, q1))
			return true;
		if (o3 == 0 && onSegment(p2, p1, q2))
			return true;
		if (o4 == 0 && onSegment(p2, q1, q2))
			return true;

		return false; // Doesn't fall in any of the above cases
	}

	/**
	 * Determines the orientation of an ordered triplet of points (p, q, r).
	 * This method calculates whether the points are collinear, clockwise, or counterclockwise.
	 *
	 * @param p The first point in the triplet.
	 * @param q The second point in the triplet.
	 * @param r The third point in the triplet.
	 * @return An integer representing the orientation of the points:
	 *         - 0 if the points are collinear.
	 *         - 1 if the points are in a clockwise orientation.
	 *         - 2 if the points are in a counterclockwise orientation.
	 */
	private int orientation(Point p, Point q, Point r) {
		double val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);
		if (val == 0)
			return 0; // Collinear
		return (val > 0) ? 1 : 2; // Clockwise or Counterclockwise
	}

	/**
	 * Checks if a point q lies on the line segment 'pr'.
	 *
	 * @param p One endpoint of the line segment.
	 * @param q The point to be checked.
	 * @param r Another endpoint of the line segment.
	 * @return {@code true} if the point q lies on the line segment 'pr', {@code false} otherwise.
	 */
	private boolean onSegment(Point p, Point q, Point r) {
		return q.x <= Math.max(p.x, r.x) && q.x >= Math.min(p.x, r.x) && q.y <= Math.max(p.y, r.y)
				&& q.y >= Math.min(p.y, r.y);
	}

}
