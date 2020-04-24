package com.newardassociates.tictactoe;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BoardTest {
    @Test
    public void constructBoard() {
        Board b = new Board();

        // Nobody should have any squares yet
        assertEquals(b.get(0,0), 0);
        assertEquals(b.get(0,1), 0);
        assertEquals(b.get(0,2), 0);
        assertEquals(b.get(1,0), 0);
        assertEquals(b.get(1,1), 0);
        assertEquals(b.get(1,2), 0);
        assertEquals(b.get(2,0), 0);
        assertEquals(b.get(2,1), 0);
        assertEquals(b.get(2,2), 0);
    }

    @Test
    public void playASquare() {
        Board b = new Board();

        // X (player 1) takes the center square
        b.set(1,1, 1);

        assertEquals(b.get(1,1), 1);
    }

    @Test
    public void checkForVictoryNo() {
        Board b = new Board();

        // X (player 1) takes the center square
        b.set(1,1, 1);

        assertEquals(b.state(), Board.State.INCOMPLETE);
    }

    @Test
    public void playARowAndWin() {
        Board b = new Board();

        // X (player 1) takes the center row
        b.set(1,1, 1);
        b.set(0,1, 1);
        b.set(2,1, 1);

        assertEquals(b.state(), Board.State.COMPLETE);
        assertEquals(b.winner(), 1);
    }
    @Test
    public void playARowAndOtherPlayerWin() {
        Board b = new Board();

        // O (player 2) takes the center row
        b.set(1,1, 2);
        b.set(0,1, 2);
        b.set(2,1, 2);

        assertEquals(b.state(), Board.State.COMPLETE);
        assertEquals(b.winner(), 2);
    }

    // TODO: invalid inputs to get/set
    // TODO: diagonal wins
    // TODO: vertical wins
    // TODO: draw
}
