package hw3;

import static api.Orientation.*;
import static api.CellType.*;

import java.util.ArrayList;

import api.Cell;
import api.Orientation;

/**
 * Utilities for parsing string descriptions of a grid.
 * 
 * <p>This helper class is <b>pure</b>: it has no fields/state and only
 * contains static methods that convert a 2‑D array of single‑character
 * strings into the model objects used elsewhere in the assignment
 * (namely {@link api.Cell} and {@link hw3.Boulder}).  Nothing here mutates
 * global state, so repeated calls are safe and deterministic.</p>
 * 
 * <p><strong>Why two passes?</strong>  We first build the raw grid of {@link api.Cell}s
 * so that the <code>Board</code> constructor can assume every legal coordinate
 * already has a <code>Cell</code> object.  Only after that do we scan for
 * contiguous boulder segments, because their orientation/length information
 * spans multiple cells.</p>
 * 
 * @author Yousif Lubbad
 */
public class GridUtil {
	//------------------------------------------------------------------
	// Public API – First pass: symbol → Cell[][]
	//------------------------------------------------------------------
	/**
	 * Builds and returns a 2‑D array of {@link api.Cell} objects that
	 * mirrors the dimensions of the <code>desc</code> argument.
	 * <p>
	 * The mapping from single‑character symbols to {@link api.CellType}s
	 * is defined by the assignment specification (see table below).
	 * Any symbol that represents part of a boulder is treated exactly
	 * the same as '.' – we create a <code>GROUND</code> cell now and let
	 * {@link #findBoulders(String[][])} handle constructing the actual
	 * boulder objects later.
	 * <pre>
	 *   "*"  → WALL
	 *   "e"  → EXIT
	 *   "." → GROUND
	 *   "[", "]", "^", "v", "#" → GROUND (occupied later)
	 * </pre>
	 *
	 * @param desc 2‑D array of single‑character strings describing a board
	 * @return fully‑populated 2‑D array of {@link api.Cell} (no boulders yet)
	 */
	public static Cell[][] createGrid(String[][] desc) {
		// allocate result array with the same shape as desc
		Cell[][] grid = new Cell[desc.length][desc[0].length]; // rows × cols

		// iterate over every coordinate in row‑major order (human‑friendly)
		for (int y = 0; y < desc.length; y++) {
			for (int x = 0; x < desc[0].length; x++) {
				String symbol = desc[y][x]; // current character at (y,x)

				// Decide the cell type based on the symbol.
				// human note: "equals" not "==" because these are String objects.
				if (symbol.equals("*")) { // wall – impassable to boulders
					grid[y][x] = new Cell(y, x, WALL);
				} else if (symbol.equals("e")) { // exit – win condition
					grid[y][x] = new Cell(y, x, EXIT);
				} else {
					// '.', '[', ']', '^', 'v', '#' all treated as ground for now
					grid[y][x] = new Cell(y, x, GROUND);
				}
				// end selection for this coordinate
			}
			// finished one row, continue outer loop
		}
		return grid; // note: no boulders placed here – handled in second pass
	}

	//------------------------------------------------------------------
	// Public API – Second pass: symbol sequence → Boulder list
	//------------------------------------------------------------------
	/**
	 * Scans the <code>desc</code> array and constructs a list of
	 * {@link hw3.Boulder} instances that correspond to the contiguous
	 * sequences of boulder‑symbols found.
	 * <p>
	 * Strategy: single sweep left‑to‑right / top‑to‑bottom.  Whenever we
	 * encounter an unvisited start‑marker ('[' for horizontal, '^' for
	 * vertical) we walk along that axis until we hit its matching end
	 * marker, counting length as we go.  A parallel <code>visited</code>
	 * boolean matrix ensures each symbol is processed exactly once.</p>
	 * 
	 * @param desc same 2‑D description array passed to {@link #createGrid}
	 * @return list of fully‑initialised {@link hw3.Boulder} objects
	 */
	public static ArrayList<Boulder> findBoulders(String[][] desc) {
		ArrayList<Boulder> result = new ArrayList<>();              // final list to return

		boolean[][] visited = new boolean[desc.length][desc[0].length]; // tracks consumed symbols

		// outer loops enumerate every coordinate exactly once
		for (int row = 0; row < desc.length; row++) {
			for (int col = 0; col < desc[0].length; col++) {
				if (visited[row][col]) continue; // skip already‑handled cells

				String ch = desc[row][col]; // symbol at (row,col)

				// ---------- horizontal boulder starting with '[' ----------
				if (ch.equals("[")) {
					int len = 1; // '[' counts as first segment
					int c = col + 1; // start checking the cell to the right

					// walk right until we find the corresponding ']'.
					// human note: assignment guarantees valid input, so we don't
					// need to guard against missing ']'.
					while (c < desc[0].length && (desc[row][c].equals("#") || desc[row][c].equals("]"))) {
						len++; // extend length counter
						visited[row][c] = true; // mark interior/end segment
						if (desc[row][c].equals("]")) break; // end marker found
						c++; // otherwise, keep walking right
					}
					visited[row][col] = true;              // mark '[' itself
					result.add(new Boulder(row, col, len, HORIZONTAL)); // stash new Boulder

				// ---------- vertical boulder starting with '^' -----------
				} else if (ch.equals("^")) {
					int len = 1;
					int r = row + 1;
					while (r < desc.length && (desc[r][col].equals("#") || desc[r][col].equals("v"))) {
						len++;
						visited[r][col] = true;
						if (desc[r][col].equals("v")) break; // bottom reached
						r++; // otherwise continue downward
					}
					visited[row][col] = true; // mark '^'
					result.add(new Boulder(row, col, len, VERTICAL));
				}
				// ---------------------------------------------------------
			}
			// completed one column for this row
		}
		return result; // ready for Board constructor
	}
}
