package com.newardassociates.tictactoe

/**
 * Basic representation of a TTT game board.
 *
 * Later, add flexible sizing (4x4, 5x5, etc).
 */
class Board @JvmOverloads constructor(val numRows: Int = 3) {
    private var board: Array<IntArray?> = arrayOf()
    private var currentPlayer = 1 // start with X

    /**
     * The number of columns in the Board
     */
    val numCols: Int = numRows

    /**
     * Gives us an indication of what is currently going
     * on in the game.
     *
     * COMPLETE: we have a victory or all squares are filled
     * INCOMPLETE: not complete
     */
    enum class State {
        COMPLETE, INCOMPLETE
    }

    /**
     * Clear out a Board and start from scratch
     */
    fun reset() {
        board = arrayOfNulls(numCols)
        for (i in 0 until numCols) board[i] = IntArray(numRows)
        currentPlayer = 1 // X always goes first
    }

    /**
     * Return player number in that spot, or 0
     */
    operator fun get(x: Int, y: Int): Int {
        require(!(x < 0 || x > numCols - 1 || y < 0 || y > numRows - 1)) { "Out of board range" }
        return board[x]!![y]
    }

    /**
     * Set player number in that spot
     */
    operator fun set(x: Int, y: Int, player: Int) {
        require(!(x < 0 || x > numCols - 1 || y < 0 || y > numRows - 1)) { "Out of board range" }
        require(!(player < 1 || player > 2)) { "How many players you got, anyway?!?" }
        board[x]!![y] = player
    }

    /**
     * Player makes a move; this changes the current player setting
     */
    fun play(x: Int, y: Int) {
        set(x, y, currentPlayer)
        // flip between players
        currentPlayer = if (currentPlayer == 1) 2 else 1
    }

    fun currentPlayer(): Int {
        return currentPlayer
    }

    fun state(): State {
        return if (winner() != 0) State.COMPLETE else State.INCOMPLETE
    }

    fun winner(): Int {
        // check for each player
        for (p in 1..2) {
            // check horizontal rows
            for (y in 0 until numRows) {
                var complete = true
                for (x in 0 until numCols) {
                    if (get(x, y) != p) {
                        complete = false
                        break
                    }
                }
                if (complete) return p
            }

            // check vertical
            for (x in 0 until numCols) {
                var complete = true
                for (y in 0 until numRows) {
                    if (get(x, y) != p) {
                        complete = false
                        break
                    }
                }
                if (complete) return p
            }

            // check diagonals
            run {
                var complete = true
                for (d in 0 until numRows) {
                    if (get(d, d) != p) {
                        complete = false
                        break
                    }
                }
                if (complete) return p
                complete = true
                for (d in 0 until numRows) {
                    if (get(d, numRows - 1 - d) != p) {
                        complete = false
                        break
                    }
                }
                if (complete) return p
            }
        }

        // check if all squares are filled; all squares filled
        // and no winner (checked above) means we have a draw
        var filled = true
        for (x in 0 until numCols) for (y in 0 until numRows) if (get(x, y) == 0) filled = false
        return if (filled) -1 else 0

        // we're still going, then
    }

    override fun toString(): String {
        // Offset is to assume "GameScreen" is the tag in the log in front of it
        return """BOARD: [ [${get(0, 0)}]  [${get(1, 0)}]  [${get(2, 0)}]
                      [${get(0, 1)}]  [${get(1, 1)}]  [${get(2, 1)}]
                      [${get(0, 2)}]  [${get(1, 2)}]  [${get(2, 2)}]  ]"""
    }

    /**
     * Construct a 3x3 game board
     */
    init {
        reset()
    }
}