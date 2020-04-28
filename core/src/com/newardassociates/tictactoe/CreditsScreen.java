package com.newardassociates.tictactoe;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.ScreenAdapter;

/**
 * This class does a rolling text animation of the various copyrights
 * and credits, and maybe plays some audio in the background, until
 * the player offers us any input whatsoever, at which point we return
 * to the MainMenuScreen.
 */
public class CreditsScreen extends ScreenAdapter {
    private Game game;

    private String credits = 
"Tic-Tac-Toe v0.1\n" +
"by Ted Neward\n" +
"Copyright (c) 2020" +
"\n" +
"This application uses libgdx, found at\n" +
"http://www.libgdx.com";

    public CreditsScreen(Game game) { 
        this.game = game;
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {
        
    }

    @Override
    public void render(float delta) {

    }
}
