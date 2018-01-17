package pjs.lifegame;

import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CellTest.class, Grid1DTest.class, Grid2DTest.class,
		Grid3DTest.class, GridFactoryTest.class, ParseUtilTest.class,
		SetupPropertiesTest.class })
public class AllTests {
	
	public static void main(String[] args) throws Exception {
		System.out.println("Note: Some of these tests use the default.properties values");
		System.out.println("If you have changed those values, some tests could fail.");
		JUnitCore.main("pjs.lifegame.AllTests");
	}

}
