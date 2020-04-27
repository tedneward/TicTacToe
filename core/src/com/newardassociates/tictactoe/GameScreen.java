package com.newardassociates.tictactoe;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static com.badlogic.gdx.graphics.Color.*;

public class GameScreen extends ScreenAdapter {
	private Game game;
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

	private static final String TAG = GameScreen.class.getSimpleName();

	private SpriteBatch batch;
	private OrthographicCamera camera;
	
	public GameScreen(Game game) {
		this.game = game;
	}

	/**
	 * This is basically the constructor for the Screen, it seems like.
	 */
    @Override
    public void show() {
		Gdx.app.log(TAG, "Showing");

		batch = new SpriteBatch();

		// If a screen can be shown/hidden in a pair without necessarily
		// wanting to start a new game, we need to move this construction
		// to the GameScreen constructor, and not here. Otherwise, board
		// state is implicitly reset every time we move between screens.
		board = new Board();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, width, height);

		boardImg = new Texture(Gdx.files.internal("tictactoe.jpg"));
		boardSprite = new Sprite(boardImg);

		// We need Textures for each spot on the board
		// 2 players, 3x3 board = 9 cells
		// 2 players, 4x4 board = 16 cells
		// 2 players, 5x5 board = 25 cells
		// and so on
		cross = new Texture[5];
		crossSprites = new Sprite[5];
		for (int i=0; i<5; i++) {
			cross[i] = new Texture(Gdx.files.internal("cross.png"));
			crossSprites[i] = new Sprite(cross[i]);
		}
		circle = new Texture[4];
		circleSprites = new Sprite[4];
		for (int i=0; i<4; i++) {
			circle[i] = new Texture(Gdx.files.internal("circle.png"));
			circleSprites[i] = new Sprite(circle[i]);
		}
	}

	public void resolveClick(int x, int y) {
		int col = getColumnFromXCoord(x);
		int row = getRowFromYCoord(y);

		// Check if this is already taken
		if (board.get(col, row) == 0) {
			board.play(col, row);
			Gdx.app.log(TAG, board.toString());
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
	
	@Override
	public void pause() {
		Gdx.app.log(TAG, "Pausing");
	}
	@Override
	public void resume() {
		Gdx.app.log(TAG, "Resuming");
	}

	/**
	 * Called when the window resizes (which I think can only happen
	 * on Desktop and WebGL applications...?).
	 */
	@Override
	public void resize(int width, int height) {
		Gdx.app.log(TAG, "Resizing to " + width + "x" + height);

		// It appears that resize() gets called before we do anything,
		// so we can capture width and height here
		this.width = width;
		this.height = height;
		boardSprite.setSize(width, height);

		// Resize the cell locations
		colXs = new int[3 + 1];
		for (int i=0; i<3 + 1; i++)
			colXs[i] = Math.round(width/3) * i;
		rowYs = new int[3 + 1];
		for (int i=0; i<3 + 1; i++)
			rowYs[i] = Math.round(height/3) * i;
	}

	/**
	 * Called once every loop cycle
	 */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(BLACK.r, BLACK.g, BLACK.b, BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// handle mouse click and resolve the result
		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
			int x = Gdx.input.getX();
			int y = Gdx.input.getY();

			resolveClick(x, y);
		}

		batch.begin();
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
		camera.update();
		batch.end();

		if (board.state() == Board.State.COMPLETE) {
			// Temporary end-of-game response
			Gdx.app.log("TicTacToeGame", "GAME OVER: " + board.winner());
			Gdx.app.exit();
		}
	}
	// invert() is used because in libgdx, drawing coordinates
	// begin in the lower-left corner
	private int invert(int y) {
		return Gdx.graphics.getHeight() - 1 - y;
	}
	
	/**
	 * Called when this screen is no longer the current screen for a Game.
	 */
	@Override
	public void hide() {
		Gdx.app.log(TAG, "Hiding");
	}

	@Override
	public void dispose () {
		Gdx.app.log(TAG, "Disposing");

		batch.dispose();
		boardImg.dispose();
		for (int i=0; i<5; i++)
			if (cross[i] != null) 
				cross[i].dispose();
		for (int i=0; i<4; i++) 
			if (circle[i] != null)
				circle[i].dispose();
	}
}
