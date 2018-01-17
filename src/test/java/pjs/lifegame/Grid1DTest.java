package pjs.lifegame;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class Grid1DTest {
	
	static GridFactory factory = new GridFactory();
	Grid1D grid;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// Set properties to defaults
		SetupProperties.setProperty("stay.live", "2,3");
		SetupProperties.setProperty("dead.live", "3");
	}

	@Before
	public void setUpBefore() throws Exception {
		this.grid = (Grid1D) factory.getGrid(this.getCoordinate(10));
	}

	@Test
	public void testSetCellValue() {
		List<Integer> tempList = this.getCoordinate(2);
		grid.setCellValue(tempList, true);
		assertTrue(grid.getCellFromCoordinate(tempList).getValue());
	}
	
	@Test
	public void testPositiveIndexOutOfBounds() {
		Cell cell = grid.getCellFromCoordinate(this.getCoordinate(12));
		assertNull(cell);
	}
	
	@Test
	public void testNegativeIndexOutOfBounds() {
		Cell cell = grid.getCellFromCoordinate(this.getCoordinate(-2));
		assertNull(cell);
	}
	
	@Test
	public void testUpdateNeighbors() {
		int index = 2;
		Cell cell = grid.getCellFromCoordinate(this.getCoordinate(index));
		assertEquals(0, cell.getNeighborValue());

		grid.setCellValue(this.getCoordinate(index-1), true);
		grid.setCellValue(this.getCoordinate(index+1), true);
		
		grid.updateNeighborValues(this.getCoordinate(index), cell);
		assertEquals("Error updating two cells", 2, cell.getNeighborValue());
	}
	
	@Test
	public void testUpdateNotNeighbors() {
		int index = 2;
		Cell cell = grid.getCellFromCoordinate(this.getCoordinate(index));
		assertEquals(0, cell.getNeighborValue());

		grid.setCellValue(this.getCoordinate(index-2), true);
		grid.updateNeighborValues(this.getCoordinate(index), cell);
		assertEquals("Error updating one cell", 0, cell.getNeighborValue());
		
		grid.setCellValue(this.getCoordinate(index+2), true);
		grid.updateNeighborValues(this.getCoordinate(index), cell);
		assertEquals("Error updating two cells", 0, cell.getNeighborValue());
	}
	
	@Test
	public void testDisplay() {
		grid.setCellValue(this.getCoordinate(1), true);
		grid.setCellValue(this.getCoordinate(3), true);
		grid.setCellValue(this.getCoordinate(7), true);
		
		String disp = grid.getDisplay();
		assertEquals(" - O - O - - - O - -" + System.lineSeparator(), disp);
	}
	
	@Test
	public void testNextGeneration() {
		grid.setCellValue(this.getCoordinate(1), true);
		grid.setCellValue(this.getCoordinate(2), true);
		grid.setCellValue(this.getCoordinate(3), true);
		grid.setCellValue(this.getCoordinate(4), true);
		grid.setCellValue(this.getCoordinate(7), true);
		
		assertEquals(" - O O O O - - O - -" + System.lineSeparator(), grid.getDisplay());

		grid.advanceGeneration();
		
		assertEquals(" - - O O - - - - - -" + System.lineSeparator(), grid.getDisplay());
	}
	
	private List<Integer> getCoordinate(int index) {
		return Arrays.asList(new Integer[] {index});
	}

}
