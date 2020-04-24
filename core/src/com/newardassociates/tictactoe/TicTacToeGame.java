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
			for (int y = 0; y < 2; y++)
			{
				boolean complete = true;
				for (int x = 0; x < 2; x++) {
					if (get(x, y) != p) {
						complete = false;
						break;
					}
				}
				if (complete)
					return p;
			}

			// check vertical
			for (int x = 0; x < 2; x++)
			{
				boolean complete = true;
				for (int y = 0; y < 2; y++) {
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
				for (int d=0; d < 2; d++) {
					if (get(d, d) != p) {
						complete = false;
						break;
					}
				}	
				if (complete)
					return p;

				complete = true;
				for (int d=0; d < 2; d++) {
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
		for (int x=0; x<2; x++)
			for (int y=0; y<2; y++)
				if (get(x,y) == 0)
					filled = false;
		if (filled)
			return -1;

		// we're still going, then
		return 0;
	}
}

class BoardUI {
	private Board board;
	private Texture boardImg;
	private Sprite boardSprite;
	private Texture cross[];
	private Texture circle[];

	public int width;
	public int height;

	public BoardUI(Board b) {
		board = b;

		boardImg = new Texture("tictactoe.jpg");
		boardSprite = new Sprite(boardImg);
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		boardSprite.setSize(width, height);

		// We need Textures for each spot on the board
		cross = new Texture[5];
		for (int i=0; i<5; i++)
			cross[0] = new Texture("cross.png");
		circle = new Texture[4];
		for (int i=0; i<4; i++)
			circle[i] = new Texture("circle.png");
	}

	public void render(SpriteBatch batch) {
		boardSprite.draw(batch);

		// For each square in the board, draw the appropriate Texture

	}

	public void dispose() {
		boardImg.dispose();
		for (int i=0; i<5; i++) cross[i].dispose();
		for (int i=0; i<4; i++) circle[i].dispose();
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

		batch.begin();
		boardUI.render(batch);
		batch.end();

		// handle mouse click and resolve the result

	}
	
	@Override
	public void dispose () {
		batch.dispose();
		boardUI.dispose();
	}
}
