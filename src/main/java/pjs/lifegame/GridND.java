package pjs.lifegame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <h1>GridND</h1>
 * Multi-dimensional grid, with 2+ dimensions. Contains a list of other sub-grids, 
 * 	either Grid1D (if this is a 2D grid), or GridND (if 3D+ grid). Many of the methods used here
 *  end up calling down to the lowest level grid (i.e. Grid1D) even just to get a cell.
 * 
 * @author Paul Seebald
 * @version 1.0
 * @since 2018-01-12
 */
public class GridND extends Grid {

	private int dimensions;
	
	private List<Grid> grids = new ArrayList<>();
	
	/**
	 * Initializes with a size and number of dimensions.
	 * @param numDimensions
	 * @param dimSizes
	 */
	public GridND(int numDimensions, List<Integer> dimSizes) {
		super(dimSizes.get(numDimensions-1));
		this.dimensions = numDimensions;
		this.createSubGrids(dimSizes);
	}
	
	/**
	 * Initializes the list of sub-grids through the GridFactory.
	 * @param dimSizes
	 */
	private void createSubGrids(List<Integer> dimSizes) {
		GridFactory factory = new GridFactory();
		for (int i = 0; i < this.dimSize; i++) {
			grids.add(factory.getGrid(dimensions-1, dimSizes));
		}
	}
	
	/**
	 * Gets the position of this grid's index in a coordinate.
	 * @return index
	 */
	private int getIndex() {
		return this.dimensions-1;
	}

	/**
	 * Sets all cell neighbor values to zero.
	 */
	@Override
	protected void resetAllNeighborValues() {
		for (Grid grid : grids) {
			grid.resetAllNeighborValues();
		}
	}

	/**
	 * Gets a cell at a given coordinate parameter. Uses sub-grids to get it. 
	 * @return cell
	 */
	@Override
	public Cell getCellFromCoordinate(List<Integer> inputCoordinate) {
		if (!this.isCoordinateOnGrid(inputCoordinate)) {
			return null;
		}
		
		return this.getGridAtCoordinate(inputCoordinate).getCellFromCoordinate(inputCoordinate);
	}

	/**
	 * Checks if coordinate is on the grid (valid).
	 * @param inputCoordinate	Coordinate of cell.
	 * @return boolean			True if coordinate is on the grid (valid), false if not.
	 */
	private boolean isCoordinateOnGrid(List<Integer> inputCoordinate) {
		int coord = inputCoordinate.get(this.getIndex());
		return (coord >= 0) && (coord < this.dimSize);
	}

	/**
	 * Sets a cell value where cell is at the coordinate.
	 * @param inputCoordinate	Coordinate of cell.
	 * @param value				New value for cell.
	 */
	@Override
	public void setCellValue(List<Integer> inputCoordinate, boolean value) {
		this.getGridAtCoordinate(inputCoordinate).setCellValue(inputCoordinate, value);
	}
	
	/**
	 * Retrieves the sub-grid given a coordinate.
	 * @param inputCoordinate	Coordinate of a cell.
	 * @return Grid				Sub-grid associated with input coordinate.
	 */
	private Grid getGridAtCoordinate(List<Integer> inputCoordinate) {
		return this.grids.get(inputCoordinate.get(this.getIndex()));
	}
	
	/**
	 * Updates all the neighbor values for all cells within this grid.
	 * Entry point for the master or top-level grid.
	 */
	public void updateAllNeighborValues() {
		// Create coordinate with same size as number of dimensions of master grid (this)
		List<Integer> coordinate = new ArrayList<>();
		for (int i = 0; i < this.dimensions; i++) {
			coordinate.add(0);
		}
		
		// Need to pass the top level grid down, because updateNeighborValues MUST be run from the top level
		// Otherwise it won't catch all the neighbors
		this.updateAllNeighborValues(coordinate, this);
	}
	
	/**
	 * Update neighbor values for all cells. This is called from both the entry point and upper-level grids.
	 * @param coordinate
	 * @param grid
	 */
	@Override
	protected void updateAllNeighborValues(List<Integer> coordinate, Grid grid) {
		for (int i = 0; i < this.getSize(); i++) {
			// Cycle through every coordinate within this dimension
			coordinate.set(this.getIndex(), i);
			// Keep moving down the grids to cycle through every coordinate in each one.
			this.getGridAtCoordinate(coordinate).updateAllNeighborValues(coordinate, grid);
		}
		// Reset this level of coordinate back to zero
		coordinate.set(this.getIndex(), 0);
	}
	
	/**
	 * Checks each neighbor of the input cell and updates the value if the neighbor is live.
	 * @param inputCoordinate	Coordinate of the current cell OR a specific neighbor.
	 * 							Only a neighbor coordinate if coming from higher grid/dimension.
	 * @param cell				Cell whose neighbor value is to be updated.
	 * 							May be a cell from a different grid.
	 */
	@Override
	public void updateNeighborValues(List<Integer> inputCoordinate, Cell cell) {
		// Want neighbors on either side of cell in this dimension
		this.getGridAtCoordinate(inputCoordinate).updateNeighborValues(inputCoordinate, cell);
		int curDimCoord = inputCoordinate.get(this.getIndex());
		for (Integer i : Arrays.asList(new Integer[] {-1, 1})) {
			inputCoordinate.set(this.getIndex(), curDimCoord+i);
			
			// Check for null (i.e. neighbor is not on grid)
			try {
				if(this.getCellFromCoordinate(inputCoordinate).getValue()) {
					cell.increaseNeighborValue();
				}
			} catch (NullPointerException e) {
				// If null, just keep going to find neighbors in the grid.
				continue;
			}
			// Also need to get lesser dimension neighbors around this neighbor.
			// (i.e. for 2D, this is the [-1,-1],[-1,1],[1,-1],[1,1] )
			this.getGridAtCoordinate(inputCoordinate).updateNeighborValues(inputCoordinate, cell);
		}
		inputCoordinate.set(this.getIndex(), curDimCoord);
	}
	
	/**
	 * Should be called only after updating all neighbor values.
	 * Updates all the cell values based on their neighbor values, calls down to sub-grids.
	 */
	@Override
	public void updateAllCellValues() {
		for (int i = 0; i < this.dimSize; i++) {
			this.grids.get(i).updateAllCellValues();
		}
	}
	
	/**
	 * Display is the string representation of the grid.
	 * This method is the entry point for the master/top-level grid.
	 * @return display	String representation of the grid.
	 */
	@Override
	public String getDisplay() {
		return this.getDisplay(new ArrayList<Integer>());
	}
	
	/**
	 * Builds the string for the grid. Adds some formatting depending on the dimension.
	 * If this is a 3D grid, then display the coordinate for the current grid display.
	 * The indices are used to manage the coordinate.
	 * @param indices		Current indices of higher level dimensions
	 * @return display		String display of grid
	 */
	@Override
	public String getDisplay(List<Integer> indices) {
		boolean addIndexCheck = (this.dimensions > 2);
		int index = indices.size();
		if (addIndexCheck) indices.add(0);
		boolean addFormattingCheck = (this.dimensions == 3);

		StringBuilder nDBuilder = new StringBuilder();
		for (Grid grid : grids) {
			if (addFormattingCheck) {
				nDBuilder.append("( , ");
				for (int j = indices.size()-1; j >= 0; j--) {
					nDBuilder.append(", " + indices.get(j));
				}
				nDBuilder.append(" )" + System.lineSeparator());
			}

			nDBuilder.append(grid.getDisplay(indices));
			if (addIndexCheck) indices.set(index, indices.get(index)+1);
			
			if (addFormattingCheck) nDBuilder.append(System.lineSeparator());
		}
		
		if (addIndexCheck) indices.remove(index);

		return nDBuilder.toString();
	}

}
