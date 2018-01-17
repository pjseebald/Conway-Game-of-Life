package pjs.lifegame;

import java.util.List;

/**
 * <h1>GameRules</h1>
 * Uses the rules of the game to determine whether the cell is live or dead.
 * 
 * @author Paul Seebald
 * @version 1.0
 * @since 2018-01-12
 */
public class GameRules {
	
	/**
	 * Checks if the cell should be live or dead based on its value and the total neighbor value.
	 * @param cell			Cell to check (if live or dead), according to game rules.
	 * @return boolean		New cell value, true if cell is live, false if not.
	 */
	public static boolean isCellLive(Cell cell) {
		// If the cell is live, use the list that checks if it will stay live.
		// If the cell is dead, use the list that checks if it should become live.
		List<Integer> liveList = cell.getValue() ? 
				SetupProperties.getIntListProperty("stay.live") : SetupProperties.getIntListProperty("dead.live");
		return liveList.contains(cell.getNeighborValue());
	}

}
