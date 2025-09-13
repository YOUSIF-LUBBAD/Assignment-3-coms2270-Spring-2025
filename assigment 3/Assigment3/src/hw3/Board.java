package hw3;

import static api.Direction.*;
import static api.Orientation.*;

import java.util.ArrayList;

import api.Cell;
import api.Direction;
import api.Move;

/**
 * Represents a board in the game. A board contains a 2D grid of cells and a
 * list of boulders that slide over the cells.
 *
 *@author Yousif Lubbad
 */
public class Board {
	/**
	 * 2D array of cells, the indexes signify (row, column) with (0, 0) representing
	 * the upper-left corner of the board.
	 */
	private Cell[][] grid;

	/**
	 * A list of boulders that are positioned on the board.
	 */
	private ArrayList<Boulder> boulders;

	/**
	 * A list of moves that have been made in order to get to the current position
	 * of boulders on the board.
	 */
	private ArrayList<Move> moveHistory;
	
	private Boulder grabbed;
	private boolean gameOver;

	/**
	 * Constructs a new board from a given 2D array of cells and list of boulders. The
	 * cells of the grid should be updated to indicate which cells have boulders
	 * placed over them (i.e., placeBoulder() method of Cell). The move history should
	 * be initialized as empty.
	 * 
	 * @param grid   a 2D array of cells which is expected to be a rectangular shape
	 * @param boulders list of boulders already containing row-column position which
	 *               should be placed on the board
	 */
	public Board(Cell[][] grid, ArrayList<Boulder> boulders) {
		this.grid = grid;                  // Assign the provided grid to the internal grid reference
		this.boulders = boulders;          // Store the list of boulders on the board
		moveHistory = new ArrayList<>();   // Initialize empty list to keep track of all moves made
		gameOver = false;                  // Start game in non-finished state
		grabbed = null;                    // No boulder grabbed at initialization

		// Loop through each boulder to place them on the board
		for (Boulder b : boulders) {
			int row = b.getFirstRow();     // Starting row of the boulder
			int col = b.getFirstCol();     // Starting column of the boulder

			// Loop through all cells that the boulder occupies and place it
			for (int i = 0; i < b.getLength(); i++) {
				grid[row][col].placeBoulder(b); // Mark cell as occupied by the boulder
				if (b.getOrientation() == VERTICAL) row++; // Move down if vertical
				else col++;                                // Move right if horizontal
			}
		}
		// TODO - Constructor initialized the board state
	}


	/**
	 * DO NOT MODIFY THIS CONSTRUCTOR
	 * <p>
	 * Constructs a new board from a given 2D array of String descriptions.
	 * 
	 * @param desc 2D array of descriptions
	 */
	public Board(String[][] desc) {
		this(GridUtil.createGrid(desc), GridUtil.findBoulders(desc));
	}

	/**
	 * Returns the number of rows of the board.
	 * 
	 * @return number of rows
	 */
	public int getRowSize() {
		// Return total number of rows in the board grid
		return grid.length;
	}


	/**
	 * Returns the number of columns of the board.
	 * 
	 * @return number of columns
	 */
	public int getColSize() {
		// Return number of columns by checking the length of first row
		return grid[0].length;
	}

	/**
	 * Returns the cell located at a given row and column.
	 * 
	 * @param row the given row
	 * @param col the given column
	 * @return the cell at the specified location
	 */
	public Cell getCellAt(int row, int col) {
		// Retrieve specific cell using row and column index
		return grid[row][col];
	}


	/**
	 * Returns the total number of moves (calls to moveGrabbedBoulder which
	 * resulted in a boulder being moved) made so far in the game.
	 * 
	 * @return the number of moves
	 */
	public int getMoveCount() {
		// TODO
		return moveHistory.size(); // Return the number of valid moves recorded in move history
	}

	/**
	 * Returns a list of all boulders on the board.
	 * 
	 * @return a list of all boulders
	 */
	public ArrayList<Boulder> getBoulders() {
		// TODO
		return boulders; // Return current list of boulders on the board
	}

	/**
	 * Returns true if the player has completed the puzzle by positioning a boulder
	 * over an exit, false otherwise.
	 * 
	 * @return true if the game is over
	 */
	public boolean isGameOver() {
		// Return true if a boulder has reached an exit cell
		return gameOver;
	}

	
	/**
	 * Models the user grabbing (mouse button down) a boulder over the given row and
	 * column. The purpose of grabbing a boulder is for the user to be able to drag
	 * the boulder to a new position, which is performed by calling
	 * moveGrabbedBoulder().
	 * <p>
	 * This method should find which boulder has been grabbed (if any) and record
	 * that boulder as grabbed in some way.
	 * 
	 * @param row row to grab the boulder from
	 * @param col column to grab the boulder from
	 */
	public void grabBoulderAt(int row, int col) {
		// Get boulder from the specified cell
		Boulder b = grid[row][col].getBoulder();

		// If there is a boulder, set it as currently grabbed
		if (b != null) grabbed = b;

		// TODO - Used for interaction (e.g., mouse click)
	}


	/**
	 * Models the user releasing (mouse button up) the currently grabbed boulder
	 * (if any). Update the object accordingly to indicate no boulder is
	 * currently being grabbed.
	 */
	public void releaseBoulder() {
		// TODO // Release the currently grabbed boulder by setting to null
		grabbed = null;
	}

	/**
	 * Returns the currently grabbed boulder. If there is no currently grabbed
	 * boulder the method return null.
	 * 
	 * @return the currently grabbed boulder or null if none
	 */
	public Boulder getGrabbedBoulder() {
		// TODO // Return the boulder that is currently being moved, or null
		return grabbed;
	}

	/**
	 * Returns true if the cell at the given row and column is available for a
	 * boulder to be placed over it. Boulders can only be placed over ground
	 * and exits. Additionally, a boulder cannot be placed over a cell that is
	 * already occupied by another boulder.
	 * 
	 * @param row row location of the cell
	 * @param col column location of the cell
	 * @return true if the cell is available for a boulder, otherwise false
	 */
	public boolean isAvailable(int row, int col) {
		// TODO Check if coordinates are within bounds and if the cell is ground or exit and unoccupied
		return (row >= 0 && row < getRowSize() && col >= 0 && col < getColSize()) &&
		       (grid[row][col].isGround() || grid[row][col].isExit()) && grid[row][col].getBoulder() == null;
	}

	/**
	 * Moves the currently grabbed boulder by one cell in the given direction. A
	 * horizontal boulder is only allowed to move right and left and a vertical boulder
	 * is only allowed to move up and down. A boulder can only move over a cell that
	 * is a floor or exit and is not already occupied by another boulder. The method
	 * does nothing under any of the following conditions:
	 * <ul>
	 * <li>The game is over.</li>
	 * <li>No boulder is currently grabbed by the user.</li>
	 * <li>A boulder is currently grabbed by the user, but the boulder is not allowed to
	 * move in the given direction.</li>
	 * </ul>
	 * If none of the above conditions are meet, the method does at least the following:
	 * <ul>
	 * <li>Moves the boulder object by calling its move() method.</li>
	 * <li>Calls placeBoulder() for the grid cell that the boulder is being moved into.</li>
	 * <li>Calls removeBoulder() for the grid cell that the boulder is being moved out of.</li>
	 * <li>Adds the move (as a Move object) to the end of the move history list.</li>
	 * <li>Increments the count of total moves made in the game.</li>
	 * </ul>
	 * 
	 * @param dir the direction to move
	 */
	public void moveGrabbedBoulder(Direction dir) {
		// Exit if game is over or no boulder is grabbed
		if (gameOver || grabbed == null) return;

		// Prevent invalid moves (e.g., UP/DOWN on HORIZONTAL)
		if ((grabbed.getOrientation() == HORIZONTAL && (dir == UP || dir == DOWN)) ||
			(grabbed.getOrientation() == VERTICAL && (dir == LEFT || dir == RIGHT))) return;

		int row = grabbed.getFirstRow();
		int col = grabbed.getFirstCol();
		int len = grabbed.getLength();

		// Compute new position to check if move is legal
		int newRow = row;
		int newCol = col;
		if (grabbed.getOrientation() == HORIZONTAL) newCol = (dir == RIGHT) ? col + len : col - 1;
		else newRow = (dir == DOWN) ? row + len : row - 1;

		// Check if the destination is valid and unoccupied
		if (!isAvailable(newRow, newCol)) return;

		// Remove boulder from current cells
		for (int i = 0; i < len; i++) {
			if (grabbed.getOrientation() == HORIZONTAL) grid[row][col + i].removeBoulder();
			else grid[row + i][col].removeBoulder();
		}

		// Move the boulder in the specified direction
		grabbed.move(dir);

		// Place boulder into new cells
		int r = grabbed.getFirstRow();
		int c = grabbed.getFirstCol();
		for (int i = 0; i < len; i++) {
			grid[r][c].placeBoulder(grabbed);
			if (grabbed.getOrientation() == HORIZONTAL) c++;
			else r++;
		}

		// Add move to history
		moveHistory.add(new Move(grabbed, dir));

		// Check if new position reached an exit
		if (checkExit(grabbed)) gameOver = true;
	}


	private boolean checkExit(Boulder b) {
		// Loop through all cells the boulder occupies
		int r = b.getFirstRow();
		int c = b.getFirstCol();
		for (int i = 0; i < b.getLength(); i++) {
			if (grid[r][c].isExit()) return true;
			if (b.getOrientation() == HORIZONTAL) c++;
			else r++;
		}
		// If no exit found under boulder
		return false;
	}


	/**
	 * Resets the state of the game back to the start, which includes the move
	 * count, the move history, and whether the game is over. The method calls the
	 * reset method of each boulder object. It also updates each grid cells by calling
	 * their placeBoulder method to either set a boulder if one is located over the cell
	 * or set null if no boulder is located over the cell.
	 */
	public void reset() {
		// Reset position of all boulders
		for (Boulder b : boulders) b.reset();

		// Clear all boulders from grid cells
		for (Cell[] row : grid)
			for (Cell c : row) c.removeBoulder();

		// Re-place all boulders in original positions
		for (Boulder b : boulders) {
			int r = b.getFirstRow();
			int c = b.getFirstCol();
			for (int i = 0; i < b.getLength(); i++) {
				grid[r][c].placeBoulder(b);
				if (b.getOrientation() == HORIZONTAL) c++;
				else r++;
			}
		}

		grabbed = null;           // No boulder is currently being moved
		gameOver = false;         // Reset game state
		moveHistory.clear();      // Clear all move history
	}


	/**
	 * Returns a list of all legal moves that can be made by any boulder on the
	 * current board.
	 * 
	 * @return a list of legal moves
	 */
	
		public ArrayList<Move> getAllPossibleMoves() {
			ArrayList<Move> all = new ArrayList<>();
			// TODO
			for (Boulder b : boulders) {
				grabbed = b; // Temporarily treat this boulder as grabbed

				int r = b.getFirstRow();
				int c = b.getFirstCol();

				// Check all directions
				for (Direction d : Direction.values()) {
					if ((b.getOrientation() == HORIZONTAL && (d == LEFT || d == RIGHT)) ||
						(b.getOrientation() == VERTICAL && (d == UP || d == DOWN))) {

						// Compute the cell to check based on direction
						int checkR = b.getOrientation() == HORIZONTAL ? r : (d == UP ? r - 1 : r + b.getLength());
						int checkC = b.getOrientation() == VERTICAL ? c : (d == LEFT ? c - 1 : c + b.getLength());

						// Only add move if it's valid
						if (checkR >= 0 && checkR < getRowSize() && checkC >= 0 && checkC < getColSize()) {
							if (isAvailable(checkR, checkC))
								all.add(new Move(b, d));
						}
					}
				}
			}
			grabbed = null; // Clear temp grab
			return all;
		}


	/**
	 * Gets the list of all moves performed to get to the current position on the
	 * board.
	 * 
	 * @return a list of moves performed to get to the current position
	 */
		public ArrayList<Move> getMoveHistory() {
			// Return the list of all moves made so far
			return moveHistory;
		}


	/**
	 * EXTRA CREDIT 5 POINTS
	 * <p>
	 * This method is only used by the Solver.
	 * <p>
	 * Undo the previous move. The method gets the last move on the moveHistory list
	 * and performs the opposite actions of that move, which are the following:
	 * <ul>
	 * <li>if required, sets is game over to false</li>
	 * <li>grabs the moved boulder and calls moveGrabbedBoulder passing the opposite
	 * direction</li>
	 * <li>decreases the total move count by two to undo the effect of calling
	 * moveGrabbedBoulder twice</li>
	 * <li>removes the move from the moveHistory list</li>
	 * </ul>
	 * If the moveHistory list is empty this method does nothing.
	 */
		public void undoMove() {
			// Do nothing if no moves to undo
			if (moveHistory.size() == 0) return;

			gameOver = false; // Reset game-over in case last move reached exit

			// Get and remove the most recent move
			Move last = moveHistory.remove(moveHistory.size() - 1);
			Boulder moved = last.getBoulder();

			// Determine opposite direction
			Direction opp = switch (last.getDirection()) {
				case UP -> DOWN;
				case DOWN -> UP;
				case LEFT -> RIGHT;
				case RIGHT -> LEFT;
			};

			grabbed = moved;

			// Move back in opposite direction (which also adds to history)
			moveGrabbedBoulder(opp);

			// Remove the new history entry created by moveGrabbedBoulder
			moveHistory.remove(moveHistory.size() - 1);

			// Reset grabbed state
			grabbed = null;
		}


		@Override
		public String toString() {
			StringBuffer buff = new StringBuffer();
			boolean first = true;
			for (Cell row[] : grid) {
				if (!first) {
					buff.append("\n");
				} else {
					first = false;
				}
				for (Cell cell : row) {
					buff.append(cell.toString());
					buff.append(" ");
				}
			}
			return buff.toString(); // Return board representation as a string
		}
}
