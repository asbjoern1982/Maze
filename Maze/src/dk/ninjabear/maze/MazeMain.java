package dk.ninjabear.maze;

import java.util.ArrayList;
import java.util.Stack;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MazeMain extends Application {

	public static void main(String[] args) {
		Application.launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Maze");
		Group root = new Group();
		initContent(root);
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}

	private void initContent(Group root) {
		Canvas canvas = new Canvas(600, 400);
		root.getChildren().add(canvas);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setStroke(Color.BLACK);
		double cellWidth = 20;
		Cell[][] cells = new Cell[(int)(canvas.getWidth()/cellWidth)][(int)(canvas.getHeight()/cellWidth)];
		for (int x = 0; x < canvas.getWidth()/cellWidth; x++)
			for (int y = 0; y < canvas.getHeight()/cellWidth; y++)
				cells[x][y] = new Cell(x, y, cellWidth);
		
		new AnimationTimer() {
			private Cell currentCell = cells[0][0];
			private Stack<Cell> visitedCells = new Stack<>();
			
			@Override
			public void handle(long now) {
				currentCell.setVisited();
				int cx = currentCell.getX();
				int cy = currentCell.getY();
				
				// make a list of legal neighbours cells that has not been visited yet
				ArrayList<Side> neighbours = new ArrayList<>();
				if (cy != 0 && !cells[cx][cy-1].getVisited())
					neighbours.add(Side.TOP);
				if (cx != (int)(canvas.getWidth()/cellWidth)-1 && !cells[cx+1][cy].getVisited())
					neighbours.add(Side.RIGHT);
				if (cy != (int)(canvas.getHeight()/cellWidth)-1 && !cells[cx][cy+1].getVisited())
					neighbours.add(Side.BOTTOM);
				if (cx != 0 && !cells[cx-1][cy].getVisited())
					neighbours.add(Side.LEFT);
				
				// if there are any, visit a random one
				if (neighbours.size() > 0) {
					Side direction = neighbours.get((int)(neighbours.size() * Math.random()));
					currentCell.clearSide(direction);
					
					if (direction == Side.TOP) {
						currentCell = cells[cx][cy-1];
						currentCell.clearSide(Side.BOTTOM);
					} else if (direction == Side.RIGHT) {
						currentCell = cells[cx+1][cy];
						currentCell.clearSide(Side.LEFT);
					} else if (direction == Side.BOTTOM) {
						currentCell = cells[cx][cy+1];
						currentCell.clearSide(Side.TOP);
					} else {
						currentCell = cells[cx-1][cy];
						currentCell.clearSide(Side.RIGHT);
					}
					//keep a stack of visited cells so it is possible to backtrack
					visitedCells.push(currentCell);
					
				// if all neighbours has been visited, go one back
				} else {
					if (!visitedCells.isEmpty())
						currentCell = visitedCells.pop();
					else {
						// reset!
						gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
						for (int x = 0; x < canvas.getWidth()/cellWidth; x++)
							for (int y = 0; y < canvas.getHeight()/cellWidth; y++) {
								cells[x][y] = new Cell(x, y, cellWidth);
							}
						currentCell = cells[0][0];
					}
				}
				
				// paint the updated maze
				gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
				for (int x = 0; x < canvas.getWidth()/cellWidth; x++)
					for (int y = 0; y < canvas.getHeight()/cellWidth; y++)
						cells[x][y].draw(gc);
			}
		}.start();
	}
}
