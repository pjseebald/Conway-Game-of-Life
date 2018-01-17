package pjs.lifegame;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ParseUtilTest {

	List<String> strings;
	
	@Before
	public void setupStrings() {
		strings = new ArrayList<>();
	}

	@Test
	public void testValidIntegers() {
		strings.add("1");
		strings.add("5");
		strings.add("22");
		strings.add("-4");
		strings.add("1");
		
		List<Integer> integers = ParseUtil.convertStrListToIntList(strings);
		assertArrayEquals(new Integer[] {1, 5, 22, -4, 1}, integers.toArray());
	}
	
	@Test(expected=RuntimeException.class)
	public void testInvalidDouble() {
		strings.add("1.2");
		strings.add("-1.5");
		
		ParseUtil.convertStrListToIntList(strings);
	}
	
	@Test(expected=RuntimeException.class)
	public void testInvalidString() {
		strings.add("hello");
		strings.add("1m");
		strings.add("w");
		
		ParseUtil.convertStrListToIntList(strings);
	}

}
