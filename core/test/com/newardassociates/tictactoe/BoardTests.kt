package com.newardassociates.tictactoe

import org.junit.Assert
import org.junit.Test

class BoardTest {
    @Test
    fun constructBoard() {
        val b = Board()

        // Nobody should have any squares yet
        Assert.assertEquals(b[0, 0].toLong(), 0)
        Assert.assertEquals(b[0, 1].toLong(), 0)
        Assert.assertEquals(b[0, 2].toLong(), 0)
        Assert.assertEquals(b[1, 0].toLong(), 0)
        Assert.assertEquals(b[1, 1].toLong(), 0)
        Assert.assertEquals(b[1, 2].toLong(), 0)
        Assert.assertEquals(b[2, 0].toLong(), 0)
        Assert.assertEquals(b[2, 1].toLong(), 0)
        Assert.assertEquals(b[2, 2].toLong(), 0)
    }

    @Test
    fun setASquare() {
        val b = Board()

        // X (player 1) takes the center square
        b[1, 1] = 1
        Assert.assertEquals(b[1, 1].toLong(), 1)
    }

    @Test
    fun checkForVictoryNo() {
        val b = Board()

        // X (player 1) takes the center square
        b[1, 1] = 1
        Assert.assertEquals(b.state(), Board.State.INCOMPLETE)
    }

    @Test
    fun testRowWin() {
        val b = Board()

        // X (player 1) takes the center row
        b[1, 1] = 1
        b[0, 1] = 1
        b[2, 1] = 1
        Assert.assertEquals(b.state(), Board.State.COMPLETE)
        Assert.assertEquals(b.winner().toLong(), 1)
    }

    @Test
    fun testPlayer2Win() {
        val b = Board()

        // O (player 2) takes the center row
        b[1, 1] = 2
        b[0, 1] = 2
        b[2, 1] = 2
        Assert.assertEquals(b.state(), Board.State.COMPLETE)
        Assert.assertEquals(b.winner().toLong(), 2)
    }

    @Test
    fun testColumnWin() {
        val b = Board()
        b[0, 0] = 1
        b[0, 1] = 1
        b[0, 2] = 1
        Assert.assertEquals(b.state(), Board.State.COMPLETE)
        Assert.assertEquals(b.winner().toLong(), 1)
    }

    @Test
    fun testBackslashDiagonalWin() {
        val b = Board()
        b[2, 0] = 1
        b[1, 1] = 1
        b[0, 2] = 1
        Assert.assertEquals(b.state(), Board.State.COMPLETE)
        Assert.assertEquals(b.winner().toLong(), 1)
    }

    @Test
    fun testSlashDiagonalWin() {
        val b = Board()
        b[0, 0] = 1
        b[1, 1] = 1
        b[2, 2] = 1
        Assert.assertEquals(b.state(), Board.State.COMPLETE)
        Assert.assertEquals(b.winner().toLong(), 1)
    }

    @Test
    fun twoInARowAndNotWin() {
        val b = Board()
        b[0, 1] = 1
        b[1, 1] = 1
        Assert.assertEquals(b.state(), Board.State.INCOMPLETE)
    }

    @Test
    fun twoInAColAndNotWin() {
        val b = Board()
        b[0, 0] = 1
        b[1, 0] = 1
        Assert.assertEquals(b.state(), Board.State.INCOMPLETE)
    } // TODO: invalid inputs to get/set
    // TODO: draw
    // TODO: game reset
}