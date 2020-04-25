package com.newardassociates.tictactoe;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
/**
 * Basic representation of a TTT game board.
 * 
 * Later, add flexible sizing (4x4, 5x5, etc).
 */
class Board {
	private int[][] board;
	private int currentPlayer = 1; // start with X

	/**
	 * COMPLETE: we have a victory or all squares are filled
	 * INCOMPLETE: not complete
	 */
	public enum State {
		COMPLETE, INCOMPLETE
	}

	/**
	 * Construct a 3x3 game board
	 */
	public Board() {
		reset(3);
	}

	/**
	 * Clear out a Board and start from scratch
	 */
	public void reset(int size) {
		board = new int[size][];
		for (int i=0; i<size; i++)
			board[i] = new int[size];
	}

	/**
	 * Return player number in that spot, or 0
	 */
	public int get(int x, int y) {
		if (x < 0 || x > 2 || y < 0 || y > 2)
			throw new IllegalArgumentException("Out of board range");

			return board[x][y];
	}
	/**
	 * Set player number in that spot
	 */
	void set(int x, int y, int player) {
		if (x < 0 || x > 2 || y < 0 || y > 2)
			throw new IllegalArgumentException("Out of board range");

		if (player < 1 || player > 2)
			throw new IllegalArgumentException("How many players you got, anyway?!?");

		board[x][y] = player;
	}

	/**
	 * Player makes a move; this changes the current player setting
	 */
	public void play(int x, int y) {
		set(x, y, currentPlayer);
		// flip between players
		currentPlayer = (currentPlayer == 1) ? 2 : 1;
	}
	public int currentPlayer() {
		return currentPlayer;
	}

	public State state() {
		if (winner() != 0)
			return State.COMPLETE;

		return State.INCOMPLETE;
	}

	public int winner() {
		// check for rows for each player
		for (int p=1; p<=2; p++)
		{
			// check horizontal rows
			for (int y = 0; y < 3; y++)
			{
				boolean complete = true;
				for (int x = 0; x < 3; x++) {
					if (get(x, y) != p) {
						complete = false;
						break;
					}
				}
				if (complete)
					return p;
			}

			// check vertical
			for (int x = 0; x < 3; x++)
			{
				boolean complete = true;
				for (int y = 0; y < 3; y++) {
					if (get(x, y) != p) {
						complete = false;
						break;
					}
				}
				if (complete)
					return p;
			}

			// check diagonals
			{
				boolean complete = true;
				for (int d=0; d < 3; d++) {
					if (get(d, d) != p) {
						complete = false;
						break;
					}
				}	
				if (complete)
					return p;

				complete = true;
				for (int d=0; d < 3; d++) {
					if (get(d, (2 - d)) != p) {
						complete = false;
						break;
					}
				}
				if (complete)
					return p;
			}
		}

		// check if all squares are filled; all squares filled
		// and no winner (checked above) means we have a draw
		boolean filled = true;
		for (int x=0; x<3; x++)
			for (int y=0; y<3; y++)
				if (get(x,y) == 0)
					filled = false;
		if (filled)
			return -1;

		// we're still going, then
		return 0;
	}

	@Override
	public String toString() {
		return "BOARD: [ [" + get(0,0) + "]  [" + get(1, 0) + "]  [" + get(2, 0) + "]\n" +
			"         [" + get(0,1) + "]  [" + get(1, 1) + "]  [" + get(2, 1) + "]\n" +
			"         [" + get(0,2) + "]  [" + get(1, 2) + "]  [" + get(2, 2) + "]  ]";
	} 
}

class BoardUI {
	private Board board;
	private Texture boardImg;
	private Sprite boardSprite;
	private Texture cross[];
	private Texture circle[];
	private Sprite crossSprites[];
	private Sprite circleSprites[];

	public int width;
	public int height;
	// These are the respective X or Y values for the "edge" they
	// represent; calculated once and cached
	public int colXs[];
	public int rowYs[];

	public BoardUI(Board b) {
		board = b;

		boardImg = new Texture("tictactoe.jpg");
		boardSprite = new Sprite(boardImg);
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		boardSprite.setSize(width, height);

		// We need Textures for each spot on the board
		cross = new Texture[5];
		crossSprites = new Sprite[5];
		for (int i=0; i<5; i++) {
			cross[i] = new Texture("cross.png");
			crossSprites[i] = new Sprite(cross[i]);
		}
		circle = new Texture[4];
		circleSprites = new Sprite[4];
		for (int i=0; i<4; i++) {
			circle[i] = new Texture("circle.png");
			circleSprites[i] = new Sprite(circle[i]);
		}

		colXs = new int[3 + 1];
		for (int i=0; i<3 + 1; i++)
			colXs[i] = Math.round(width/3) * i;
		rowYs = new int[3 + 1];
		for (int i=0; i<3 + 1; i++)
			rowYs[i] = Math.round(height/3) * i;

		Gdx.app.log("UI", "Coords will be: ");
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				Gdx.app.log("UI", "Drawing [" + x + "," + y +  "] at " + colXs[x] + "x" + invert(rowYs[y+1]));
			}
		}
	}

	public void render(SpriteBatch batch) {
		boardSprite.draw(batch);

		// For each square in the board, draw the appropriate Sprite
		// NOTE: Screen coords are 0,0 from lower-left 
		int crossCount = 0;
		int circleCount = 0;
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				if (board.get(x,y) == 1) {
					crossSprites[crossCount].setPosition(colXs[x], invert(rowYs[y+1]));
					crossSprites[crossCount].draw(batch);
					crossCount++;
				}
				else if (board.get(x,y) == 2) {
					circleSprites[circleCount].setPosition(colXs[x], invert(rowYs[y+1]));
					circleSprites[circleCount].draw(batch);
					circleCount++;
				}
			}
		}
	}	
	private int invert(int y) {
		return Gdx.graphics.getHeight() - 1 - y;
	}

	public void resolveClick(int x, int y) {
		int col = getColumnFromXCoord(x);
		int row = getRowFromYCoord(y);

		// Check if this is already taken
		if (board.get(col, row) == 0) {
			board.play(col, row);
			Gdx.app.log("UI", board.toString());
		}
	}
	private int getColumnFromXCoord(int x) {
		// If we get to the last column, there's
		// no comparison that needs to be made
		for (int i=0; i<3 - 1; i++) {
			if (x < colXs[i+1])
				return i;
		}

		return 2;
	}
	private int getRowFromYCoord(int y) {
		// If we get to the last row, there's
		// no comparison that needs to be made
		for (int i=0; i<3 - 1; i++) {
			if (y < rowYs[i+1])
				return i;
		}

		return 2;
	}

	public void dispose() {
		boardImg.dispose();
		for (int i=0; i<5; i++)
			if (cross[i] != null) 
				cross[i].dispose();
		for (int i=0; i<4; i++) 
			if (circle[i] != null)
				circle[i].dispose();
	}
}

public class TicTacToeGame extends ApplicationAdapter {
	SpriteBatch batch;
	Board board;
	BoardUI boardUI;
	OrthographicCamera camera;
	
	@Override
	public void create () {
		batch = new SpriteBatch();

		board = new Board();
		boardUI = new BoardUI(board);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, boardUI.width, boardUI.height);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// handle mouse click and resolve the result
		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
			int x = Gdx.input.getX();
			int y = Gdx.input.getY();

			boardUI.resolveClick(x, y);
		}

		batch.begin();
		boardUI.render(batch);
		camera.update();
		batch.end();

		if (board.state() == Board.State.COMPLETE) {
			// Temporary end-of-game response
			Gdx.app.log("TicTacToeGame", "GAME OVER: " + board.winner());
			Gdx.app.exit();
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		boardUI.dispose();
	}
}
