package pjs.lifegame;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <h1>GameApp</h1>
 * Main controller for the Conway's Game of Life.
 * 
 * Features added:
 * 1) Variable grid sizes
 * 2) Variable number of generations (states)
 * 3) Variable number of dimensions (1D, 2D, 3D, etc. up to 10D).
 * 4) Properties file to adjust parameters.
 * 5) Grid file to create initial grid scenarios.
 * 
 * @author Paul Seebald
 * @version 1.0
 * @since 2018-01-12
 */

public class GameApp {
	
	public static String propertyLocation;
	public static String initGridLocation;
	
	public static String defaultPropertyLocation = "default.properties";
	public static String defaultInitGridLocation = "default.grid";
	
	private static boolean help = false;

	
	/**
	 * Main method, controls the game.
	 * Uses initial properties and grid to create (and print to screen) a master starting grid.
	 * Advances it the number of generations specified in the properties file and prints the final
	 * grid to the screen. 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			
			// Start by parsing the arguments
			parseArguments(args);
			
			// Show the help if that was passed as an argument
			if (help) {
				System.out.println();
				System.out.println("To use this program, run with JDK 1.7+ " + System.lineSeparator());
				System.out.println("** There are three possible properties: ");
				System.out.println("** [-props | -p] <property file location> : file location of setup properties. Use './" + defaultPropertyLocation + "' for default values and reference.");
				System.out.println("** [-initGrid | -i] <initial grid file location> : file location of initial grid values. Use './" + defaultInitGridLocation + "' for default values and reference.");
				System.out.println("** [-help | -h] : Show help, does not run the rest of the program." + System.lineSeparator());
				System.out.println("Note: running the jar without any properties will use default properties and initial grid locations" + System.lineSeparator());
				System.out.println("Example to run default properties and grid:");
				System.out.println("java -jar seebald_game_of_life.jar" + System.lineSeparator());
				System.out.println("Example to run custom properties and grid:");
				System.out.println("java -jar seebald_game_of_life.jar -p .\\my.properties -i .\\my.grid" + System.lineSeparator());
				System.out.println("Example to run help: ");
				System.out.println("java -jar seebald_game_of_life.jar -h" + System.lineSeparator());
				return;
			}
			
			System.out.println("Note: To read the help, run the command:");
			System.out.println("java -jar seebald_game_of_life.jar -h" + System.lineSeparator());

			// Check number of dimensions
			List<Integer> dimensions = SetupProperties.getIntListProperty("grid.size");
			if (!checkValidDimensions(dimensions)) return;
			
			// Get initial grid from file
			List<List<Integer>> initGrid = initializeGrid(initGridLocation);
			if (!checkValidInitGrid(dimensions, initGrid)) return;

			// Create master (top-level) grid
			GridFactory gridFactory = new GridFactory();
			Grid masterGrid = gridFactory.getGrid(dimensions);

			// Set the values for the initial grid
			masterGrid.setCellValues(initGrid, true);

			// Show the initial grid
			printGrid(masterGrid, "Initial Grid: ");

			// Advance the generations
			int generations = SetupProperties.getIntProperty("generations");
			if (generations < 1 || generations > 1000) {
				System.out.println("Error: generations property is outside the range of 1 to 1000");
			}
			masterGrid.advanceGenerations(generations, 
					SetupProperties.getBooleanProperty("show.all.generations"));

			// Show the final grid
			printGrid(masterGrid, "Final grid after " + generations + " generations: ");

		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return;
		}
	}

	/**
	 * Parses the input string arguments. Specifically looks for help, properties file and
	 * initial grid file locations.
	 * @param args		String args passed when running the GameApp main method.
	 */
	private static void parseArguments(String[] args) {
		if (args.length == 0) {
			propertyLocation = "default.properties";
			initGridLocation = "default.grid";
		}
		
		int index = 0;
		while (index < args.length) {
			switch (args[index]) {
				case "-help": case "-h":
					help = true;
					break;
				case "-initGrid": case "-i":
					initGridLocation = args[++index];
					break;
				case "-props": case "-p":
					propertyLocation = args[++index];
					break;
			}
			index++;
		}
		
		// Set default values if they were not set in args
		if (initGridLocation == null) {
			initGridLocation = defaultInitGridLocation;
		}
		
		if (propertyLocation == null) {
			propertyLocation = defaultPropertyLocation;
			SetupProperties.setPropertiesLocation(propertyLocation);
		}
	}
	
	/**
	 * Initializes the grid. This means it takes the list of coordinates in the initial grid
	 * file and sets all of the cells at those coordinates to true (live). 
	 * @param gridFileLocation		Location of the initial grid file.
	 * @return initGrid				List of coordinates for live cells in the initial grid.
	 */
	private static List<List<Integer>> initializeGrid(String gridFileLocation) {
		System.out.println("Reading initial grid file" + System.lineSeparator());
		try (BufferedReader gridStream = new BufferedReader(new FileReader(gridFileLocation)) ) {
			List<List<Integer>> initGrid = new ArrayList<>();
			String curLine = gridStream.readLine();
			
			while (curLine != null) {
				String curLineNoWhiteSpace = curLine.replaceAll("\\s+",""); 
				if (curLineNoWhiteSpace.length() > 0 && !(curLine.charAt(0) == '#')) {
					// Remove any spaces and split into a list of numbers
					List<String> curLineList = Arrays.asList(curLineNoWhiteSpace.split(","));
					initGrid.add(ParseUtil.convertStrListToIntList(curLineList));
				}

				curLine = gridStream.readLine();
			}

			return initGrid;
		} catch(Exception e) {
			System.out.println(e.getMessage());
			System.out.println("File at location " + gridFileLocation + " could not be read");
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Prints the input message, then the grid to the screen.
	 * @param grid		Grid to print to screen.
	 * @param message	Message to print to screen.
	 */
	private static void printGrid(Grid grid, String message) {
		System.out.println(message);
		System.out.println(grid.getDisplay());
	}
	
	/**
	 * Checks if the dimension sizes are valid numbers.
	 * Example is negative number, as dimension size cannot be negative.
	 * Current maximum is 10 dimensions, but program is set up to go higher if needed.
	 * @param dimensions	List of dimension sizes.
	 * @return boolean		Are the dimension sizes valid or not? Returns true or false.
	 */
	private static boolean checkValidDimensions(List<Integer> dimensions){
		if ((dimensions.size() < 1) || (dimensions.size() > 10)) {
			System.out.println("Grid size property does not have the right number of dimensions.");
			System.out.println("Dimensions are currently " + dimensions.size() + " but must be in the range of 1-10.");
			return false;
		}
		
		for (Integer dim : dimensions) {
			if (dim < 1 || dim > 100) {
				System.out.println("Dimension size is out of bounds with value of: " + dim);
				System.out.println("Size must be between 1 and 100 (inclusive). Check properties file.");
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Checks if the initial grid coordinates are valid or not.
	 * @param dimensions	Sizes of the dimensions.
	 * @param initGrid		Initial grid coordinates for live cells.
	 * @return boolean		Are initial grid coordinates valid? Returns true or false.
	 */
	private static boolean checkValidInitGrid(List<Integer> dimensions, List<List<Integer>> initGrid) {

		for (List<Integer> coordinate : initGrid) {
			if (coordinate.size() != dimensions.size()) {
				// Coordinate is not same size as dimensions, invalid.
				// Example: coordinate = {1,1,2}, dimension sizes = {10,10}
				System.out.println("Initial grid has a coordinate with a different number of dimensions than defined in the properties file.");
				System.out.println("Grid size: " + coordinate.size());
				System.out.println("Grid coordinate: " + coordinate);
				System.out.println("Properties grid size: " + dimensions.size());
				return false;
			}
			for (int i = 0; i < coordinate.size(); i++) {
				if ( (coordinate.get(i) < 0) || (coordinate.get(i) >= dimensions.get(i)) ) {
					// Coordinate defined in initial grid is out of bounds for defined dimensions in properties file
					System.out.println("Initial grid has a coordinate with a value smaller than 0 or larger than defined in properties file.");
					System.out.println("Grid coordinate: " + coordinate);
					System.out.println("Properties grid size: " + dimensions.get(i) + " at (zero-based) index: " + i);
					return false;
				}
			}
		}
		
		return true;
	}

}
