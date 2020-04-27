package com.newardassociates.tictactoe;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Basically just a screen that displays a graphic for a few
 * seconds, and then shifts over to the next screen for
 * actual interaction.
 */
public class SplashScreen extends ScreenAdapter{
    private Game game;
    private float pauseTime = 0.0f;
    private SpriteBatch batch;
    private Texture logo;
    private Sprite logoSprite;

    private static final String TAG = SplashScreen.class.getSimpleName();

    public SplashScreen(Game game) {
        this.game = game;
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
        logoSprite.setSize(width, height);
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        logo = new Texture(Gdx.files.internal("badlogic.jpg"));
        logoSprite = new Sprite(logo);
    }

    @Override
    public void render(float delta) {
        Gdx.app.log(TAG, "pauseTime = " + pauseTime);

        Gdx.gl.glClearColor(Color.RED.r, Color.RED.g, Color.RED.b, Color.RED.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        logoSprite.draw(batch);
        batch.end();

        pauseTime += delta;
        if (pauseTime > 5.0f) {
            game.setScreen(new GameScreen(game));
        }
    }

    @Override
    public void hide() {
        logo.dispose();
        batch.dispose();
    }
}