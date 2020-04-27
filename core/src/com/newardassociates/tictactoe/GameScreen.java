package com.newardassociates.tictactoe;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

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
	private BitmapFont bitmapFont;
	private SpriteBatch batch;
	private OrthographicCamera camera;

	private int width;
	private int height;

	// These are the respective X or Y values for the "edge" they
	// represent; calculated once and cached
	private int colXs[];
	private int rowYs[];

	private float gameOverTime = 0.0f;
	private static final float GAME_OVER_DISPLAY_TIME = 2.0f;

	private static final String TAG = GameScreen.class.getSimpleName();

	
	/**
	 * Constructor. Game parameter is to allow us to do Screen-to-Screen
	 * transitions. GameScreen, upon completion, takes us back to the
	 * MainMenuScreen. (Might want to think about a quick-new-game option.)
	 * 
	 * @param game
	 */
	public GameScreen(Game game) {
		this.game = game;
	}

	/**
	 * Called when the Screen is being displayed (after not being seen).
	 * Acts as something of a libGdx constructor (as opposed to a Java object
	 * constructor).
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

		bitmapFont = new BitmapFont();

		boardImg = new Texture(Gdx.files.internal("tictactoe.jpg"));
		boardSprite = new Sprite(boardImg);

		// We need Textures for each spot on the board
		// 2 players, 3x3 board = 9 cells
		// 2 players, 4x4 board = 16 cells
		// 2 players, 5x5 board = 25 cells
		// and so on
		int numP1Cells = ((board.numRows * board.numCols) / 2) + 1;
			// The +1 here is to account for odd numbers of total cells;
			// in an even-numbered side, it means one extra cell will be
			// allocated to player 1. Meh. Could fix that by doing a
			// modulo expression nonzero test, but... meh.
		cross = new Texture[numP1Cells];
		crossSprites = new Sprite[numP1Cells];
		for (int i=0; i<numP1Cells; i++) {
			cross[i] = new Texture(Gdx.files.internal("cross.png"));
			crossSprites[i] = new Sprite(cross[i]);
		}
		int numP2Cells = ((board.numRows * board.numCols) / 2);
		circle = new Texture[numP2Cells];
		circleSprites = new Sprite[numP2Cells];
		for (int i=0; i<numP2Cells; i++) {
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
		camera.setToOrtho(false, width, height);
		boardSprite.setSize(width, height);

		// Resize the cell locations
		colXs = new int[board.numCols + 1];
		for (int i=0; i<board.numCols + 1; i++)
			colXs[i] = Math.round(width/board.numCols) * i;
		rowYs = new int[board.numRows + 1];
		for (int i=0; i<board.numRows + 1; i++)
			rowYs[i] = Math.round(height/board.numRows) * i;
	}

	/**
	 * Called once every loop cycle
	 */
    @Override
    public void render(float delta) {
		camera.update();

		Gdx.gl.glClearColor(BLACK.r, BLACK.g, BLACK.b, BLACK.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		boardSprite.draw(batch);
		renderBoard(batch);
	
		if (board.state() == Board.State.COMPLETE) {
			renderGameOver(batch);

			// Pause for two seconds, then look for touch or click input
			gameOverTime += delta;
			Gdx.app.log(TAG, "gameOverTime = " + gameOverTime);
			if (gameOverTime > GAME_OVER_DISPLAY_TIME) {
				gameOverTime = 0.0f;
				board.reset();
			}
		}
		else {
			// handle mouse click and resolve the result
			if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
				resolveClick(Gdx.input.getX(), Gdx.input.getY());
			}
		}

		batch.end();
	}
	private void renderBoard(SpriteBatch batch) {
		// For each square in the board, draw the appropriate Sprite
		// NOTE: Screen coords are 0,0 from lower-left 
		int crossCount = 0;
		int circleCount = 0;
		for (int x = 0; x < board.numCols; x++) {
			for (int y = 0; y < board.numRows; y++) {
				// in libgdx, drawing coordinates begin in the lower-left corner
				int yInverted = Gdx.graphics.getHeight() - 1 - rowYs[y+1];

				if (board.get(x,y) == 1) {
					crossSprites[crossCount].setPosition(colXs[x], yInverted);
					crossSprites[crossCount].draw(batch);
					crossCount++;
				}
				else if (board.get(x,y) == 2) {
					circleSprites[circleCount].setPosition(colXs[x], yInverted);
					circleSprites[circleCount].draw(batch);
					circleCount++;
				}
			}
		}
	}
	private void renderGameOver(SpriteBatch batch) {
		String text = "GAME OVER -- ";
		switch (board.winner()) {
			case -1:
				text = text + " DRAW";
				break;
			case 1:
				text = text + " PLAYER 1 WINS!";
				break;
			case 2:
				text = text + " PLAYER 2 WINS!";
				break;
		}

		GlyphLayout layout = new GlyphLayout(bitmapFont, text);
		if (board.winner() == -1) {
			bitmapFont.setColor(Color.BROWN);
		}
		else {
			bitmapFont.setColor(Color.GREEN);
		}
		bitmapFont.draw(batch, text, (width - layout.width) / 2,
			(height - layout.height) / 2);
	}
	
	/**
	 * Called when this screen is no longer the current screen for a Game.
	 */
	@Override
	public void hide() {
		Gdx.app.log(TAG, "Hiding");

		batch.dispose();
		boardImg.dispose();
		bitmapFont.dispose();
		for (int i=0; i<cross.length; i++)
			if (cross[i] != null) 
				cross[i].dispose();
		for (int i=0; i<circle.length; i++) 
			if (circle[i] != null)
				circle[i].dispose();

		// NOTE: We don't need to dispose() crossSprites or circleSprites;
		// largely because they don't have dispose() methods
	}

	@Override
	public void dispose () {
		Gdx.app.log(TAG, "Disposing");
	}
}
