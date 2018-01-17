package pjs.lifegame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <h1>Grid1D</h1>
 * This grid is separate because it is the foundation of all higher dimension grids.
 * Essentially a line of cells. This is where the cells are stored, and methods in higher 
 * dimensions like getting a cell, setting a cell value, etc. all call down to this level.
 * 
 * @author Paul Seebald
 * @version 1.0
 * @since 2018-01-12
 *
 */
public class Grid1D extends Grid {

	private List<Cell> cells = new ArrayList<>();
	
	protected Grid1D(List<Integer> dimSizes) {
		super(dimSizes.get(0));
		this.createCells();
	}
	
	/**
	 * Initiates the cells when grid is created. Gives them initial value of false (dead) to each Cell.
	 */
	private void createCells() {
		for (int i = 0; i < this.dimSize; i++) {
			cells.add(new Cell(false));
		}
	}

	/**
	 * Sets neighbor values for all cells to zero (happens after advancing generation).
	 */
	@Override
	protected void resetAllNeighborValues() {
		for (Cell cell : cells) {
			cell.resetNeighborValue();
		}
	}

	/**
	 * Gets a cell at a particular point in the 1D grid.
	 */
	@Override
	public Cell getCellFromCoordinate(List<Integer> inputCoordinate) {
		// Check for valid coordinate
		if (!this.isCoordinateOnGrid(inputCoordinate)) {
			return null;
		}
		
		return this.cells.get(inputCoordinate.get(this.getIndex()));
	}

	/**
	 * Checks if the inputCoordinate is on the grid or not.
	 * Example: dimSize is 10 and coordinate is {20} --> returns false.
	 * @param inputCoordinate	Coordinate to check.
	 * @return boolean 			True if coordinate is on the grid (valid), false if not.
	 */
	private boolean isCoordinateOnGrid(List<Integer> inputCoordinate) {
		int coord = inputCoordinate.get(this.getIndex());
		return (coord >= 0) && (coord < this.dimSize);
	}

	/**
	 * Gets a cell at a particular coordinate and changes its value to the input
	 * parameter value.
	 * @param inputCoordinate	Coordinate of the required cell.
	 * @param value				New value for required cell.
	 */
	@Override
	public void setCellValue(List<Integer> inputCoordinate, boolean value) {
		this.getCellFromCoordinate(inputCoordinate).setValue(value);
	}
	
	/**
	 * Entry method for top-level grid to update all neighbor values.
	 * Only accessed if this Grid1D is the master grid at the top level.
	 */
	public void updateAllNeighborValues() {
		List<Integer> coordinate = new ArrayList<>();
		coordinate.add(0);				// Starting coordinate
		this.updateAllNeighborValues(coordinate, this);
	}
	
	/**
	 * Start with coordinate, cycle through every cell in this grid and update its 
	 * neighbor values. Need to update them from the top-level/master grid, though (which is the 
	 * input parameter grid).
	 * @param coordinate	Starting coordinate.
	 * @param grid			Master/top-level grid to update values from.
	 */
	@Override
	protected void updateAllNeighborValues(List<Integer> coordinate, Grid grid) {
		for (int i = 0; i < this.getSize(); i++) {
			// Cycle through every coordinate within this dimension
			coordinate.set(this.getIndex(), i);
			grid.updateNeighborValues(coordinate, this.getCellFromCoordinate(coordinate));
		}
		// Reset this level of coordinate back to zero
		coordinate.set(this.getIndex(), 0);
	}

	/**
	 * Checks each neighbor of the input cell and updates the cell neighbor value if the neighbor is live.
	 * @param inputCoordinate	Coordinate of the current cell OR a specific neighbor.
	 * 							Only a neighbor coordinate if coming from higher grid/dimension.
	 * @param cell				Cell whose neighbor value is to be updated.
	 * 							May be a cell from a different grid.
	 */
	@Override
	public void updateNeighborValues(List<Integer> inputCoordinate, Cell cell) {
		// Want neighbors on either side of cell in this dimension
		
		int index = this.getIndex();
		
		int curDimCoord = inputCoordinate.get(index);
		for (Integer i : Arrays.asList(new Integer[] {-1, 1})) {
			inputCoordinate.set(index, curDimCoord+i);
			
			try {
				if(this.getCellFromCoordinate(inputCoordinate).getValue()) {
					// If the neighbor has true value, add one to neighbor value of cell
					cell.increaseNeighborValue();
				}
			} catch (NullPointerException e) {
				// If null, just keep going to find neighbors in the grid.
				continue;
			}
		}
		
		// Setting inputCoordinate back to original value because this is referenced
		// in any grids above this one.
		inputCoordinate.set(index, curDimCoord);
	}
	
	/**
	 * In a coordinate, gets the position of the specific coordinate for this grid.
	 * Example: for a coordinate {2,1,0}, the 2 is the coordinate for this 1D grid.
	 * The index for 1D grid is always 0.
	 * @return index
	 */
	private int getIndex() {
		return 0;
	}

	/**
	 * Should be called only after updating all neighbor values.
	 * This method calls the game rules. The game rules will check if the neighbor values
	 * (and cell value) match the conditions for the cell to be live or dead.  
	 */
	@Override
	public void updateAllCellValues() {
		for (Cell cell : cells) {
			cell.setValue(GameRules.isCellLive(cell));
		}
	}
	
	/**
	 * Display is the string representation of the grid.
	 * This method is the entry point for the master/top-level grid.
	 * @return display	Representation of the grid.
	 */
	@Override
	public String getDisplay() {
		return this.getDisplay(null);
	}
	
	/**
	 * Builds the string for the grid. Checks each cell, puts an O if the cell is live
	 * and puts a - if the cell is dead.
	 * @param indices	Represent the current indices of the grid.
	 * 					Not used in 1D grid.
	 * @return display	String (line) showing which cells are live and dead.
	 */
	@Override
	public String getDisplay(List<Integer> indices) {
		StringBuilder baseBuilder = new StringBuilder();
		for (Cell cell : cells) {
			if (cell.getValue()) {
				baseBuilder.append(" O");
			} else {
				baseBuilder.append(" -");
			}
		}
		baseBuilder.append(System.lineSeparator());

		return baseBuilder.toString();
	}

}
