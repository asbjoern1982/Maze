package dk.ninjabear.maze;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Cell {
	private int x, y;
	private double w;
	private boolean visited;
	private boolean drawTop, drawRight, drawBottom, drawLeft;
	
	public Cell(int x, int y, double width) {
		this.x = x; this.y = y; w = width;
		visited = false;
		drawTop = true; drawRight = true; drawBottom = true; drawLeft = true;
	}
	
	public boolean getVisited() {return visited;}
	public void setVisited() {visited = true;}
	public int getX() {return x;}
	public int getY() {return y;}
	
	public void clearSide(Side side) {
		if (side == Side.TOP) drawTop = false;
		else if (side == Side.RIGHT) drawRight = false;
		else if (side == Side.BOTTOM) drawBottom = false;
		else if (side == Side.LEFT) drawLeft = false;
	}
	
	public void draw(GraphicsContext gc) {
		// draw the background
		if (visited) gc.setFill(Color.WHITE);
		else gc.setFill(Color.LIGHTGRAY);
		gc.fillRect(x*w, y*w, w, w);

		// draw the walls
		if (drawTop) 	gc.strokeLine(x*w, 	 y*w, 	x*w+w, y*w);
		if (drawRight) 	gc.strokeLine(x*w+w, y*w, 	x*w+w, y*w+w);
		if (drawBottom) gc.strokeLine(x*w, 	 y*w+w, x*w+w, y*w+w);
		if (drawLeft)	gc.strokeLine(x*w, 	 y*w, 	x*w,   y*w+w);
	}
}
