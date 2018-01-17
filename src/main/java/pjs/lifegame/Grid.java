package pjs.lifegame;

import java.util.List;

/**
 * <h1>Grid</h1>
 * Grid is actually a representation of a slice of a dimension.
 * The 1D grid (i.e. line of cells) is the foundation of all possible grids.
 * Each Grid contains no information about any grids above it.
 * For example, a 2D grid that is 3 columns by 4 rows will have 
 * 	four 1D grids that are each 3 cells long horizontally. This creates a 3x4 grid:
 * 2D Grid:
 * 				o	o	o		--> 1D Grid
 * 				o	o	o		--> 1D Grid
 * 				o	o	o		--> 1D Grid
 * 				o	o	o		--> 1D Grid
 * 
 * The above example would have five total grids created, one to represent the 2D grid, four
 * 	for the 1D grids. This also creates a hierarchy where there is only one top-level grid.
 * In another way to view it, if a 4D grid is defined, the representation is a List<List<List<List<Cell>>>>
 * 
 * @author Paul Seebald
 * @version 1.0
 * @since 2018-01-12
 */

public abstract class Grid {
	
	public int dimSize;
	
	/**
	 * Grid represents a slice of a dimension.
	 * @param dSize 	Size of the grid
	 * 					i.e. How many sub-grids or cells it contains.
	 */
	Grid(int dSize) {
		this.dimSize = dSize;
	}
	
	/**
	 * Getter for the grid size
	 * @return int 	The size of the grid.
	 */
	public int getSize() {
		return this.dimSize;
	}
	
	/**
	 * This method updates the neighbor values for each cell, then updates the values
	 * for each cell based on the game rules (and properties). Then it resets the neighbor
	 * values to 0 for each cell.
	 */
	public void advanceGeneration() {
		// Update neighbors first
		this.updateAllNeighborValues();
		
		// Now update all cell values
		this.updateAllCellValues();
		
		// Need to reset all neighbor values
		this.resetAllNeighborValues();
	}
	
	/**
	 * This method is similar to advanceGeneration(), but it performs n advances and displays 
	 * each genreation if the displayIntermediates is true
	 * @param n 						Number of generations to advance
	 * @param displayIntermediates 		Property set in a file to display intermediate generations or not.
	 */
	public void advanceGenerations(int n, boolean displayIntermediates) {
		for (int i = 1; i <= n; i++) {
			this.advanceGeneration();
			if (displayIntermediates && (i < n)) {
				System.out.println("Generation: " + i);
				System.out.println(this.getDisplay());
			}
		}
	}
	
	/**
	 * This method takes a list of coordinates and sets their value to be equal to the
	 * value parameter passed in. Most useful when initializing the grid with starting values.
	 * @param coordinates 	Locations of the points on the grid to initialize
	 * @param value 		The value to initialize with. Default cell value is false, so usually pass true.
	 */
	public  void setCellValues(List<List<Integer>> coordinates, boolean value) {
		for (List<Integer> coordinate : coordinates) {
			this.setCellValue(coordinate, value);
		}
	}
	
	public abstract Cell getCellFromCoordinate(List<Integer> inputCoordinate);
	
	public abstract void setCellValue(List<Integer> inputCoordinate, boolean value);
	
	public abstract void updateAllNeighborValues();
	abstract void updateAllNeighborValues(List<Integer> coordinate, Grid grid);
	public abstract void updateNeighborValues(List<Integer> inputCoordinate, Cell cell);
	
	public abstract void updateAllCellValues();
	
	public abstract String getDisplay();
	abstract String getDisplay(List<Integer> indices);

	abstract void resetAllNeighborValues();
	
}
