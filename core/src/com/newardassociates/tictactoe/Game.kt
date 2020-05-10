package com.newardassociates.tictactoe

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport

class GameScreen(private val game: Game) : ScreenAdapter() {
    private var board: Board? = null
    private var boardImg: Texture? = null
    private var boardSprite: Sprite? = null
    private var cross: Array<Texture?> = arrayOf()
    private var circle: Array<Texture?> = arrayOf()
    private var crossSprites: Array<Sprite?> = arrayOf()
    private var circleSprites: Array<Sprite?> = arrayOf()
    private var bitmapFont: BitmapFont? = null
    private var batch: SpriteBatch? = null
    private var camera: OrthographicCamera? = null
    private var viewport: Viewport? = null
    private var width = 0
    private var height = 0

    // These are the respective X or Y values for the "edge" they
    // represent; calculated once and cached
    private var colXs: IntArray = IntArray(board!!.numCols + 1)
    private var rowYs: IntArray = IntArray(board!!.numRows + 1)
    private var gameOverTime = 0.0f

    /**
     * Called when the Screen is being displayed (after not being seen).
     * Acts as something of a libGdx constructor (as opposed to a Java object
     * constructor).
     */
    override fun show() {
        Gdx.app.log(TAG, "Showing")
        batch = SpriteBatch()

        // If a screen can be shown/hidden in a pair without necessarily
        // wanting to start a new game, we need to move this construction
        // to the GameScreen constructor, and not here. Otherwise, board
        // state is implicitly reset every time we move between screens.
        board = Board()
        camera = OrthographicCamera()
        camera!!.position[320f, 240f] = 0f
        camera!!.update()
        viewport = FitViewport(640f, 480f, camera)
        bitmapFont = BitmapFont()
        boardImg = Texture(Gdx.files.internal("tictactoe.jpg"))
        boardSprite = Sprite(boardImg)

        // We need Textures for each spot on the board
        // 2 players, 3x3 board = 9 cells
        // 2 players, 4x4 board = 16 cells
        // 2 players, 5x5 board = 25 cells
        // and so on
        val numP1Cells = board!!.numRows * board!!.numCols / 2 + 1

        // The +1 here is to account for odd numbers of total cells;
        // in an even-numbered side, it means one extra cell will be
        // allocated to player 1. Meh. Could fix that by doing a
        // modulo expression nonzero test, but... meh.
        cross = arrayOfNulls(numP1Cells)
        crossSprites = arrayOfNulls(numP1Cells)
        for (i in 0 until numP1Cells) {
            cross[i] = Texture(Gdx.files.internal("cross.png"))
            crossSprites[i] = Sprite(cross[i])
        }
        val numP2Cells = board!!.numRows * board!!.numCols / 2
        circle = arrayOfNulls(numP2Cells)
        circleSprites = arrayOfNulls(numP2Cells)
        for (i in 0 until numP2Cells) {
            circle[i] = Texture(Gdx.files.internal("circle.png"))
            circleSprites[i] = Sprite(circle[i])
        }
    }

    fun resolveClick(x: Int, y: Int) {
        val col = getColumnFromXCoord(x)
        val row = getRowFromYCoord(y)

        // Check if this is already taken
        if (board!![col, row] == 0) {
            board!!.play(col, row)
            Gdx.app.log(TAG, board.toString())
        }
    }

    private fun getColumnFromXCoord(x: Int): Int {
        // If we get to the last column, there's
        // no comparison that needs to be made
        for (i in 0 until 3 - 1) {
            if (x < colXs[i + 1]) return i
        }
        return 2
    }

    private fun getRowFromYCoord(y: Int): Int {
        // If we get to the last row, there's
        // no comparison that needs to be made
        for (i in 0 until 3 - 1) {
            if (y < rowYs[i + 1]) return i
        }
        return 2
    }

    override fun pause() {
        Gdx.app.log(TAG, "Pausing")
    }

    override fun resume() {
        Gdx.app.log(TAG, "Resuming")
    }

    /**
     * Called when the window resizes (which I think can only happen
     * on Desktop and WebGL applications...?).
     */
    override fun resize(width: Int, height: Int) {
        Gdx.app.log(TAG, "Resizing to " + width + "x" + height)

        // It appears that resize() gets called before we do anything,
        // so we can capture width and height here
        this.width = width
        this.height = height
        viewport!!.update(width, height)
        camera!!.setToOrtho(false, width.toFloat(), height.toFloat()) // ??? necessary ???
        boardSprite!!.setSize(width.toFloat(), height.toFloat())

        // Resize the cell locations
        colXs = IntArray(board!!.numCols + 1)
        for (i in 0 until board!!.numCols + 1) colXs[i] = Math.round(width / board!!.numCols.toFloat()) * i
        rowYs = IntArray(board!!.numRows + 1)
        for (i in 0 until board!!.numRows + 1) rowYs[i] = Math.round(height / board!!.numRows.toFloat()) * i
    }

    /**
     * Called once every loop cycle
     */
    override fun render(delta: Float) {
        camera!!.update()
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        batch!!.begin()
        boardSprite!!.draw(batch)
        renderBoard(batch)
        if (board!!.state() == Board.State.COMPLETE) {
            renderGameOver(batch)

            // Pause for two seconds, then look for touch or click input
            gameOverTime += delta
            Gdx.app.log(TAG, "gameOverTime = $gameOverTime")
            if (gameOverTime > GAME_OVER_DISPLAY_TIME) {
                gameOverTime = 0.0f
                board!!.reset()
            }
        } else {
            // handle mouse click and resolve the result
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                resolveClick(Gdx.input.x, Gdx.input.y)
            }
        }
        batch!!.end()
    }

    private fun renderBoard(batch: SpriteBatch?) {
        // For each square in the board, draw the appropriate Sprite
        // NOTE: Screen coords are 0,0 from lower-left
        var crossCount = 0
        var circleCount = 0
        for (x in 0 until board!!.numCols) {
            for (y in 0 until board!!.numRows) {
                // in libgdx, drawing coordinates begin in the lower-left corner
                val yInverted = Gdx.graphics.height - 1 - rowYs[y + 1]
                if (board!![x, y] == 1) {
                    crossSprites[crossCount]!!.setPosition(colXs[x].toFloat(), yInverted.toFloat())
                    crossSprites[crossCount]!!.draw(batch)
                    crossCount++
                } else if (board!![x, y] == 2) {
                    circleSprites[circleCount]!!.setPosition(colXs[x].toFloat(), yInverted.toFloat())
                    circleSprites[circleCount]!!.draw(batch)
                    circleCount++
                }
            }
        }
    }

    private fun renderGameOver(batch: SpriteBatch?) {
        var text = "GAME OVER -- "
        when (board!!.winner()) {
            -1 -> text = "$text DRAW"
            1 -> text = "$text PLAYER 1 WINS!"
            2 -> text = "$text PLAYER 2 WINS!"
        }
        val layout = GlyphLayout(bitmapFont, text)
        if (board!!.winner() == -1) {
            bitmapFont!!.color = Color.BROWN
        } else {
            bitmapFont!!.color = Color.GREEN
        }
        bitmapFont!!.draw(batch, text, (width - layout.width) / 2,
                (height - layout.height) / 2)
    }

    /**
     * Called when this screen is no longer the current screen for a Game.
     */
    override fun hide() {
        Gdx.app.log(TAG, "Hiding")
        batch!!.dispose()
        boardImg!!.dispose()
        bitmapFont!!.dispose()
        for (i in cross.indices) if (cross[i] != null) cross[i]!!.dispose()
        for (i in circle.indices) if (circle[i] != null) circle[i]!!.dispose()

        // NOTE: We don't need to dispose() crossSprites or circleSprites;
        // largely because they don't have dispose() methods
    }

    override fun dispose() {
        Gdx.app.log(TAG, "Disposing")
    }

    companion object {
        private const val GAME_OVER_DISPLAY_TIME = 2.0f
        private val TAG = GameScreen::class.java.simpleName
    }

}