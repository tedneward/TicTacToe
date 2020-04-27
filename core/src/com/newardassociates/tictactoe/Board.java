package com.newardassociates.tictactoe;

/**
 * Basic representation of a TTT game board.
 * 
 * Later, add flexible sizing (4x4, 5x5, etc).
 */
public class Board {
	private int[][] board;
	private int currentPlayer = 1; // start with X

	/**
	 * The number of rows in the Board
	 */
	public final int numRows;

	/**
	 * The number of columns in the Board
	 */
	public final int numCols;

	/**
	 * Gives us an indication of what is currently going
	 * on in the game.
	 * 
	 * COMPLETE: we have a victory or all squares are filled
	 * INCOMPLETE: not complete
	 */
	public enum State {
		COMPLETE, INCOMPLETE
	}

	/**
	 * Construct a 3x3 game board
	 */
	public Board() {
		this(3);
	}

	/**
	 * Construct a Board of given size (square)
	 * @param size
	 */
	public Board(int size) {
		this.numRows = size;
		this.numCols = size;

		reset();
	}

	/**
	 * Clear out a Board and start from scratch
	 */
	public void reset() {
		board = new int[numCols][];
		for (int i=0; i<numCols; i++)
			board[i] = new int[numRows];

		currentPlayer = 1; // X always goes first
	}

	/**
	 * Return player number in that spot, or 0
	 */
	public int get(int x, int y) {
		if (x < 0 || x > (numCols-1) || y < 0 || y > (numRows-1))
			throw new IllegalArgumentException("Out of board range");

			return board[x][y];
	}
	/**
	 * Set player number in that spot
	 */
	void set(int x, int y, int player) {
		if (x < 0 || x > (numCols-1) || y < 0 || y > (numRows-1))
			throw new IllegalArgumentException("Out of board range");

		if (player < 1 || player > 2)
			throw new IllegalArgumentException("How many players you got, anyway?!?");

		board[x][y] = player;
	}

	/**
	 * Player makes a move; this changes the current player setting
	 */
	public void play(int x, int y) {
		set(x, y, currentPlayer);
		// flip between players
		currentPlayer = (currentPlayer == 1) ? 2 : 1;
	}
	public int currentPlayer() {
		return currentPlayer;
	}

	public State state() {
		if (winner() != 0)
			return State.COMPLETE;

		return State.INCOMPLETE;
	}

	public int winner() {
		// check for each player
		for (int p=1; p<=2; p++)
		{
			// check horizontal rows
			for (int y = 0; y < numRows; y++)
			{
				boolean complete = true;
				for (int x = 0; x < numCols; x++) {
					if (get(x, y) != p) {
						complete = false;
						break;
					}
				}
				if (complete)
					return p;
			}

			// check vertical
			for (int x = 0; x < numCols; x++)
			{
				boolean complete = true;
				for (int y = 0; y < numRows; y++) {
					if (get(x, y) != p) {
						complete = false;
						break;
					}
				}
				if (complete)
					return p;
			}

			// check diagonals
			{
				boolean complete = true;
				for (int d=0; d < numRows; d++) {
					if (get(d, d) != p) {
						complete = false;
						break;
					}
				}	
				if (complete)
					return p;

				complete = true;
				for (int d=0; d < numRows; d++) {
					if (get(d, (numRows-1 - d)) != p) {
						complete = false;
						break;
					}
				}
				if (complete)
					return p;
			}
		}

		// check if all squares are filled; all squares filled
		// and no winner (checked above) means we have a draw
		boolean filled = true;
		for (int x=0; x<numCols; x++)
			for (int y=0; y<numRows; y++)
				if (get(x,y) == 0)
					filled = false;
		if (filled)
			return -1;

		// we're still going, then
		return 0;
	}

	@Override
	public String toString() {
		// Offset is to assume "GameScreen" is the tag in the log in front of it
		return "BOARD: [ [" + get(0,0) + "]  [" + get(1, 0) + "]  [" + get(2, 0) + "]\n" +
			"                      [" + get(0,1) + "]  [" + get(1, 1) + "]  [" + get(2, 1) + "]\n" +
			"                      [" + get(0,2) + "]  [" + get(1, 2) + "]  [" + get(2, 2) + "]  ]";
	} 
}
