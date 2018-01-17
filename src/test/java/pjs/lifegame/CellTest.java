package pjs.lifegame;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CellTest {

	Cell cell;
	
	@Before
	public void setUpBefore() throws Exception {
		cell = new Cell(false);
	}

	@Test
	public void testIncreaseNeighborValue() {
		int neighbors = 2;
		assertEquals(0, cell.getNeighborValue());
		
		for (int i = 0; i < neighbors; i++) {
			cell.increaseNeighborValue();
		}
		
		assertEquals(neighbors, cell.getNeighborValue());
	}
	
	@Test
	public void testSetCellValue() {
		assertFalse(cell.getValue());
		cell.setValue(true);
		assertTrue(cell.getValue());
	}
	
	@Test
	public void testResetNeighborValue() {
		int neighbors = 4;
		for (int i = 0; i < neighbors; i++) {
			cell.increaseNeighborValue();
		}
		
		assertEquals(neighbors, cell.getNeighborValue());
		
		cell.resetNeighborValue();
		
		assertEquals(0, cell.getNeighborValue());
		
	}

}
