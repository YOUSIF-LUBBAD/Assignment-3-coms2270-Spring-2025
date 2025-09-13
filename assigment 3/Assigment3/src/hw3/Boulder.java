package hw3;

import static api.Direction.*;
import static api.Orientation.HORIZONTAL;
import static api.Orientation.VERTICAL;

import api.Direction;
import api.Orientation;

/**
 * Represents a boulder in the game.
 * 
 * @author Yousif Lubbad
 */
public class Boulder {

	private int positionY;       // current row (topmost if vertical)
	private int positionX;       // current column (leftmost if horizontal)
	private int length;          // number of cells the boulder occupies
	private Orientation orientation; // HORIZONTAL or VERTICAL

	private int originx;         // original column (start position)
	private int originy;         // original row (start position)
	/**
	 * Constructs a new Boulder with a specific location relative to the board. The
	 * upper/left most corner of the boulder is given as firstRow and firstCol. All
	 * boulders are only one cell wide. The length of the boulder is specified in cells.
	 * The boulder can either be horizontal or vertical on the board as specified by
	 * orientation.
	 * 
	 * @param firstRow    the first row that contains the boulder
	 * @param firstCol    the first column that contains the boulder
	 * @param length      boulder length in cells
	 * @param orientation either HORIZONTAL or VERTICAL
	 */
	public Boulder(int firstRow, int firstCol, int length, Orientation orientation) {
		// Assign the row position to the given firstRow (used as Y coordinate)
		this.positionY = firstRow;

		// Assign the column position to the given firstCol (used as X coordinate)
		this.positionX = firstCol;

		// Store the provided length of the boulder (how many cells it occupies)
		this.length = length;

		// Set the orientation of the boulder (HORIZONTAL or VERTICAL)
		this.orientation = orientation;

		// Save the initial X position to be used for resetting
		this.originx = firstCol;

		// Save the initial Y position to be used for resetting
		this.originy = firstRow;
	}

	/**
	 * Resets the boulder's current position back to where it originally started.
	 * Uses the saved originx and originy values from when it was first created.
	 */
	public void reset() {
		// Restore the Y coordinate to the original starting row
		positionY = originy;

		// Restore the X coordinate to the original starting column
		positionX = originx;

		// No need to reset length or orientation as those are fixed
	}

	/**
	 * Moves the boulder one cell in a valid direction depending on its orientation.
	 * Only certain directions are allowed:
	 *   - Horizontal boulders can move LEFT or RIGHT
	 *   - Vertical boulders can move UP or DOWN
	 * Invalid directions for the orientation are ignored.
	 * 
	 * @param dir The direction to attempt to move the boulder.
	 */
	public void move(Direction dir) {

		// Check if the boulder is horizontal
		if (orientation == Orientation.HORIZONTAL ) {

			// If the move is RIGHT and the orientation is correct, move right
			if (dir == Direction.RIGHT ) {
				positionX ++;
			}
			// If the move is LEFT and the orientation is correct, move left
			else if (dir == Direction.LEFT ) {
				positionX --;
			}
		}

		// Check if the boulder is vertical
		if (orientation == Orientation.VERTICAL ) {

			// If the move is DOWN and the orientation is correct, move down
			if (dir == Direction.DOWN ) {
				positionY ++;
			}
			// If the move is UP and the orientation is correct, move up
			else if (dir == Direction.UP ) {
				positionY --;
			}
		}

		// Any direction that doesn't match the boulder's orientation does nothing
	}

	/**
	 * Returns the current row (Y coordinate) of the boulder's first/topmost cell.
	 * 
	 * @return Current top row of the boulder.
	 */
	public int getFirstRow() {
		return positionY ;
	}

	/**
	 * Sets the row (Y coordinate) of the boulder's first/topmost cell.
	 * Useful when placing or adjusting the boulder's position manually.
	 * 
	 * @param firstRow The row to set as the new position.
	 */
	public void setFirstRow(int firstRow) {
		positionY = firstRow;
	}

	/**
	 * Returns the current column (X coordinate) of the boulder's leftmost cell.
	 * 
	 * @return Current first column of the boulder.
	 */
	public int getFirstCol() {
		return positionX;
	}

	/**
	 * Sets the column (X coordinate) of the boulder's leftmost cell.
	 * Useful for positioning or resetting the boulder manually.
	 * 
	 * @param firstCol The column to set as the new position.
	 */
	public void setFirstCol(int firstCol) {
		positionX = firstCol;
	}

	/**
	 * Returns the total number of cells the boulder occupies.
	 * Length is fixed once set and doesn't change over time.
	 * 
	 * @return Length of the boulder in cells.
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Returns the orientation of the boulder: HORIZONTAL or VERTICAL.
	 * This determines the valid movement directions.
	 * 
	 * @return Orientation of the boulder.
	 */
	public Orientation getOrientation() {
		return orientation;
	} 
	
	/**
	 * Returns a string representation of the boulder object.
	 * This is useful for debugging or printing boulder info.
	 * Example output: (row=2, col=4, len=3, ori=HORIZONTAL)
	 */
	@Override
	public String toString() {
		return "(row=" + getFirstRow() + ", col=" + getFirstCol() + ", len=" + getLength()
				+ ", ori=" + getOrientation() + ")";
	}
}
