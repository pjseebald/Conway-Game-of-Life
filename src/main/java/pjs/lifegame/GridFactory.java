package pjs.lifegame;

import java.util.List;

/**
 * <h1>GridFactory</h1>
 * Factory class for generating grids based on dimension sizes.
 * 
 * @author Paul Seebald
 * @version 1.0
 * @since 2018-01-12
 */
public class GridFactory {
	
	/**
	 * Create a grid based on a list of dimension sizes.
	 * Uses the list to determine number of dimensions
	 * @param dimSizes		Dimension sizes
	 * @return Grid			Created grid
	 */
	public Grid getGrid(List<Integer> dimSizes) {
		return this.getGrid(dimSizes.size(), dimSizes);
	}
	
	/**
	 * Create grid based on number of dimensions and list of dimension sizes.
	 * @param n				Number of dimensions
	 * @param dimSizes		Size of dimensions
	 * @return Grid			Created grid
	 */
	public Grid getGrid(int n, List<Integer> dimSizes) {
		if (n == 1) {
			return new Grid1D(dimSizes);
		} else if (n > 1) {
			return new GridND(n, dimSizes);
		} else {
			return null;
		}
	}

}
