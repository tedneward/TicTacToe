package com.newardassociates.tictactoe

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch

/**
 * Basically just a screen that displays a graphic for a few
 * seconds, and then shifts over to the next screen for
 * actual interaction.
 */
class SplashScreen(private val game: Game) : ScreenAdapter() {
    private var pauseTime = 0.0f
    private var batch: SpriteBatch? = null
    private var logo: Texture? = null
    private var logoSprite: Sprite? = null

    /**
     * Called when the window resizes (which I think can only happen
     * on Desktop and WebGL applications...?).
     */
    override fun resize(width: Int, height: Int) {
        Gdx.app.log(TAG, "Resizing to " + width + "x" + height)

        // It appears that resize() gets called before we do anything,
        // so we can capture width and height here
        logoSprite!!.setSize(width.toFloat(), height.toFloat())
    }

    override fun show() {
        batch = SpriteBatch()
        logo = Texture(Gdx.files.internal("badlogic.jpg"))
        logoSprite = Sprite(logo)
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(Color.RED.r, Color.RED.g, Color.RED.b, Color.RED.a)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        batch!!.begin()
        logoSprite!!.draw(batch)
        batch!!.end()
        pauseTime += delta
        if (pauseTime > PAUSE) {
            game.screen = GameScreen(game)
        }
    }

    override fun hide() {
        logo!!.dispose()
        batch!!.dispose()
    }

    companion object {
        private const val PAUSE = 2.0f // 5.0f;
        private val TAG = SplashScreen::class.java.simpleName
    }

}