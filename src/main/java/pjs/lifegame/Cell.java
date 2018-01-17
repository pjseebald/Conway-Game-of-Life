package pjs.lifegame;

/**
 * <h1>Cell</h1>
 * The Cell class represents a single point in a grid.
 * A cell contains information about its own value and the number of live neighbors.
 * It contains no information about where it is in the grid, that is managed by the grid.
 * A cell has value true if it is live, false if it is inactive.
 * 
 * @author Paul Seebald
 * @version 1.0
 * @since 2018-01-12
 *
 */
public class Cell {

	boolean value;
	int neighbors;
	
	/**
	 * Created cell starts with a value and no information about its neighbors.
	 * @param initValue Value for the created cell.
	 * 					True is live, false is inactive.
	 */
	public Cell(boolean initValue) {
		this.value = initValue;
		this.neighbors = 0;
	}
	
	/**
	 * Getter for value field.
	 * @return value The value of the cell.
	 */
	public boolean getValue() {
		return this.value;
	}
	
	/**
	 * Setter for value field.
	 * @param updatedValue
	 */
	public void setValue(boolean updatedValue) {
		this.value = updatedValue;
	}
	
	/**
	 * Add one to the neighbor value. This means a neighbor is live (value == true).
	 */
	public void increaseNeighborValue() {
		this.neighbors++;
	}
	
	/**
	 * Set neighbor value back to zero. This occurs when generation is advanced.
	 * Used because cell history is not maintained to know if neighbor values changed.
	 */
	public void resetNeighborValue() {
		this.neighbors = 0;
	}
	
	/**
	 * Getter for neighbor value. Ultimately used to determine if it is live or inactive 
	 * in the next cycle/generation.
	 * @return neighbors
	 */
	public int getNeighborValue() {
		return this.neighbors;
	}

}
