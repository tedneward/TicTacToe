package com.newardassociates.tictactoe

import org.junit.Assert
import org.junit.Test

class PlayTest {
    @Test
    fun playGame1Wins() {
        val b = Board()
        b.play(1, 1) // X center-center
        Assert.assertEquals(b.state(), Board.State.INCOMPLETE)
        b.play(0, 0) // O upper-left
        Assert.assertEquals(b.state(), Board.State.INCOMPLETE)
        b.play(1, 0) // X upper-center
        Assert.assertEquals(b.state(), Board.State.INCOMPLETE)
        b.play(0, 1) // O mid-left
        Assert.assertEquals(b.state(), Board.State.INCOMPLETE)
        b.play(1, 2) // X lower-center WIN
        Assert.assertEquals(b.state(), Board.State.COMPLETE)
        Assert.assertEquals(b.winner().toLong(), 1) // X WIN
    }

    @Test
    fun playGameDraw() {
        val b = Board()
        b.play(0, 0) // X upper-left
        // [X] [ ] [ ]
        // [ ] [ ] [ ]
        // [ ] [ ] [ ]
        b.play(1, 0) // O upper-center
        // [X] [O] [ ]
        // [ ] [ ] [ ]
        // [ ] [ ] [ ]
        b.play(2, 0) // X upper-right
        // [X] [O] [X]
        // [ ] [ ] [ ]
        // [ ] [ ] [ ]
        b.play(1, 1) // O center-center
        // [X] [O] [X]
        // [ ] [O] [ ]
        // [ ] [ ] [ ]
        b.play(0, 1) // X mid-left
        // [X] [O] [X]
        // [X] [O] [ ]
        // [ ] [ ] [ ]
        b.play(0, 2) // O lower-left
        // [X] [O] [X]
        // [X] [O] [ ]
        // [O] [ ] [ ]
        b.play(1, 2) // X lower-center
        // [X] [O] [X]
        // [X] [O] [ ]
        // [O] [X] [ ]
        b.play(2, 1) // O center-mid
        // [X] [O] [X]
        // [X] [O] [O]
        // [O] [X] [ ]
        b.play(2, 2) // X lower-right
        // [X] [O] [X]
        // [X] [O] [O]
        // [O] [X] [X]
        Assert.assertEquals(b.state(), Board.State.COMPLETE)
        Assert.assertEquals(b.winner().toLong(), -1) // DRAW
    }
}