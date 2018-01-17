package pjs.lifegame;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class GridFactoryTest {
	
	GridFactory factory = new GridFactory();
	Integer input;
	Integer output;
	
	public GridFactoryTest(Integer input, Integer expectedOutput) {
		this.input = input;
		this.output = expectedOutput;
	}
	
	@Parameters
	public static Collection<Integer[]> testConditions() {
		Integer expectedOutputs[][] = {{1,15},{3,11},{10,4},{-1,2}};
		return Arrays.asList(expectedOutputs);
	}
	
	@Test
	public void testCreateGrid() {

		List<Integer> dims = getGridDims(input, output);
		
		Grid grid = factory.getGrid(input, dims);
		if (input < 1) {
			assertNull(grid);
			return;
		}
		
		if (input == 1) {
			assertTrue(grid instanceof Grid1D);
		} else {
			assertTrue(grid instanceof GridND);
		}
		
		assertEquals(output.intValue(), grid.getSize());
		
	}

	private static List<Integer> getGridDims(int n, int dimSize) {
		List<Integer> gridList = new ArrayList<>();
		
		for (int i = 0; i < n; i++) {
			gridList.add(dimSize);
		}
		
		return gridList;
	}

}
