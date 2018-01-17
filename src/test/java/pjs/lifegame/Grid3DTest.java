package pjs.lifegame;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class Grid3DTest {

	GridND grid;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// Set properties to defaults
		SetupProperties.setProperty("stay.live", "2,3");
		SetupProperties.setProperty("dead.live", "3");
	}

	@Before
	public void setUpBefore() throws Exception {
		grid = (GridND) (new GridFactory()).getGrid(Arrays.asList(new Integer[] {4,3,2}));
	}

	@Test
	public void testSetCellValue() {
		List<Integer> coordinate = Arrays.asList(new Integer[] {1,2,0});
		
		grid.setCellValue(coordinate, true);
		assertTrue(grid.getCellFromCoordinate(coordinate).getValue());
	}
	
	@Test
	public void testUpdateNeighborValues() {
		List<Integer> coordinate = Arrays.asList(new Integer[] {2,1,0});
		Cell mainCell = grid.getCellFromCoordinate(coordinate);
		
		List<Integer> neighborCoordinate = new ArrayList<>();
		for (int i = 0; i < coordinate.size(); i++) {
			neighborCoordinate.add(coordinate.get(i));
		}
		
		for (int i = 0; i < coordinate.size(); i++) {
			neighborCoordinate.set(i, neighborCoordinate.get(i)+1);
			grid.setCellValue(neighborCoordinate, true);
		}
		
		grid.updateNeighborValues(coordinate, mainCell);
		assertEquals(3, mainCell.getNeighborValue());
	}

	@Test
	public void testUpdateAllCellValues() {
		List<Integer> coordinate = Arrays.asList(new Integer[] {2,1,0});
		Cell mainCell = grid.getCellFromCoordinate(coordinate);
		
		List<Integer> neighborCoordinate = Arrays.asList(new Integer[] {3,2,1});
		grid.setCellValue(neighborCoordinate, true);
		neighborCoordinate.set(0, 1);
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
		grid.setCellValue(Arrays.asList(new Integer[] {0,0,0}), true);
		grid.setCellValue(Arrays.asList(new Integer[] {1,1,0}), true);
		grid.setCellValue(Arrays.asList(new Integer[] {2,2,0}), true);
		grid.setCellValue(Arrays.asList(new Integer[] {3,2,0}), true);
		grid.setCellValue(Arrays.asList(new Integer[] {3,2,1}), true);
		grid.setCellValue(Arrays.asList(new Integer[] {2,0,1}), true);
		grid.setCellValue(Arrays.asList(new Integer[] {1,0,1}), true);
		grid.setCellValue(Arrays.asList(new Integer[] {0,0,1}), true);
		
		String gridDisplay = grid.getDisplay();
		String sep = System.lineSeparator();
		String expectedGridDisplay = "( , , 0 )" + sep + 
				" O - - -" + sep +
				" - O - -" + sep +
				" - - O O" + sep + sep +
				"( , , 1 )" + sep + 
				" O O O -" + sep +
				" - - - -" + sep +
				" - - - O" + sep + sep;
		assertEquals(expectedGridDisplay, gridDisplay);
	}
	
	@Test
	public void testNewGeneration() {
		List<Integer> coordinate = Arrays.asList(new Integer[] {2,1,0});
		Cell mainCell = grid.getCellFromCoordinate(coordinate);
		
		List<Integer> neighborCoordinate = new ArrayList<>();
		for (int i = 0; i < coordinate.size(); i++) {
			neighborCoordinate.add(coordinate.get(i));
		}
		
		for (int i = 0; i < coordinate.size(); i++) {
			neighborCoordinate.set(i, neighborCoordinate.get(i)+1);
			grid.setCellValue(neighborCoordinate, true);
		}

		grid.advanceGeneration();

		assertTrue(mainCell.getValue());
		assertEquals(0, mainCell.getNeighborValue());
		
	}

}
