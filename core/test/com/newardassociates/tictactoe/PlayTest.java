package com.newardassociates.tictactoe;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class PlayTest {
    @Test
    public void playGame1Wins() {
        Board b = new Board();

        b.play(1,1); // X center-center
        assertEquals(b.state(), Board.State.INCOMPLETE);
        b.play(0,0); // O upper-left
        assertEquals(b.state(), Board.State.INCOMPLETE);
        b.play(1,0); // X upper-center
        assertEquals(b.state(), Board.State.INCOMPLETE);
        b.play(0,1); // O mid-left
        assertEquals(b.state(), Board.State.INCOMPLETE);
        b.play(1,2); // X lower-center WIN
        assertEquals(b.state(), Board.State.COMPLETE);
        assertEquals(b.winner(), 1); // X WIN
    }

    @Test
    public void playGameDraw() {
        Board b = new Board();

        b.play(0,0); // X upper-left
            // [X] [ ] [ ]
            // [ ] [ ] [ ]
            // [ ] [ ] [ ]

        b.play(1,0); // O upper-center
            // [X] [O] [ ]
            // [ ] [ ] [ ]
            // [ ] [ ] [ ]

        b.play(2,0); // X upper-right
            // [X] [O] [X]
            // [ ] [ ] [ ]
            // [ ] [ ] [ ]

        b.play(1,1); // O center-center
            // [X] [O] [X]
            // [ ] [O] [ ]
            // [ ] [ ] [ ]

        b.play(0,1); // X mid-left
            // [X] [O] [X]
            // [X] [O] [ ]
            // [ ] [ ] [ ]

        b.play(0,2); // O lower-left
            // [X] [O] [X]
            // [X] [O] [ ]
            // [O] [ ] [ ]
        
        b.play(1,2); // X lower-center
            // [X] [O] [X]
            // [X] [O] [ ]
            // [O] [X] [ ]

        b.play(2,1); // O center-mid
            // [X] [O] [X]
            // [X] [O] [O]
            // [O] [X] [ ]

        b.play(2,2); // X lower-right
            // [X] [O] [X]
            // [X] [O] [O]
            // [O] [X] [X]

        assertEquals(b.state(), Board.State.COMPLETE);
        assertEquals(b.winner(), -1); // DRAW
    }
}
