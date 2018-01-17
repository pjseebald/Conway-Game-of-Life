package pjs.lifegame;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

public class SetupPropertiesTest {
	
	private static String propertyLocation;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		propertyLocation = GameApp.defaultPropertyLocation;
		SetupProperties.setPropertiesLocation(propertyLocation);
		
		// Set properties to defaults
		SetupProperties.setProperty("grid.size", "8,6");
		SetupProperties.setProperty("generations", "1");
		SetupProperties.setProperty("show.all.generations", "T");
		SetupProperties.setProperty("stay.live", "2,3");
		SetupProperties.setProperty("dead.live", "3");
		
	}

	@Test
	public void testGetStringProperty() {
		String value = SetupProperties.getProperty("dead.live");
		assertEquals("3", value);
	}
	
	@Test
	public void testGetIntProperty() {
		Integer value = SetupProperties.getIntProperty("generations");
		assertEquals(1,value.intValue());
	}
	
	@Test
	public void testGetBooleanProperty() {
		Boolean value = SetupProperties.getBooleanProperty("show.all.generations");
		assertEquals(true, value);
	}
	
	@Test
	public void testGetIntListProperty() {
		List<Integer> value = SetupProperties.getIntListProperty("grid.size");
		assertArrayEquals(new Integer[] {8,6}, value.toArray());
	}
	
	@Test
	public void testGetIntPropertyAsList() {
		List<Integer> value = SetupProperties.getIntListProperty("generations");
		assertArrayEquals(new Integer[] {1}, value.toArray());
	}
	
	@Test(expected=RuntimeException.class)
	public void testGetNonexistentProperty() {
		SetupProperties.getProperty("non.property");
	}
	
	@Test(expected=RuntimeException.class)
	public void testGetInvalidIntProperty() {
		SetupProperties.getIntProperty("show.all.generations");
	}
	
	@Test(expected=RuntimeException.class)
	public void testGetInvalidBooleanProperty() {
		SetupProperties.getBooleanProperty("generations");
	}
	
	@Test(expected=RuntimeException.class)
	public void testGetInvalidIntListProperty() {
		SetupProperties.getIntListProperty("show.all.generations");
	}


}
