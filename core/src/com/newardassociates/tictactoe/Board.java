package com.newardassociates.tictactoe;

/**
 * Basic representation of a TTT game board.
 * 
 * Later, add flexible sizing (4x4, 5x5, etc).
 */
public class Board {
	private int[][] board;
	private int currentPlayer = 1; // start with X

	// TODO: Refactor to use these to allow for easier
	// variation in board size later
	private final int numRows = 3;
	private final int numCols = 3;

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
		reset(3);
	}

	/**
	 * Clear out a Board and start from scratch
	 */
	public void reset(int size) {
		board = new int[size][];
		for (int i=0; i<size; i++)
			board[i] = new int[size];
	}

	/**
	 * Return player number in that spot, or 0
	 */
	public int get(int x, int y) {
		if (x < 0 || x > 2 || y < 0 || y > 2)
			throw new IllegalArgumentException("Out of board range");

			return board[x][y];
	}
	/**
	 * Set player number in that spot
	 */
	void set(int x, int y, int player) {
		if (x < 0 || x > 2 || y < 0 || y > 2)
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
		// check for rows for each player
		for (int p=1; p<=2; p++)
		{
			// check horizontal rows
			for (int y = 0; y < 3; y++)
			{
				boolean complete = true;
				for (int x = 0; x < 3; x++) {
					if (get(x, y) != p) {
						complete = false;
						break;
					}
				}
				if (complete)
					return p;
			}

			// check vertical
			for (int x = 0; x < 3; x++)
			{
				boolean complete = true;
				for (int y = 0; y < 3; y++) {
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
				for (int d=0; d < 3; d++) {
					if (get(d, d) != p) {
						complete = false;
						break;
					}
				}	
				if (complete)
					return p;

				complete = true;
				for (int d=0; d < 3; d++) {
					if (get(d, (2 - d)) != p) {
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
		for (int x=0; x<3; x++)
			for (int y=0; y<3; y++)
				if (get(x,y) == 0)
					filled = false;
		if (filled)
			return -1;

		// we're still going, then
		return 0;
	}

	@Override
	public String toString() {
		return "BOARD: [ [" + get(0,0) + "]  [" + get(1, 0) + "]  [" + get(2, 0) + "]\n" +
			"         [" + get(0,1) + "]  [" + get(1, 1) + "]  [" + get(2, 1) + "]\n" +
			"         [" + get(0,2) + "]  [" + get(1, 2) + "]  [" + get(2, 2) + "]  ]";
	} 
}
