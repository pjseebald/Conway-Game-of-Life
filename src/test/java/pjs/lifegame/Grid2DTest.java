package pjs.lifegame;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class Grid2DTest {
	
	GridND grid;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// Set properties to defaults
		SetupProperties.setProperty("stay.live", "2,3");
		SetupProperties.setProperty("dead.live", "3");
	}
	
	@Before
	public void setUpBefore() throws Exception {
		grid = (GridND) (new GridFactory()).getGrid(Arrays.asList(new Integer[] {10,5}));
	}

	@Test
	public void testSetCellValue() {
		List<Integer> coordinate = Arrays.asList(new Integer[] {3,2});
		
		grid.setCellValue(coordinate, true);
		assertTrue(grid.getCellFromCoordinate(coordinate).getValue());
	}

	@Test
	public void testUpdateNeighborValues() {
		List<Integer> coordinate = Arrays.asList(new Integer[] {3,2});
		Cell mainCell = grid.getCellFromCoordinate(coordinate);
		
		List<Integer> neighborCoordinate = Arrays.asList(new Integer[] {3,3});
		grid.setCellValue(neighborCoordinate, true);
		neighborCoordinate.set(0, 4);
		grid.setCellValue(neighborCoordinate, true);
		
		grid.updateNeighborValues(coordinate, mainCell);
		assertEquals(2, mainCell.getNeighborValue());
	}

	@Test
	public void testUpdateAllCellValues() {
		List<Integer> coordinate = Arrays.asList(new Integer[] {3,2});
		Cell mainCell = grid.getCellFromCoordinate(coordinate);
		
		List<Integer> neighborCoordinate = Arrays.asList(new Integer[] {3,3});
		grid.setCellValue(neighborCoordinate, true);
		neighborCoordinate.set(0, 4);
		grid.setCellValue(neighborCoordinate, true);
		neighborCoordinate.set(0, 2);
		neighborCoordinate.set(1, 2);
		grid.setCellValue(neighborCoordinate, true);
		
		grid.updateNeighborValues(coordinate, mainCell);
		
		grid.updateAllCellValues();
		assertTrue(mainCell.getValue());
	}

	@Test
	public void testDisplay() {
		grid.setCellValue(Arrays.asList(new Integer[] {0,0}), true);
		grid.setCellValue(Arrays.asList(new Integer[] {1,1}), true);
		grid.setCellValue(Arrays.asList(new Integer[] {2,2}), true);
		grid.setCellValue(Arrays.asList(new Integer[] {3,3}), true);
		grid.setCellValue(Arrays.asList(new Integer[] {4,4}), true);
		grid.setCellValue(Arrays.asList(new Integer[] {5,4}), true);
		grid.setCellValue(Arrays.asList(new Integer[] {6,3}), true);
		grid.setCellValue(Arrays.asList(new Integer[] {7,2}), true);
		grid.setCellValue(Arrays.asList(new Integer[] {8,1}), true);
		grid.setCellValue(Arrays.asList(new Integer[] {9,0}), true);
		
		String gridDisplay = grid.getDisplay();
		String sep = System.lineSeparator();
		String expectedGridDisplay = " O - - - - - - - - O" + sep + 
				" - O - - - - - - O -" + sep +
				" - - O - - - - O - -" + sep +
				" - - - O - - O - - -" + sep +
				" - - - - O O - - - -" + sep;
		assertEquals(expectedGridDisplay, gridDisplay);
	}
	
	@Test
	public void testNextGeneration() {
		List<Integer> coordinate = Arrays.asList(new Integer[] {3,2});
		Cell mainCell = grid.getCellFromCoordinate(coordinate);
		
		List<Integer> neighborCoordinate = Arrays.asList(new Integer[] {3,3});
		grid.setCellValue(neighborCoordinate, true);
		neighborCoordinate.set(0, 4);
		grid.setCellValue(neighborCoordinate, true);
		neighborCoordinate.set(0, 2);
		grid.setCellValue(neighborCoordinate, true);
		
		grid.advanceGeneration();
		
		assertTrue(mainCell.getValue());
		
		grid.advanceGeneration();
		
		assertFalse(mainCell.getValue());
	}

}
