package com.newardassociates.tictactoe

import com.badlogic.gdx.Game
import com.badlogic.gdx.ScreenAdapter

/**
 * This class does a rolling text animation of the various copyrights
 * and credits, and maybe plays some audio in the background, until
 * the player offers us any input whatsoever, at which point we return
 * to the MainMenuScreen.
 */
class CreditsScreen(private val game: Game) : ScreenAdapter() {
    private val credits = """
        Tic-Tac-Toe v0.1
        by Ted Neward
        Copyright (c) 2020
        This application uses libgdx, found at
        http://www.libgdx.com
        """.trimIndent()

    override fun show() {}
    override fun resize(width: Int, height: Int) {}
    override fun render(delta: Float) {}

}