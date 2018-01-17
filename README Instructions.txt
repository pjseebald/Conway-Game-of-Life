How to use Paul Seebald's code for Conway's Game of Life:

*******************************************************************************************
FEATURES
1) Variable grid sizes
2) Variable number of generations (states)
3) Variable number of dimensions (1D, 2D, 3D, etc. up to 10D).
4) Properties file to adjust parameters.
5) Grid file to create initial grid scenarios.

*******************************************************************************************
HOW TO BUILD

1) Use Ant 1.7+ to run build.xml. Open the command line and change directory to the project's folder.
	Run the following command (default target is "jar" in the build.xml file):
		ant

2) Import the Maven project into Eclipse. Use JRE 1.7+ and include JUnit 4+ library and run build.xml as ant build.
	Make sure to run target "jar" (default is "help" target).
	
Note that the seebald_game_of_life.jar file is included and can be run without needing to build the code.


*******************************************************************************************
HOW TO RUN

Uses JDK 1.7+

Inputs:
1) *.properties file.
	These are the initial settings for the game. Define the number of dimensions, number of generations to run, and change the default parameters depending on what dimensions you're working in. Comments in the default.properties file explains what each property is used for. Create your own *.properties file for different scenarios.
	
2) *.grid file.
	These are the initial live points within the grid. They are zero-based (i.e. if there are 10 columns, then the coordinates have a range of 0-9). Set whatever points you want to be live and see how they evolve with the number of generations (set in the properties file). In terms of dimensions, the first number is the number of columns, second is rows, third is the 3rd dimension, fourth is the 4th dimension and so on. The current maximum number of dimensions is set to 10.
	
Outputs:
All output is to the command line through System.out.

-- Run as a standard jar file (runs with default.properties and default.grid as inputs):
	java -jar seebald_game_of_life.jar

-- Run to view the help:
	java -jar seebald_game_of_life.jar -h

-- Run with sample properties and sample grid files:
	java -jar seebald_game_of_life.jar -p .\samples\3d.properties -i .\samples\3d.grid

*******************************************************************************************
HOW TO RUN UNIT TESTS

Requires:
1) junit.jar and hamcrest-core-1.3.0.jar files in the local .\lib folder.
2) default.properties file in same folder as the jar file.

-- Run unit tests (note: do not change default.properties, otherwise some tests will fail)
	java -cp seebald_game_of_life.jar pjs.lifegame.AllTests
