package hw3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import api.Direction;
import api.Move;

/**
 * A puzzle solver for the the game.
 * 
 * This class attempts to find all solutions for a given Board configuration
 * by recursively searching through all possible sequences of valid moves
 * up to a specified maximum number of allowed moves.
 * 
 * The only method the student is expected to implement is solve().
 * This method will call a helper that performs a depth-first recursive search.
 * The search must stop if:
 * - The maximum number of allowed moves is reached
 * - A winning configuration is found
 * - A configuration already visited with fewer or equal moves is encountered
 * 
 * Valid solutions must be stored in the solutions field. Each solution is a
 * list of Move objects representing one possible way to solve the puzzle.
 * 
 * @author Yousif Lubbad
 */
public class Solver {
	/**
	 * Maximum number of moves allowed in the search.
	 */
	private int maxMoves;

	/**
	 * Associates a string representation of a grid with the move count required to
	 * reach that grid layout.
	 */
	private Map<String, Integer> seen = new HashMap<>();

	/**
	 * All solutions found in this search.
	 */
	private ArrayList<ArrayList<Move>> solutions = new ArrayList<>();

	/**
	 * Constructs a solver with the given maximum number of moves.
	 * 
	 * @param givenMaxMoves maximum number of moves
	 */
	public Solver(int givenMaxMoves) {
		maxMoves = givenMaxMoves;
		solutions = new ArrayList<>();
	}

	/**
	 * Returns all solutions found in the search. Each solution is a list of moves.
	 * 
	 * @return list of all solutions
	 */
	public ArrayList<ArrayList<Move>> getSolutions() {
		return solutions;
	}

	/**
	 * Prints all solutions found in the search.
	 */
	public void printSolutions() {
		for (ArrayList<Move> moves : solutions) {
			System.out.println("Solution:");
			for (Move move : moves) {
				System.out.println(move);
			}
			System.out.println();
		}
	}

	/**
	 * EXTRA CREDIT 10 POINTS
	 * <p>
	 * Recursively search for solutions to the given board instance according to the
	 * algorithm described in the assignment pdf. This method does not return
	 * anything. Its purpose is to update the instance variable 'solutions' with every
	 * solution found. A solution is a winning board state where a boulder is on the exit.
	 * <p>
	 * This is the entry point that kicks off the recursive solving process.
	 * 
	 * @param board any instance of Board
	 */
	public void solve(Board board) {
		solveRecursive(board);
	}

	/**
	 * Recursive helper method for solving the board.
	 * 
	 * Uses depth-first search to explore all legal move sequences up to maxMoves.
	 * Utilizes a map to avoid re-exploring previously seen configurations unless
	 * they are reached in fewer moves.
	 * 
	 * If a solution is found (isGameOver is true), the current move history is
	 * deep copied and added to the list of solutions.
	 * 
	 * @param board the current board state during recursive exploration
	 */
	private void solveRecursive(Board board) {
		// Check if game is already won
		if (board.isGameOver()) {
			// Store a deep copy of current move history
			solutions.add(new ArrayList<>(board.getMoveHistory()));
			return;
		}

		// Stop searching if max moves reached
		if (board.getMoveCount() >= maxMoves) {
			return;
		}

		// Use board's string state as a unique key
		String key = board.toString();
		if (seen.containsKey(key) && seen.get(key) <= board.getMoveCount()) {
			// Already visited this state with fewer or equal moves
			return;
		}

		// Mark this state as seen with current move count
		seen.put(key, board.getMoveCount());

		// Try every legal move
		for (Move move : board.getAllPossibleMoves()) {
			/* ---------- FIXED SECTION: use correct Move accessors ---------- */
			int row = move.getBoulder().getFirstRow();
			int col = move.getBoulder().getFirstCol();
			Direction dir = move.getDirection();
			/* -------------------------------------------------------------- */

			board.grabBoulderAt(row, col);
			board.moveGrabbedBoulder(dir);
			board.releaseBoulder();

			solveRecursive(board);
			board.undoMove();
		}

	}
}
