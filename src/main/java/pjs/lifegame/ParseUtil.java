package pjs.lifegame;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>ParseUtil</h1>
 * Utility methods to help with parsing from files. Particularly lists of integers.
 * 
 * @author Paul Seebald
 * @version 1.0
 * @since 2018-01-12
 */
public class ParseUtil {

	/**
	 * Converts a list of strings to a list of integers. Catches any conversion issues.
	 * @param strList			Initial list of values as string.
	 * @return List<Integer>	Final list of values as integers.
	 */
	public static List<Integer> convertStrListToIntList(List<String> strList) {
		List<Integer> intList = new ArrayList<>();
		
		try {
			for (String str : strList) {
				intList.add(Integer.valueOf(str));
			}
		} catch (NumberFormatException e) {
			System.out.println(e.getClass() + ": " + e.getMessage());
			throw new RuntimeException("Could not convert string to integer - check syntax in properties and initial grid files.");
		}
		
		return intList;
	}
	
}
