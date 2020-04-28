package com.newardassociates.tictactoe;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

/**
 * TODO: Show a splash screen, then move to a main
 * menu screen, where the player can choose from
 * a couple of options (New Game, History, Credits).
 * New Game takes us to the MainScreen to play the
 * actual game. History shows the running total of
 * wins, losses, draws. Credits puts up a wall of
 * text with copyrights, etc. Mostly all of this is
 * an exercise with moving/switching between screens.
 */
public class TicTacToeGame extends Game {
	private static final String TAG = TicTacToeGame.class.getSimpleName();

	@Override
	public void create () {
		Gdx.app.log(TAG, "This is running as a " + Gdx.app.getType() + " application");
		Gdx.app.log(TAG, "Version: " + Gdx.app.getVersion());
		Gdx.app.log(TAG, "JavaHeap: " + Gdx.app.getJavaHeap());
		Gdx.app.log(TAG, "NativeHeap: " + Gdx.app.getNativeHeap());

		setScreen(new SplashScreen(this));
	}
}
